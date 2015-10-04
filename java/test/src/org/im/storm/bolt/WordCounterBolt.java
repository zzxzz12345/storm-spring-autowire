package org.im.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import org.im.storm.annotation.base.Bolt;
import org.im.storm.annotation.grouping.ShuffleGrouping;
import org.im.storm.counter.WordCounter;
import org.im.storm.model.bolt.BaseRichBolt;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by ImKAIne on 2015/9/19.
 */
@Bolt("wordCounter")
@ShuffleGrouping("spout")
public class WordCounterBolt extends BaseRichBolt {
  private OutputCollector collector;
  @Resource
  private WordCounter wordCounter;

  @Override
  public void prepare(Map stormConf, TopologyContext context, OutputCollector collector, ApplicationContext ctx) {
    this.collector = collector;
  }

  public void execute(Tuple tuple) {
    String message = tuple.getStringByField("message");
    System.out.println("word:" + message);
    int length = wordCounter.count(message);
    collector.emit("word", new Values(length));
  }

  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declareStream("word", new Fields("count"));
  }
}
