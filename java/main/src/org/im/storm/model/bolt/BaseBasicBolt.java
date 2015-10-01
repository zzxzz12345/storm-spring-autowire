package org.im.storm.model.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import org.im.storm.utils.BeanUtil;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * 自动调用autoware方法，重写原本的prepare方法将会使依赖注入的功能失效
 * Created by ImKAIne on 2015/10/1.
 */
public abstract class BaseBasicBolt extends backtype.storm.topology.base.BaseBasicBolt {
  public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
    BeanUtil.autoware(this);
    prepare(stormConf, context, collector, BeanUtil.getContext());
  }

  public abstract void prepare(Map stormConf, TopologyContext context,
                               OutputCollector collector, ApplicationContext ctx);
}
