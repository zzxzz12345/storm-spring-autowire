package org.im.storm.model.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import org.im.storm.utils.BeanUtil;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * 自动调用autoware方法，重写原本的open方法将会使依赖注入的功能失效
 * Created by ImKAIne on 2015/10/1.
 */
public abstract class BaseRichSpout extends backtype.storm.topology.base.BaseRichSpout {
  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
    BeanUtil.autoware(this);
    open(conf, context, collector, BeanUtil.getContext());
  }

  public abstract void open(Map conf, TopologyContext context, SpoutOutputCollector collector, ApplicationContext ctx);
}
