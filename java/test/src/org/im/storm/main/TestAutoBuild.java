package org.im.storm.main;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.*;
import org.im.storm.annotation.strategy.GroupingStrategy;
import org.im.storm.build.AutoBuild;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ImKAIne on 2015/9/19.
 */
public class TestAutoBuild {
  public static void main(String[] args) throws InstantiationException, IllegalAccessException {
    TopologyBuilder builder = new TopologyBuilder();
    AutoBuild autoBuild = new AutoBuild(builder);
    autoBuild.build();
    LocalCluster cluster = new LocalCluster();
    Config config = new Config();
    cluster.submitTopology("test", config, builder.createTopology());
  }
}
