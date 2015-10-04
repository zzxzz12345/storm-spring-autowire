package org.im.storm.annotation.grouping;

import java.lang.annotation.*;

/**
 * Created by HuChong on 2015/10/3.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GlobalGrouping {
  String value();

  String stream() default "";
}
