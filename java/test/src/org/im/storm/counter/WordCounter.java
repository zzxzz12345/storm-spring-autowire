package org.im.storm.counter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by HuChong on 2015/10/4.
 */
@Component
@Scope("prototype")
public class WordCounter {
  public int count(String string) {
    return string.split("\\s").length;
  }
}
