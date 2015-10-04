package org.im.storm.annotation.strategy.impl;

import backtype.storm.topology.InputDeclarer;
import org.im.storm.annotation.grouping.NoneGrouping;
import org.im.storm.annotation.groups.NoneGroups;
import org.im.storm.annotation.strategy.GroupingStrategy;
import org.im.storm.model.proxy.Proxy;
import org.im.storm.model.proxy.impl.ProxyBolt;

/**
 * Created by HuChong on 2015/10/4.
 */
public class NoneGroupingStrategy implements GroupingStrategy {

  public void grouping(InputDeclarer declarer, Proxy proxy) {
    Class<?> aClass = proxy.getInner().getClass();
    NoneGrouping grouping = aClass.getAnnotation(NoneGrouping.class);
    grouping(declarer, grouping);
    NoneGroups groups = aClass.getAnnotation(NoneGroups.class);
    if (groups != null) {
      NoneGrouping[] noneGroupings = groups.value();
      for (NoneGrouping noneGrouping : noneGroupings) {
        grouping(declarer, noneGrouping);
      }
    }
  }

  private void grouping(InputDeclarer declarer, NoneGrouping grouping) {
    if (grouping == null) {
      return;
    }
    if ("".equals(grouping.stream())) {
      declarer.noneGrouping(grouping.value());
    }else {
      declarer.noneGrouping(grouping.value(), grouping.stream());
    }
  }
}
