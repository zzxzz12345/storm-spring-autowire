package org.im.storm.annotation.strategy.impl;

import backtype.storm.topology.InputDeclarer;
import org.im.storm.annotation.grouping.LocalOrShuffleGrouping;
import org.im.storm.annotation.groups.LocalOrShuffleGroups;
import org.im.storm.annotation.strategy.GroupingStrategy;
import org.im.storm.model.proxy.Proxy;
import org.im.storm.model.proxy.impl.ProxyBolt;

/**
 * Created by HuChong on 2015/10/4.
 */
public class LocalOrShuffleGroupingStrategy implements GroupingStrategy {

  public void grouping(InputDeclarer declarer, Proxy proxy) {
    Class<?> aClass = proxy.getInner().getClass();
    LocalOrShuffleGrouping grouping = aClass.getAnnotation(LocalOrShuffleGrouping.class);
    LocalOrShuffleGroups groups = aClass.getAnnotation(LocalOrShuffleGroups.class);
    if (groups != null) {
      LocalOrShuffleGrouping[] localOrShuffleGroupings = groups.value();
      for (LocalOrShuffleGrouping localOrShuffleGrouping : localOrShuffleGroupings) {
        grouping(declarer, localOrShuffleGrouping);
      }
    }
    grouping(declarer, grouping);
  }

  private void grouping(InputDeclarer declarer, LocalOrShuffleGrouping grouping) {
    if (grouping == null) {
      return;
    }
    if ("".equals(grouping.stream())) {
      declarer.localOrShuffleGrouping(grouping.value());
    }else {
      declarer.localOrShuffleGrouping(grouping.value(), grouping.stream());
    }
  }
}
