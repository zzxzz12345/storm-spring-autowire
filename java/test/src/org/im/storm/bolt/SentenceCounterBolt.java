package org.im.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import org.im.storm.annotation.base.Bolt;
import org.im.storm.annotation.grouping.ShuffleGrouping;
import org.im.storm.counter.SentenceCounter;
import org.im.storm.model.bolt.BaseRichBolt;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by ImKAIne on 2015/9/19.
 */
@Bolt("sentenceCounter")
@ShuffleGrouping("spout")
public class SentenceCounterBolt extends BaseRichBolt {
  private OutputCollector collector;
  @Resource
  private SentenceCounter counter;

  @Override
  public void prepare(Map stormConf, TopologyContext context, OutputCollector collector, ApplicationContext ctx) {
    this.collector = collector;
  }

  public void execute(Tuple tuple) {
    String message = tuple.getStringByField("message");
    System.out.println("sentence:" + message);
    collector.emit("sentence", new Values(counter.count(message)));
  }

  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declareStream("sentence", new Fields("count"));
  }
}
