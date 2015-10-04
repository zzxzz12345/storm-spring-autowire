package org.im.storm.annotation.strategy.impl;

import backtype.storm.topology.*;
import org.im.storm.annotation.grouping.AllGrouping;
import org.im.storm.annotation.groups.AllGroups;
import org.im.storm.annotation.strategy.GroupingStrategy;
import org.im.storm.model.proxy.Proxy;
import org.im.storm.model.proxy.impl.ProxyBolt;

/**
 * Created by HuChong on 2015/10/4.
 */
public class AllGroupingStrategy implements GroupingStrategy {

  public void grouping(InputDeclarer declarer, Proxy proxy) {
    AllGrouping grouping = proxy.getInner().getClass().getAnnotation(AllGrouping.class);
    grouping(declarer, grouping);
    AllGroups groups = proxy.getInner().getClass().getAnnotation(AllGroups.class);
    if (groups != null) {
      AllGrouping[] allGroupings = groups.value();
      for (AllGrouping allGrouping : allGroupings) {
        grouping(declarer, allGrouping);
      }
    }
  }

  private void grouping(InputDeclarer declarer, AllGrouping grouping) {
    if (grouping == null) {
      return;
    }
    if ("".equals(grouping.stream())) {
      declarer.allGrouping(grouping.value());
    }else {
      declarer.allGrouping(grouping.value(), grouping.stream());
    }
  }
}
