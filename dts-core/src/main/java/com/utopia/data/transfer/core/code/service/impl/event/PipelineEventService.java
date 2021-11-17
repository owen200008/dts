package com.utopia.data.transfer.core.code.service.impl.event;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.utopia.data.transfer.core.base.config.ConfigService;
import com.utopia.module.distributed.lock.api.DtbLockFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ConfigService configService;

    @Autowired
    private DtbLockFactory redis;

    /**
     *
     */
    private LoadingCache<Long, PipelineEventContext> pipelineEventSelectCache = CacheBuilder.newBuilder().build(new CacheLoader<Long, PipelineEventContext>() {
        @Override
        public PipelineEventContext load(Long key) throws Exception {
            return new PipelineEventContext(redis.createResource(getPipelineEventSelectKey(key),
                    configService.getPipeline(key).getParams().getResourceTimeout()));
        }
    });

    private LoadingCache<Long, PipelineEventContext> pipelineEventLoadCache = CacheBuilder.newBuilder().build(new CacheLoader<Long, PipelineEventContext>() {
        @Override
        public PipelineEventContext load(Long key) throws Exception {
            return new PipelineEventContext(redis.createResource(getPipelineEventLoadKey(key),
                    configService.getPipeline(key).getParams().getResourceTimeout()));
        }
    });

    /**
     * 竞争的key
     * @param pipelineId
     * @return
     */
    protected String getPipelineEventSelectKey(Long pipelineId){
        return String.format("dts:pipeline:event:select:%d", pipelineId);
    }

    protected String getPipelineEventLoadKey(Long pipelineId){
        return String.format("dts:pipeline:event:load:%d", pipelineId);
    }

    /**
     * 等待资源
     * @param pipelineId
     * @return
     */
    public void waitSelectResource(Long pipelineId) throws Exception {
        PipelineEventContext pipelineEventContext = pipelineEventSelectCache.get(pipelineId);
        pipelineEventContext.waitResource();
    }

    public void releaseSelectResource(Long pipelineId) throws ExecutionException {
        PipelineEventContext pipelineEventContext = pipelineEventSelectCache.get(pipelineId);
        pipelineEventContext.releaseResource();
    }

    public boolean checkSelectResource(Long pipelineId) throws ExecutionException {
        PipelineEventContext pipelineEventContext = pipelineEventSelectCache.get(pipelineId);
        return pipelineEventContext.checkResource();
    }

    /**
     * 等待资源
     * @param pipelineId
     * @return
     */
    public void waitLoadResource(Long pipelineId) throws Exception {
        PipelineEventContext pipelineEventContext = pipelineEventLoadCache.get(pipelineId);
        pipelineEventContext.waitResource();
    }

    public void releaseLoadResource(Long pipelineId) throws ExecutionException {
        PipelineEventContext pipelineEventContext = pipelineEventLoadCache.get(pipelineId);
        pipelineEventContext.releaseResource();
    }

    public boolean checkLoadResource(Long pipelineId) throws ExecutionException {
        PipelineEventContext pipelineEventContext = pipelineEventLoadCache.get(pipelineId);
        return pipelineEventContext.checkResource();
    }

    public void closePipeline(Long pipelineId) {
        pipelineEventSelectCache.invalidate(pipelineId);
        pipelineEventLoadCache.invalidate(pipelineId);
    }
}
