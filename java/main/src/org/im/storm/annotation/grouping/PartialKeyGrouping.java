package org.im.storm.annotation.grouping;

import java.lang.annotation.*;

/**
 * Created by HuChong on 2015/10/4.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PartialKeyGrouping {
  String value();

  String fields();

  String stream() default "";
}
