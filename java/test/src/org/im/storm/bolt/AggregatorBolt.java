package org.im.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import org.im.storm.annotation.base.Bolt;
import org.im.storm.annotation.grouping.ShuffleGrouping;
import org.im.storm.annotation.groups.ShuffleGroups;
import org.im.storm.model.bolt.BaseRichBolt;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by ImKAIne on 2015/9/19.
 */
@Bolt("aggregator")
@ShuffleGroups({
    @ShuffleGrouping(value = "sentenceCounter",stream = "sentence"),
    @ShuffleGrouping(value = "wordCounter",stream = "word")
})
public class AggregatorBolt extends BaseRichBolt {
  private int wordCounter;
  private int sentenceCounter;

  @Override
  public void prepare(Map stormConf, TopologyContext context, OutputCollector collector, ApplicationContext ctx) {
  }

  public void execute(Tuple tuple) {
    if (tuple.getSourceStreamId().equals("word")) {
      wordCounter = wordCounter + tuple.getIntegerByField("count");
    }
    if (tuple.getSourceStreamId().equals("sentence")) {
      sentenceCounter = sentenceCounter + tuple.getIntegerByField("count");
    }
    System.out.println("word count:" + wordCounter);
    System.out.println("sentence count:" + sentenceCounter);
  }

  public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

  }
}
