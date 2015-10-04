package org.im.storm.annotation.strategy.impl;

import backtype.storm.topology.InputDeclarer;
import backtype.storm.tuple.Fields;
import org.im.storm.annotation.grouping.FieldsGrouping;
import org.im.storm.annotation.groups.FieldsGroups;
import org.im.storm.annotation.strategy.GroupingStrategy;
import org.im.storm.model.proxy.Proxy;
import org.im.storm.model.proxy.impl.ProxyBolt;

/**
 * Created by HuChong on 2015/10/4.
 */
public class FieldsGroupingStrategy implements GroupingStrategy {

  public void grouping(InputDeclarer declarer, Proxy proxy) {
    Class<?> aClass = proxy.getInner().getClass();
    FieldsGrouping grouping = aClass.getAnnotation(FieldsGrouping.class);
    grouping(declarer, grouping);
    FieldsGroups groups = aClass.getAnnotation(FieldsGroups.class);
    if (groups != null) {
      FieldsGrouping[] fieldsGroupings = groups.value();
      for (FieldsGrouping fieldsGrouping : fieldsGroupings) {
        grouping(declarer, fieldsGrouping);
      }
    }
  }

  private void grouping(InputDeclarer declarer, FieldsGrouping grouping) {
    if (grouping == null) {
      return;
    }
    if ("".equals(grouping.stream())) {
      declarer.fieldsGrouping(grouping.value(),new Fields(grouping.fields()));
    }else {
      declarer.fieldsGrouping(grouping.value(), grouping.stream(), new Fields(grouping.fields()));
    }
  }
}
