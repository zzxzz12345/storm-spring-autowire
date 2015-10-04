package org.im.storm.annotation.strategy.impl;

import backtype.storm.topology.InputDeclarer;
import backtype.storm.tuple.Fields;
import org.im.storm.annotation.grouping.PartialKeyGrouping;
import org.im.storm.annotation.grouping.ShuffleGrouping;
import org.im.storm.annotation.groups.PartialKeyGroups;
import org.im.storm.annotation.strategy.GroupingStrategy;
import org.im.storm.model.proxy.Proxy;
import org.im.storm.model.proxy.impl.ProxyBolt;

/**
 * Created by HuChong on 2015/10/4.
 */
public class PartialKeyGroupingStrategy implements GroupingStrategy {

  public void grouping(InputDeclarer declarer, Proxy proxy) {
    Class<?> aClass = proxy.getInner().getClass();
    PartialKeyGrouping grouping = aClass.getAnnotation(PartialKeyGrouping.class);
    grouping(declarer, grouping);
    PartialKeyGroups groups = aClass.getAnnotation(PartialKeyGroups.class);
    if (groups != null) {
      PartialKeyGrouping[] groupings = groups.value();
      for (PartialKeyGrouping partialKeyGrouping : groupings) {
        grouping(declarer, partialKeyGrouping);
      }
    }
  }

  private void grouping(InputDeclarer declarer, PartialKeyGrouping grouping) {
    if (grouping == null) {
      return;
    }
    if ("".equals(grouping.stream())) {
      declarer.partialKeyGrouping(grouping.value(), new Fields(grouping.fields()));
    } else {
      declarer.partialKeyGrouping(grouping.value(), new Fields(grouping.fields()));
    }
  }
}
