package org.im.storm.annotation;

import java.lang.annotation.*;

/**
 * Created by ImKAIne on 2015/9/19.
 */
@Target({ElementType.TYPE, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FromBolt {
  String bolt();

  String[] bolts() default {};

  Class[] boltClasses() default {};

  String[] grouping() default {};

  int[] taskNum() default {};

  int[] parallelism() default {};
}
