package org.im.storm.annotation.strategy.impl;

import backtype.storm.topology.InputDeclarer;
import org.im.storm.annotation.grouping.DirectGrouping;
import org.im.storm.annotation.groups.DirectGroups;
import org.im.storm.annotation.strategy.GroupingStrategy;
import org.im.storm.model.proxy.Proxy;
import org.im.storm.model.proxy.impl.ProxyBolt;

/**
 * Created by HuChong on 2015/10/4.
 */
public class DirectGroupingStrategy implements GroupingStrategy {

  public void grouping(InputDeclarer declarer, Proxy proxy) {
    Class<?> aClass = proxy.getInner().getClass();
    DirectGrouping grouping = aClass.getAnnotation(DirectGrouping.class);
    grouping(declarer, grouping);
    DirectGroups groups = aClass.getAnnotation(DirectGroups.class);
    if (groups != null) {
      DirectGrouping[] directGroupings = groups.value();
      for (DirectGrouping directGrouping : directGroupings) {
        grouping(declarer, directGrouping);
      }
    }
  }

  private void grouping(InputDeclarer declarer, DirectGrouping grouping) {
    if (grouping == null) {
      return;
    }
    if ("".equals(grouping.stream())) {
      declarer.directGrouping(grouping.value());
    }else {
      declarer.directGrouping(grouping.value(), grouping.stream());
    }
  }
}
