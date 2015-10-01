package org.im.storm.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.Resource;
import java.lang.reflect.Field;

/**
 * Created by ImKAIne on 2015/9/23.
 */
public class BeanUtil {
  private static ApplicationContext context;

  static {
    context = new ClassPathXmlApplicationContext("applicationContext.xml");
  }

  public static <T> T getBean(String name, Class<T> clazz) {
    return context.getBean(name, clazz);
  }

  public static Object getBean(String name) {
    return context.getBean(name);
  }

  public static <T> T getBean(Class<T> clazz) {
    return context.getBean(clazz);
  }

  public static ApplicationContext getContext() {
    return context;
  }

  /**
   * 扫描obj下的@Resource注解和@Autowired注解进行注入
   * @param obj
   */
  public static void autoware(Object obj) {
    Field[] fields = obj.getClass().getDeclaredFields();
    for (Field field : fields) {
      Resource resource = field.getAnnotation(Resource.class);
      Autowired autowired = field.getAnnotation(Autowired.class);
      if (resource != null) {
        Object bean;
        if (resource.name() != null && !resource.name().equals("")) {
          bean = context.getBean(resource.name());
        } else if (context.containsBean(field.getName())) {
          bean = context.getBean(field.getName());
        } else {
          bean = context.getBean(field.getClass());
        }
        field.setAccessible(true);
        try {
          field.set(obj, bean);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }

      if (autowired != null) {
        Object bean;
        field.setAccessible(true);
        Qualifier qualifier = field.getAnnotation(Qualifier.class);
        if (qualifier != null && qualifier.value() != null && !qualifier.value().equals("")) {
          bean = context.getBean(qualifier.value());
        } else {
          bean = context.getBean(field.getClass());
        }
        try {
          field.set(obj, bean);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
}
