# storm-spring-autowire
使storm支持spring的注入功能<br/>
由于storm的生命周期如下<br/>
1.在提交了一个topology之后(是在nimbus所在的机器么?), 创建spout/bolt实例(spout/bolt在storm中统称为component)并进行序列化.<br/>
2.将序列化的component发送给所有的任务所在的机器<br/>
3.在每一个任务上反序列化component. <br/>
4.在开始执行任务之前, 先执行component的初始化方法(bolt是prepare, spout是open). <br/>
这个项目采取了了在prepare方法中执行注入的方式使bolt在序列化完成后执行注入，绕开了storm的序列化机制<br/>
使用这个项目里 你可以通过以下代码做到最基本的注入
```Java
@Resource
private SentenceCounter counter;

public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
  BeanUtil.autoware(this);
}
```
目前提供了两个封装好的bolt以供使用
org.im.storm.model.bolt.BaseBasicBolt
org.im.storm.model.bolt.BaseRichBolt
或者你可以通过代理的方式进行注入
```Java
ProxyBolt proxyBolt = new ProxyBolt(new AggregatorBolt());
```
另外这个项目还提供了一套注解用于自动构建拓扑，在构建过程中将会自动使用代理类进行构建
例如
```Java
@Bolt("aggregator")
@ShuffleGroups({
    @ShuffleGrouping(value = "sentenceCounter",stream = "sentence"),
    @ShuffleGrouping(value = "wordCounter",stream = "word")
})
public class AggregatorBolt extends BaseRichBolt {...}

@Bolt("sentenceCounter")
@ShuffleGrouping("spout")
public class SentenceCounterBolt extends BaseRichBolt {...}

@Bolt("wordCounter")
@ShuffleGrouping("spout")
public class WordCounterBolt extends BaseRichBolt {...}

@Spout("spout")
public class TestSpout extends BaseRichSpout {...}

public class TestAutoBuild {
  public static void main(String[] args) throws InstantiationException, IllegalAccessException {
    TopologyBuilder builder = new TopologyBuilder();
    AutoBuild autoBuild = new AutoBuild(builder);
    autoBuild.build();
    LocalCluster cluster = new LocalCluster();
    Config config = new Config();
    cluster.submitTopology("test", config, builder.createTopology());
  }
}

```
这样就能通过一种可读性非常强的的方式构建出一个拓扑
此外由于在构建过程中自动使用了代理，你可以用@Resource和@Autowired这两个注解进行注入
目前会自动从classpath下的applicationContext.xml中进行读取
