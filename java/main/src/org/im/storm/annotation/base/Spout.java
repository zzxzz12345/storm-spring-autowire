package org.im.storm.annotation.base;

import java.lang.annotation.*;

/**
 * Created by ImKAIne on 2015/9/19.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Spout {
  String value();

  int tasks() default 1;

  int parallelism() default 1;

  int maxSpoutPending() default -1;
}
