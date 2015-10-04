package org.im.storm.annotation.strategy;

import org.im.storm.annotation.grouping.AllGrouping;
import org.im.storm.annotation.strategy.impl.*;

/**
 * Created by HuChong on 2015/10/4.
 */
public enum Strategies {
  ALL(new AllGroupingStrategy()),
  DIRECT(new DirectGroupingStrategy()),
  FIELDS(new FieldsGroupingStrategy()),
  GLOBAL(new GlobalGroupingStrategy()),
  LOCAL_OR_SHUFFLE(new LocalOrShuffleGroupingStrategy()),
  NONE(new NoneGroupingStrategy()),
  PARTIAL_KEY(new PartialKeyGroupingStrategy()),
  SHUFFLE(new ShuffleGroupingStrategy());


  private GroupingStrategy strategy;


  Strategies(GroupingStrategy strategy) {
    this.strategy = strategy;
  }

  public GroupingStrategy getStrategy() {
    return strategy;
  }
}
