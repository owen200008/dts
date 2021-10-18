package com.utopia.data.transfer.web.archetype.config.filter.dubbo;

import static org.apache.dubbo.common.constants.CommonConstants.PROVIDER;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;

@Slf4j
@Activate(order = 10000, group = PROVIDER)
public class PrometheusFilter implements Filter {

  public PrometheusFilter() {
  }

  @Override
  public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
    if (!invocation.getMethodName().startsWith("$")) {
      return Metrics.timer("dubbo_server_requests", getTags(invoker, invocation))
              .record(() -> invoker.invoke(invocation));
    } else {
      return invoker.invoke(invocation);
    }
  }

  public Iterable<Tag> getTags(Invoker<?> invoker, Invocation invocation) {
    return Tags.of(Tag.of("interface", invoker.getInterface().getName()),
            Tag.of("method", invocation.getMethodName()),
            Tag.of("group", (String)invocation.getObjectAttachments().get("group")));
  }
}