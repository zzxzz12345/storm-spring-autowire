package org.im.storm.model.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import org.im.storm.utils.BeanUtil;

import java.util.Map;

/**
 * IRichBolt的代理类，用于实现在调用prepare方法时的延时注入
 * Created by HuChong on 2015/10/3.
 */
public class ProxyBolt implements IRichBolt {
  private IRichBolt bolt;
  private boolean isAutowired;

  public ProxyBolt(IRichBolt bolt) {
    this(bolt, true);
  }

  public ProxyBolt(IRichBolt bolt, boolean isAutowired) {
    this.bolt = bolt;
    this.isAutowired = isAutowired;
  }

  public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
    if (isAutowired) {
      BeanUtil.autoware(this);
    }
    bolt.prepare(stormConf, context, collector);
  }

  public void execute(Tuple input) {
    bolt.execute(input);
  }

  public void cleanup() {
    bolt.cleanup();
  }

  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    bolt.declareOutputFields(declarer);
  }

  public Map<String, Object> getComponentConfiguration() {
    return bolt.getComponentConfiguration();
  }
}
