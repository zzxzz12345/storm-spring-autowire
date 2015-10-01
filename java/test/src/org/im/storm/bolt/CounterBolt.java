package org.im.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import org.im.storm.annotation.Bolt;
import org.im.storm.annotation.FromSpout;
import org.im.storm.bean.BaseBean;
import org.im.storm.model.bolt.BaseRichBolt;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by ImKAIne on 2015/9/19.
 */
@Bolt(bolt = "counter")
@FromSpout(spout = "test")
public class CounterBolt extends BaseRichBolt {
  private OutputCollector collector;
  private int count;
  @Resource
  private BaseBean baseBean;

  @Override
  public void prepare(Map stormConf, TopologyContext context, OutputCollector collector, ApplicationContext ctx) {
    this.collector = collector;
  }

  public void execute(Tuple tuple) {
    System.out.println(baseBean.getName());
    System.out.println(tuple.getStringByField("message"));
    count++;
    collector.emit(new Values(count));
  }

  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields("count"));
  }
}
