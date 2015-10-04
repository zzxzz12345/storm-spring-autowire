package org.im.storm.annotation.strategy.impl;

import backtype.storm.topology.InputDeclarer;
import org.im.storm.annotation.grouping.ShuffleGrouping;
import org.im.storm.annotation.groups.ShuffleGroups;
import org.im.storm.annotation.strategy.GroupingStrategy;
import org.im.storm.model.proxy.Proxy;
import org.im.storm.model.proxy.impl.ProxyBolt;

/**
 * Created by HuChong on 2015/10/4.
 */
public class ShuffleGroupingStrategy implements GroupingStrategy {

  public void grouping(InputDeclarer declarer, Proxy proxy) {
    Class<?> aClass = proxy.getInner().getClass();
    ShuffleGrouping grouping = aClass.getAnnotation(ShuffleGrouping.class);
    grouping(declarer, grouping);
    ShuffleGroups groups = aClass.getAnnotation(ShuffleGroups.class);
    if (groups != null) {
      ShuffleGrouping[] shuffleGroupings = groups.value();
      for (ShuffleGrouping shuffleGrouping : shuffleGroupings) {
        grouping(declarer, shuffleGrouping);
      }
    }
  }

  private InputDeclarer grouping(InputDeclarer declarer, ShuffleGrouping grouping) {
    if (grouping == null) {
      return declarer;
    }
    InputDeclarer result;
    if ("".equals(grouping.stream())) {
      result = declarer.allGrouping(grouping.value());
    } else {
      result = declarer.allGrouping(grouping.value(), grouping.stream());
    }
    return result;
  }
}
