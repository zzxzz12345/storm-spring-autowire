package org.im.storm.annotation.groups;

import org.im.storm.annotation.grouping.ShuffleGrouping;

import java.lang.annotation.*;

/**
 * Created by HuChong on 2015/10/3.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ShuffleGroups {
  ShuffleGrouping[] value();
}
