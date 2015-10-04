package org.im.storm.annotation.strategy.impl;

import backtype.storm.topology.InputDeclarer;
import org.im.storm.annotation.grouping.GlobalGrouping;
import org.im.storm.annotation.groups.GlobalGroups;
import org.im.storm.annotation.strategy.GroupingStrategy;
import org.im.storm.model.proxy.Proxy;
import org.im.storm.model.proxy.impl.ProxyBolt;

/**
 * Created by HuChong on 2015/10/4.
 */
public class GlobalGroupingStrategy implements GroupingStrategy {

  public void grouping(InputDeclarer declarer, Proxy proxy) {
    Class<?> aClass = proxy.getInner().getClass();
    GlobalGrouping grouping = aClass.getAnnotation(GlobalGrouping.class);
    grouping(declarer, grouping);
    GlobalGroups groups = aClass.getAnnotation(GlobalGroups.class);
    if (groups != null) {
      GlobalGrouping[] globalGroupings = groups.value();
      for (GlobalGrouping globalGrouping : globalGroupings) {
        grouping(declarer, globalGrouping);
      }
    }
  }

  private void grouping(InputDeclarer declarer, GlobalGrouping grouping) {
    if (grouping == null) {
      return;
    }
    if ("".equals(grouping.stream())) {
      declarer.globalGrouping(grouping.value());
    }else {
      declarer.globalGrouping(grouping.value(), grouping.stream());
    }
  }
}
