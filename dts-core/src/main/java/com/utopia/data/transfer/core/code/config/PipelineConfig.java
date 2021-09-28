package com.utopia.data.transfer.core.code.config;

import com.utopia.data.transfer.core.code.base.ErrorCode;
import com.utopia.data.transfer.core.code.base.datasource.bean.DataMediaSource;
import com.utopia.data.transfer.core.code.bean.DataMedia;
import com.utopia.data.transfer.core.code.bean.DataMediaPair;
import com.utopia.data.transfer.core.code.bean.Pipeline;
import com.utopia.exception.UtopiaRunTimeException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author owen.cai
 * @create_date 2021/9/15
 * @alter_author
 * @alter_date
 */

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "dts.pipeline")
public class PipelineConfig {

    @Resource
    private DataMediaConfig dataMediaConfig;

    @Resource
    private List<DataMediaSourceConfig> dataMediaSourceConfigList;

    @Getter
    private List<Pipeline> list;

    public void setList (List<Pipeline> list) {
        this.list = list;

        Map<Long, DataMediaSource> dataMediaSourceMap = dataMediaSourceConfigList.stream().flatMap(item -> item.getMapDataMediaSource().entrySet().stream())
                .collect(Collectors.toMap(item -> item.getKey(), item -> item.getValue()));

        Map<Long, DataMedia> dataMediaMap = dataMediaConfig.getList().stream().map(item -> {
            DataMediaSource dataMediaSource = dataMediaSourceMap.get(item.getSource().getId());
            if (Objects.isNull(dataMediaSource)) {
                log.error("no find dataMediaSourceMap {}", item.getSource().getId());
                throw new UtopiaRunTimeException(ErrorCode.INIT_CONFIG_ERROR);
            }
            item.setSource(dataMediaSource);
            return item;
        }).collect(Collectors.toMap(DataMedia::getId, item -> item));

        for (Pipeline pipeline : list) {
            List<DataMediaPair> pairs = pipeline.getPairs();
            for (DataMediaPair pair : pairs) {
                DataMedia dataMedia = dataMediaMap.get(pair.getSource().getId());
                if (Objects.isNull(dataMedia)) {
                    log.error("no find dataMediaMap {}", pair.getSource().getId());
                    throw new UtopiaRunTimeException(ErrorCode.INIT_CONFIG_ERROR);
                }
                pair.setSource(dataMedia);

                dataMedia = dataMediaMap.get(pair.getTarget().getId());
                if (Objects.isNull(dataMedia)) {
                    log.error("no find dataMediaMap {}", pair.getSource().getId());
                    throw new UtopiaRunTimeException(ErrorCode.INIT_CONFIG_ERROR);
                }
                pair.setTarget(dataMedia);
            }
        }
    }
}
