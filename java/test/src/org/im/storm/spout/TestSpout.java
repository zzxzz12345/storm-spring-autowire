package org.im.storm.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import org.im.storm.annotation.base.Spout;
import org.im.storm.model.spout.BaseRichSpout;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Created by ImKAIne on 2015/9/19.
 */
@Spout("spout")
public class TestSpout extends BaseRichSpout {
  private SpoutOutputCollector collector;

  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields("message"));
  }

  @Override
  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector, ApplicationContext ctx) {
    this.collector = collector;
  }

  public void nextTuple() {
    collector.emit(new Values("this sentence contains 5 words"));
    Utils.sleep(1000);
  }
}
