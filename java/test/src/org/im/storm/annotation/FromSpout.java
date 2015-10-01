package org.im.storm.annotation;

import java.lang.annotation.*;

/**
 * Created by ImKAIne on 2015/9/19.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FromSpout {
    String spout();

    String[] spouts() default {};

    String[] grouping() default {};

    int[] taskNum() default {};

    int[] parallelism() default {};
}
