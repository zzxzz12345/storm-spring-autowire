package org.im.storm.build;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.*;
import org.im.storm.annotation.base.Bolt;
import org.im.storm.annotation.base.Spout;
import org.im.storm.annotation.strategy.GroupingStrategy;
import org.im.storm.annotation.strategy.Strategies;
import org.im.storm.model.proxy.Proxy;
import org.im.storm.model.proxy.impl.ProxyBolt;
import org.im.storm.model.proxy.impl.ProxySpout;
import org.im.storm.utils.PackageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by ImKAIne on 2015/9/19.
 */
public class AutoBuild {

  private List<GroupingStrategy> strategies = new ArrayList<GroupingStrategy>();
  private TopologyBuilder builder;

  public static void main(String[] args) throws InstantiationException, IllegalAccessException {
    TopologyBuilder builder = new TopologyBuilder();
    AutoBuild autoBuild = new AutoBuild(builder);
    autoBuild.build();
    LocalCluster cluster = new LocalCluster();
    Config config = new Config();
    cluster.submitTopology("test", config, builder.createTopology());
  }

  public AutoBuild(TopologyBuilder builder) {
    this.builder = builder;
    Strategies[] values = Strategies.values();
    for (Strategies value : values) {
      strategies.add(value.getStrategy());
    }
  }

  public void registryStrategy(GroupingStrategy strategy) {
    strategies.add(strategy);
  }

  public void build() throws IllegalAccessException, InstantiationException {
    Set<Class<?>> classes = PackageUtil.getClasses("org.im");
    for (Class<?> aClass : classes) {
      Spout spout = aClass.getAnnotation(Spout.class);
      if (spout != null) {
        ProxySpout proxySpout = new ProxySpout((IRichSpout) aClass.newInstance());
        SpoutDeclarer spoutDeclarer = builder.setSpout(spout.value(), proxySpout, spout.parallelism())
            .setNumTasks(spout.tasks());
        if (spout.maxSpoutPending() > 0) {
          spoutDeclarer.setMaxSpoutPending(spout.maxSpoutPending());
        }
      }

      Bolt bolt = aClass.getAnnotation(Bolt.class);
      if (bolt != null) {
        ProxyBolt proxyBolt = new ProxyBolt((IRichBolt) aClass.newInstance());
        InputDeclarer declarer = builder.setBolt(bolt.value(),
            proxyBolt, bolt.parallelism()).setNumTasks(bolt.tasks());
        invokeStrategy(declarer, proxyBolt);
      }
    }
  }

  private void invokeStrategy(InputDeclarer declarer, Proxy proxy) {
    for (GroupingStrategy strategy : strategies) {
      strategy.grouping(declarer, proxy);
    }
  }

}
