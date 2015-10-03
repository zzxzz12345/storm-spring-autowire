package org.im.storm.model.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import org.im.storm.utils.BeanUtil;
import org.springframework.context.annotation.Bean;

import java.util.Map;

/**
 * Created by HuChong on 2015/10/3.
 */
public class ProxySpout implements IRichSpout {
  private IRichSpout spout;
  private boolean isAutowired;

  public ProxySpout(IRichSpout spout) {
    this(spout, true);
  }

  public ProxySpout(IRichSpout spout, boolean isAutowired) {
    this.spout = spout;
    this.isAutowired = isAutowired;
  }

  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    spout.declareOutputFields(declarer);
  }

  public Map<String, Object> getComponentConfiguration() {
    return spout.getComponentConfiguration();
  }

  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
    if (isAutowired) {
      BeanUtil.autoware(this);
    }
    spout.open(conf, context, collector);
  }

  public void close() {
    spout.close();
  }

  public void activate() {
    spout.activate();
  }

  public void deactivate() {
    spout.deactivate();
  }

  public void nextTuple() {
    spout.nextTuple();
  }

  public void ack(Object msgId) {
    spout.ack(msgId);
  }

  public void fail(Object msgId) {
    spout.fail(msgId);
  }
}
