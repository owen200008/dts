package com.utopia.data.transfer.core.code.service.impl.event;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.utopia.module.distributed.lock.api.DtbLockFactory;
import com.utopia.unique.serviceid.api.UniqueServiceid;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

/**
 * @author owen.cai
 * @create_date 2021/9/28
 * @alter_author
 * @alter_date
 */
@Slf4j
@Service
public class PipelineEventService {

    @Getter
    @Value("${dts.pipeline.event.key.timeout:60}")
    private Integer resourceTimeout = 60;

    @Getter
    @Value("${dts.pipeline.event.process.timeout:10}")
    private Integer processTimeout = 10;

    @Autowired
    private DtbLockFactory redis;

    @Autowired
    private UniqueServiceid uniqueServiceid;

    /**
     *
     */
    private LoadingCache<Long, PipelineEventContext> pipelineEventCache = CacheBuilder.newBuilder().build(new CacheLoader<Long, PipelineEventContext>() {
        @Override
        public PipelineEventContext load(Long key) throws Exception {
            return new PipelineEventContext(redis.createResource(getPipelineEventKey(key), resourceTimeout));
        }
    });

    /**
     * 竞争的key
     * @param pipelineId
     * @return
     */
    protected String getPipelineEventKey(Long pipelineId){
        return String.format("dts:pipeline:event:%d", pipelineId);
    }

    /**
     * 等待资源
     * @param pipelineId
     * @return
     */
    public void waitResource(Long pipelineId) throws Exception {
        PipelineEventContext pipelineEventContext = pipelineEventCache.get(pipelineId);
        pipelineEventContext.waitResource();
    }

    public void releaseResource(Long pipelineId) throws ExecutionException {
        PipelineEventContext pipelineEventContext = pipelineEventCache.get(pipelineId);
        pipelineEventContext.releaseResource();
    }

    public boolean checkResource(Long pipelineId) throws ExecutionException {
        PipelineEventContext pipelineEventContext = pipelineEventCache.get(pipelineId);
        return pipelineEventContext.checkResource();
    }

    public void closePipeline(Long pipelineId) {
        pipelineEventCache.invalidate(pipelineId);
    }



}
