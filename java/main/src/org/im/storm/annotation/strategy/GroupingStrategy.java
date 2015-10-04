package org.im.storm.annotation.strategy;

import backtype.storm.topology.*;
import org.im.storm.model.proxy.Proxy;
import org.im.storm.model.proxy.impl.ProxyBolt;

/**
 * Created by HuChong on 2015/10/4.
 */
public interface GroupingStrategy {

  void grouping(InputDeclarer declarer, Proxy proxy);
}
