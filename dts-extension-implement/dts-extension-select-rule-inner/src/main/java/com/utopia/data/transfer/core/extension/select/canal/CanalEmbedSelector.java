package com.utopia.data.transfer.core.extension.select.canal;

import com.alibaba.otter.canal.instance.core.CanalInstance;
import com.alibaba.otter.canal.instance.core.CanalInstanceGenerator;
import com.alibaba.otter.canal.instance.manager.CanalInstanceWithManager;
import com.alibaba.otter.canal.instance.manager.model.Canal;
import com.alibaba.otter.canal.instance.manager.model.CanalParameter;
import com.alibaba.otter.canal.parse.CanalEventParser;
import com.alibaba.otter.canal.parse.ha.CanalHAController;
import com.alibaba.otter.canal.parse.inbound.mysql.MysqlEventParser;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.ClientIdentity;
import com.alibaba.otter.canal.server.embedded.CanalServerWithEmbedded;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mysql.cj.conf.ConnectionUrlParser;
import com.utopia.data.transfer.core.base.config.DTSConstants;
import com.utopia.data.transfer.core.extension.select.utils.MessageDumper;
import com.utopia.data.transfer.core.extension.select.utils.MessageParser;
import com.utopia.data.transfer.model.archetype.ServiceException;
import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.data.transfer.model.code.entity.data.EventDataTransaction;
import com.utopia.data.transfer.model.code.entity.CanalSupport;
import com.utopia.data.transfer.model.code.entity.EntityDesc;
import com.utopia.data.transfer.model.code.entity.mysql.MysqlProperty;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;
import com.utopia.data.transfer.model.code.entity.data.Message;
import com.utopia.data.transfer.core.base.config.ConfigService;
import com.utopia.register.center.api.ZookeeperConfig;
import com.utopia.utils.CollectionUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SystemUtils;
import org.slf4j.MDC;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author owen.cai
 * @create_date 2021/9/15
 * @alter_author
 * @alter_date
 */
@Slf4j
public class CanalEmbedSelector {
    private static final String     DATE_FORMAT      = "yyyy-MM-dd HH:mm:ss";
    private static final String     SEP              = SystemUtils.LINE_SEPARATOR;
    private int                     logSplitSize     = 50;

    @Getter
    private Pipeline                pipeline;
    private EntityDesc              source;
    private CanalSupport            canalSupport;
    private ConfigService           configService;
    private MessageParser           messageParser;
    private ZookeeperConfig         zookeeperConfig;

    private CanalServerWithEmbedded canalServer = new CanalServerWithEmbedded();
    private ClientIdentity          clientIdentity;
    private String                  filter;

    /**
     * 是否处于运行中
     */
    private volatile boolean        running          = false;

    public CanalEmbedSelector(Long pipelineId, ConfigService configService, MessageParser messageParser, EntityDesc source, ZookeeperConfig canalZKConfig){
        this.configService = configService;
        this.pipeline = configService.getPipeline(pipelineId);
        this.messageParser = messageParser;
        this.zookeeperConfig = canalZKConfig;
        this.source = source;
    }

    public boolean isStart() {
        return running;
    }

    public void ack(Long batchId) {
        canalServer.ack(clientIdentity, batchId);
    }


    protected CanalSupport getCanalSupport(EntityDesc source){
        switch(source.getType()) {
            case MYSQL:{
                return source.getParams().toJavaObject(MysqlProperty.class);
            }
        }
        return null;
    }
    public void start(){
        if (running) {
            return;
        }

        this.canalSupport = getCanalSupport(this.source);
        if(this.canalSupport == null){
            throw new ServiceException(ErrorCode.SOURCE_TYPE_ERROR);
        }
        this.filter = CanalFilterSupport.makeFilterExpression(pipeline);

        canalServer.setCanalInstanceGenerator(new CanalInstanceGenerator() {
            @Override
            public CanalInstance generate(String entityName) {
                Canal canal = createCanalByEntity(source);

                CanalInstanceWithManager instance = new CanalInstanceWithManager(canal, filter){
                    @Override
                    protected void startEventParserInternal(CanalEventParser parser, boolean isGroup) {
                        super.startEventParserInternal(parser, isGroup);

                        if (eventParser instanceof MysqlEventParser) {
                            // 设置支持的类型
                            ((MysqlEventParser) eventParser).setSupportBinlogFormats("ROW");
                            ((MysqlEventParser) eventParser).setSupportBinlogImages("FULL");

                            MysqlEventParser mysqlEventParser = (MysqlEventParser) eventParser;
                            /**
                             * 默认不开启并行解析
                             */
                            mysqlEventParser.setParallel(false);
                            CanalHAController haController = mysqlEventParser.getHaController();

                            if (!haController.isStart()) {
                                haController.start();
                            }
                        }
                    }
                };
                return instance;
            }
        });

        canalServer.start();

        canalServer.start(pipeline.getId().toString());
        this.clientIdentity = new ClientIdentity(pipeline.getId().toString(), pipeline.getId().shortValue(), filter);
        /**
         * 发起一次订阅
         */
        canalServer.subscribe(clientIdentity);

        running = true;
    }

    public void stop() {
        if (!running) {
            return;
        }
        running = false;
        canalServer.stop(pipeline.getId().toString());
        canalServer.stop();
    }

    protected Canal createCanalByEntity(EntityDesc entityDesc){
        Canal canal = new Canal();
        canal.setId(pipeline.getId());
        canal.setName(pipeline.getId().toString());
        //canal.setStatus(CanalStatus.START);

        CanalParameter canalParameter = new CanalParameter();
        canalParameter.setZkClusterId(1L);
        canalParameter.setZkClusters(Arrays.asList(zookeeperConfig.getUrl().split(",")));
        //使用zk
        canalParameter.setMetaMode(CanalParameter.MetaMode.ZOOKEEPER);

        ConnectionUrlParser connectionUrlParser = ConnectionUrlParser.parseConnectionString(canalSupport.getUrl());
        List<CanalParameter.DataSourcing> collect = connectionUrlParser.getHosts().stream()
                .map(item -> new CanalParameter.DataSourcing(CanalParameter.SourcingType.MYSQL, new InetSocketAddress(item.getHost(), item.getPort())))
                .collect(Collectors.toList());
        canalParameter.setGroupDbAddresses(Arrays.asList(collect));
        canalParameter.setDbUsername(canalSupport.getUsername());
        canalParameter.setDbPassword(canalSupport.getPassword());

        //索引模式
        canalParameter.setIndexMode(CanalParameter.IndexMode.META);
        // slaveId = 10000 + pipelineid
        canalParameter.setSlaveId(10000 + pipeline.getId());
        //心跳检查
        canalParameter.setDetectingSQL("insert into retl.xdual values(1,now()) on duplicate key update x=now()");
        canalParameter.setDetectingIntervalInSeconds(5);

        canalParameter.setDdlIsolation(true);
        //过滤表解析异常
        canalParameter.setFilterTableError(true);
        canalParameter.setMemoryStorageRawEntry(false);

        //使用gtid
        canalParameter.setGtidEnable(true);

        canal.setCanalParameter(canalParameter);
        canal.setGmtCreate(Objects.isNull(entityDesc.getCreateTime()) ? null : Date.from(entityDesc.getCreateTime().atZone(ZoneId.of("GMT")).toInstant()));
        canal.setGmtModified(Objects.isNull(entityDesc.getModifyTime()) ? null : Date.from(entityDesc.getModifyTime().atZone(ZoneId.of("GMT")).toInstant()));

        return canal;
    }


    public Optional<Message<EventDataTransaction>> selector() throws InterruptedException {
        com.alibaba.otter.canal.protocol.Message message = canalServer.getWithoutAck(clientIdentity, pipeline.getParams().getBatchsize(), pipeline.getParams().getBatchTimeout(), TimeUnit.MILLISECONDS);;
        if (message == null || message.getId() == -1L) {
            //no data
            return Optional.empty();
        }

        if (!running) {
            throw new InterruptedException();
        }

        List<CanalEntry.Entry> entries = null;
        if (message.isRaw()) {
            entries = new ArrayList(message.getRawEntries().size());
            for (ByteString entry : message.getRawEntries()) {
                try {
                    entries.add(CanalEntry.Entry.parseFrom(entry));
                } catch (InvalidProtocolBufferException e) {
                    throw new ServiceException(ErrorCode.CANAL_PARSE_ERROR, e);
                }
            }
        } else {
            entries = message.getEntries();
        }

        // 过滤事务头/尾和回环数据
        List<EventDataTransaction> eventDatas = messageParser.parse(pipeline.getId(), entries);
        //
        Message<EventDataTransaction> result = new Message(message.getId(), eventDatas);

        if (Boolean.TRUE.equals(pipeline.getParams().getDumpSelector()) && log.isInfoEnabled()) {
            String startPosition = null;
            String endPosition = null;
            if (!CollectionUtils.isEmpty(entries)) {
                startPosition = buildPositionForDump(entries.get(0));
                endPosition = buildPositionForDump(entries.get(entries.size() - 1));
            }

            // 记录一下，方便追查问题
            dumpMessages(result, startPosition, endPosition, entries.size());
        }
        return Optional.of(result);
    }

    private String buildPositionForDump(CanalEntry.Entry entry) {
        long time = entry.getHeader().getExecuteTime();
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return entry.getHeader().getLogfileName() + ":" + entry.getHeader().getLogfileOffset() + ":"
                + entry.getHeader().getExecuteTime() + "(" + format.format(date) + ")";
    }

    /**
     * 记录一下message对象
     */
    private synchronized void dumpMessages(Message<EventDataTransaction> message, String startPosition, String endPosition, int total) {
        try {
            MDC.put(DTSConstants.splitPipelineSelectLogFileKey, String.valueOf(pipeline.getId()));
            log.info(SEP + "****************************************************" + SEP);
            log.info(MessageDumper.dumpMessageInfo(message, startPosition, endPosition, total));
            log.info("****************************************************" + SEP);
            if (Boolean.TRUE.equals(pipeline.getParams().getDumpSelectorDetail())) {
                // 判断一下是否需要打印详细信息
                dumpEventDatas(message.getDatas(), pipeline);
                log.info("****************************************************" + SEP);
            }
        } finally {
            MDC.remove(DTSConstants.splitPipelineSelectLogFileKey);
        }
    }

    /**
     * 分批输出多个数据
     */
    private void dumpEventDatas(List<EventDataTransaction> eventDatas, Pipeline pipeline) {
        int size = eventDatas.size();
        // 开始输出每条记录
        int index = 0;
        do {
            if (index + logSplitSize >= size) {
                log.info(MessageDumper.dumpEventDatas(eventDatas.subList(index, size), pipeline));
            } else {
                log.info(MessageDumper.dumpEventDatas(eventDatas.subList(index, index + logSplitSize), pipeline));
            }
            index += logSplitSize;
        } while (index < size);
    }

    public void rollback() {
        canalServer.rollback(clientIdentity);
    }
}
