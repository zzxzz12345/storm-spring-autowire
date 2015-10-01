package org.im.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import org.im.storm.annotation.Bolt;
import org.im.storm.annotation.FromBolt;
import org.im.storm.model.bolt.BaseRichBolt;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Created by ImKAIne on 2015/9/19.
 */
@Bolt(bolt = "aggregator")
@FromBolt(bolt = "counter")
public class AggregatorBolt extends BaseRichBolt {
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector, ApplicationContext ctx) {
    }

    public void execute(Tuple tuple) {
        System.out.println(tuple.getIntegerByField("count"));
    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
