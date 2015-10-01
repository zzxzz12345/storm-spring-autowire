package org.im.storm.main;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.BoltDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.topology.base.BaseRichSpout;
import org.im.storm.bolt.AggregatorBolt;
import org.im.storm.bolt.CounterBolt;
import org.im.storm.spout.TestSpout;
import org.im.storm.annotation.Bolt;
import org.im.storm.annotation.FromBolt;
import org.im.storm.annotation.FromSpout;
import org.im.storm.annotation.Spout;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by ImKAIne on 2015/9/19.
 */
public class AutoBuild {

  public static void main(String[] args) throws InstantiationException, IllegalAccessException {
    build();
  }

  public static void build() throws IllegalAccessException, InstantiationException {
    TopologyBuilder builder = new TopologyBuilder();
    Set<Class<?>> classes = getClasses("org.im");
    for (Class<?> aClass : classes) {
      Bolt bolt = aClass.getAnnotation(Bolt.class);
      if (bolt != null) {
        FromBolt fromBolt = aClass.getAnnotation(FromBolt.class);
        BoltDeclarer declarer = builder.setBolt(bolt.bolt(), (BaseRichBolt) aClass.newInstance());
        if (fromBolt != null) {
          declarer.allGrouping(fromBolt.bolt());
        }
        FromSpout fromSpout = aClass.getAnnotation(FromSpout.class);
        if (fromSpout != null) {
          declarer.allGrouping(fromSpout.spout());
        }
      }
      Spout spout = aClass.getAnnotation(Spout.class);
      if (spout != null) {
        builder.setSpout(spout.spout(), (BaseRichSpout) aClass.newInstance());
      }
    }
    LocalCluster cluster = new LocalCluster();
    Config config = new Config();
    cluster.submitTopology("test", config, builder.createTopology());
  }

  public static void startTopology() {
    TopologyBuilder builder = new TopologyBuilder();
    TestSpout spout = new TestSpout();
    CounterBolt counterBolt = new CounterBolt();
    AggregatorBolt aggregatorBolt = new AggregatorBolt();
    builder.setSpout("spout", spout);
    builder.setBolt("counter", counterBolt).allGrouping("spout");
    builder.setBolt("aggregator", aggregatorBolt).allGrouping("counter");
    LocalCluster cluster = new LocalCluster();
    Config config = new Config();
    cluster.submitTopology("test", config, builder.createTopology());
  }

  /**
   * 从包package中获取所有的Class
   *
   * @param pack
   * @return
   */
  public static Set<Class<?>> getClasses(String pack) {
    Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
    boolean recursive = true;
    String packageName = pack;
    String packageDirName = packageName.replace('.', '/');
    Enumeration<URL> dirs;
    try {
      dirs = Thread.currentThread().getContextClassLoader().getResources(
          packageDirName);
      while (dirs.hasMoreElements()) {
        URL url = dirs.nextElement();
        String protocol = url.getProtocol();
        if ("file".equals(protocol)) {
          String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
          findAndAddClassesInPackageByFile(packageName, filePath,
              recursive, classes);
        } else if ("jar".equals(protocol)) {
          JarFile jar;
          try {
            jar = ((JarURLConnection) url.openConnection())
                .getJarFile();
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
              JarEntry entry = entries.nextElement();
              String name = entry.getName();
              if (name.charAt(0) == '/') {
                name = name.substring(1);
              }
              if (name.startsWith(packageDirName)) {
                int idx = name.lastIndexOf('/');
                if (idx != -1) {
                  packageName = name.substring(0, idx)
                      .replace('/', '.');
                }
                if ((idx != -1) || recursive) {
                  if (name.endsWith(".class")
                      && !entry.isDirectory()) {
                    String className = name.substring(
                        packageName.length() + 1, name
                            .length() - 6);
                    try {
                      classes.add(Class
                          .forName(packageName + '.'
                              + className));
                    } catch (ClassNotFoundException e) {
                      e.printStackTrace();
                    }
                  }
                }
              }
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return classes;
  }

  /**
   * 以文件的形式来获取包下的所有Class
   *
   * @param packageName
   * @param packagePath
   * @param recursive
   * @param classes
   */
  public static void findAndAddClassesInPackageByFile(String packageName,
                                                      String packagePath,
                                                      final boolean recursive,
                                                      Set<Class<?>> classes) {
    File dir = new File(packagePath);
    if (!dir.exists() || !dir.isDirectory()) {
      return;
    }
    File[] dirfiles = dir.listFiles(new FileFilter() {
      public boolean accept(File file) {
        return (recursive && file.isDirectory())
            || (file.getName().endsWith(".class"));
      }
    });
    for (File file : dirfiles) {
      if (file.isDirectory()) {
        findAndAddClassesInPackageByFile(packageName + "."
                + file.getName(), file.getAbsolutePath(), recursive,
            classes);
      } else {
        String className = file.getName().substring(0,
            file.getName().length() - 6);
        try {
          classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
        } catch (ClassNotFoundException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
