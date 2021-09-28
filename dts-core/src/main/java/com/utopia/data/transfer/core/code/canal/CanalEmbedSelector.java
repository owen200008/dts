package com.utopia.data.transfer.core.code.canal;

import com.alibaba.otter.canal.instance.core.CanalInstance;
import com.alibaba.otter.canal.instance.core.CanalInstanceGenerator;
import com.alibaba.otter.canal.instance.manager.CanalInstanceWithManager;
import com.alibaba.otter.canal.instance.manager.model.Canal;
import com.alibaba.otter.canal.parse.CanalEventParser;
import com.alibaba.otter.canal.parse.ha.CanalHAController;
import com.alibaba.otter.canal.parse.inbound.mysql.MysqlEventParser;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.ClientIdentity;
import com.alibaba.otter.canal.server.embedded.CanalServerWithEmbedded;
import com.alibaba.otter.canal.sink.AbstractCanalEventSink;
import com.alibaba.otter.canal.sink.CanalEventSink;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.utopia.data.transfer.core.archetype.base.ServiceException;
import com.utopia.data.transfer.core.code.base.ErrorCode;
import com.utopia.data.transfer.core.code.base.config.DTSConstants;
import com.utopia.data.transfer.core.code.bean.Pipeline;
import com.utopia.data.transfer.core.code.model.EventData;
import com.utopia.data.transfer.core.code.model.Message;
import com.utopia.data.transfer.core.code.service.ConfigService;
import com.utopia.data.transfer.core.code.utils.MessageDumper;
import com.utopia.data.transfer.core.code.utils.MessageParser;
import com.utopia.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SystemUtils;
import org.slf4j.MDC;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

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
    private static final int        maxEmptyTimes    = 10;
    private int                     logSplitSize     = 50;

    private Long                    pipelineId;
    private ConfigService           configService;
    private MessageParser           messageParser;
    private CanalDownStreamHandler  handler;

    private CanalServerWithEmbedded canalServer = new CanalServerWithEmbedded();
    private ClientIdentity          clientIdentity;
    private String                  filter;
    private String                  destination;
    private int                     batchSize        = 10000;
    private long                    batchTimeout     = -1L;
    private boolean                 dump             = true;
    private boolean                 dumpDetail       = true;

    private volatile boolean        running          = false;                                            // 是否处于运行中
    private volatile long           lastEntryTime    = 0;

    public CanalEmbedSelector(Long pipelineId, ConfigService configService){
        this.pipelineId = pipelineId;
        this.configService = configService;
    }

    public void start(){
        if (running) {
            return;
        }
        Pipeline pipeline = configService.getPipeline(this.pipelineId);
        this.filter = CanalFilterSupport.makeFilterExpression(pipeline);
        this.destination = pipeline.getParams().getDestinationName();
        this.batchSize = pipeline.getParams().getBatchsize();
        this.batchTimeout = pipeline.getParams().getBatchTimeout();
        if (pipeline.getParams().getDumpSelector() != null) {
            dump = pipeline.getParams().getDumpSelector();
        }

        if (pipeline.getParams().getDumpSelectorDetail() != null) {
            dumpDetail = pipeline.getParams().getDumpSelectorDetail();
        }

        canalServer.setCanalInstanceGenerator(new CanalInstanceGenerator() {
            @Override
            public CanalInstance generate(String destination) {
                Canal canal = configService.getCanal(destination);
                long slaveId = 10000;// 默认基数
                if (canal.getCanalParameter().getSlaveId() != null) {
                    slaveId = canal.getCanalParameter().getSlaveId();
                }
                canal.getCanalParameter().setSlaveId(slaveId + pipelineId);
                canal.getCanalParameter().setDdlIsolation(true);
                canal.getCanalParameter().setFilterTableError(false);
                canal.getCanalParameter().setMemoryStorageRawEntry(false);

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
//                instance.setAlarmHandler(otterAlarmHandler);

                CanalEventSink eventSink = instance.getEventSink();
                if (eventSink instanceof AbstractCanalEventSink) {
                    handler = new CanalDownStreamHandler();
                    handler.setPipelineId(pipelineId);
                    handler.setDetectingIntervalInSeconds(canal.getCanalParameter().getDetectingIntervalInSeconds());
//                    OtterContextLocator.autowire(handler); // 注入一下spring资源
                    ((AbstractCanalEventSink) eventSink).addHandler(handler, 0); // 添加到开头
                    handler.start();
                }
                return instance;
            }
        });

        canalServer.start();

        canalServer.start(destination);
        this.clientIdentity = new ClientIdentity(destination, pipeline.getParams().getClientId(), filter);
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
        try {
            handler.stop();
        } catch (Exception e) {
            log.warn("failed destory handler", e);
        }

        handler = null;
        canalServer.stop(destination);
        canalServer.stop();
    }


    public Message<EventData> selector() throws InterruptedException {
        int emptyTimes = 0;
        com.alibaba.otter.canal.protocol.Message message = null;
        /**
         * 进行轮询处理
         */
        if (batchTimeout < 0) {
            while (running) {
                message = canalServer.getWithoutAck(clientIdentity, batchSize);
                /**
                 * 代表没数据
                 */
                if (message == null || message.getId() == -1L) {
                    applyWait(emptyTimes++);
                } else {
                    break;
                }
            }
            if (!running) {
                throw new InterruptedException();
            }
        } else {
            /**
             * 进行超时控制
             */
            while (running) {
                message = canalServer.getWithoutAck(clientIdentity, batchSize, batchTimeout, TimeUnit.MILLISECONDS);
                if (message == null || message.getId() == -1L) {
                    // 代表没数据
                    continue;
                } else {
                    break;
                }
            }
            if (!running) {
                throw new InterruptedException();
            }
        }

        List<CanalEntry.Entry> entries = null;
        if (message.isRaw()) {
            entries = new ArrayList<CanalEntry.Entry>(message.getRawEntries().size());
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
        List<EventData> eventDatas = messageParser.parse(pipelineId, entries);
        Message<EventData> result = new Message(message.getId(), eventDatas);
        // 更新一下最后的entry时间，包括被过滤的数据
        if (!CollectionUtils.isEmpty(entries)) {
            long lastEntryTime = entries.get(entries.size() - 1).getHeader().getExecuteTime();
            if (lastEntryTime > 0) {
                // oracle的时间可能为0
                this.lastEntryTime = lastEntryTime;
            }
        }

        if (dump && log.isInfoEnabled()) {
            String startPosition = null;
            String endPosition = null;
            if (!CollectionUtils.isEmpty(entries)) {
                startPosition = buildPositionForDump(entries.get(0));
                endPosition = buildPositionForDump(entries.get(entries.size() - 1));
            }

            // 记录一下，方便追查问题
            dumpMessages(result, startPosition, endPosition, entries.size());
        }
        return result;
    }

    /**
     * 处理无数据的情况，避免空循环挂死
     * @param emptyTimes
     */
    private void applyWait(int emptyTimes) {
        int newEmptyTimes = emptyTimes > maxEmptyTimes ? maxEmptyTimes : emptyTimes;
        if (emptyTimes <= 3) {
            // 3次以内
            Thread.yield();
        } else {
            // 超过3次，最多只sleep 10ms
            LockSupport.parkNanos(1000 * 1000L * newEmptyTimes);
        }
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
    private synchronized void dumpMessages(Message message, String startPosition, String endPosition, int total) {
        try {
            MDC.put(DTSConstants.splitPipelineSelectLogFileKey, String.valueOf(pipelineId));
            log.info(SEP + "****************************************************" + SEP);
            log.info(MessageDumper.dumpMessageInfo(message, startPosition, endPosition, total));
            log.info("****************************************************" + SEP);
            if (dumpDetail) {// 判断一下是否需要打印详细信息
                dumpEventDatas(message.getDatas());
                log.info("****************************************************" + SEP);
            }
        } finally {
            MDC.remove(DTSConstants.splitPipelineSelectLogFileKey);
        }
    }

    /**
     * 分批输出多个数据
     */
    private void dumpEventDatas(List<EventData> eventDatas) {
        int size = eventDatas.size();
        // 开始输出每条记录
        int index = 0;
        do {
            if (index + logSplitSize >= size) {
                log.info(MessageDumper.dumpEventDatas(eventDatas.subList(index, size)));
            } else {
                log.info(MessageDumper.dumpEventDatas(eventDatas.subList(index, index + logSplitSize)));
            }
            index += logSplitSize;
        } while (index < size);
    }
}
