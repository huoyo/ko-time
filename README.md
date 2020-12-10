# koTime

#### 介绍
koTime是一个springboot项目性能分析工具，通过追踪方法调用链路以及对应的运行时长快速定位性能瓶颈：



优点：
> * 无缝集成springboot，使用简单
> * 通过简单配置集成，与业务逻辑无耦合


缺点：
> * 目前仅适用于单机版，不支持分布式和集群


#### 安装教程

1.  引入依赖 或者 下载发行版本
```
 <dependency>
    <groupId>cn.langpy</groupId>
    <artifactId>simsearch</artifactId>
    <version>1.2</version>
  </dependency>
```
2.  配置信息

在application.yml中配置
```
sim-search.dir=xxx  //索引位置，可不填，使用默认位置：当前项目下的indexs目录（第一次运行需手动创建）
sim-search.size.core=10  //创建索引的核心线程数量，根据cpu自行决定，可不填，默认为10
sim-search.size.max=10  //创建索引的最大线程数量，根据cpu自行决定，可不填，默认为200
sim-search.size.queue=1000 //创建索引的线程队列容量，自行决定，可不填，默认为20000
sim-search.index.init=true 重启时是否要对之前的索引进行删除，默认为false
```

#### 使用说明

1.  在需要创建索引的实体上标注需要创建索引的字段
```java
import cn.langpy.simsearch.annotation.IndexColumn;
import cn.langpy.simsearch.annotation.IndexId;

public class Student {
    /*索引唯一id*/
    @IndexId 
    private String id;
    /*需要创建索引的字段*/
    @IndexColumn
    private String studentName;
    @IndexColumn
    private String schoolName;
    private String age;
}
```

2.  在需要创建索引的方法上加上创建索引的注解

```java

```

3.  在需要删除索引的方法上加上删除索引的注解

```java

```

4.  搜索的时候自定义一个空的方法，加上注解即可

```java

```
注意：搜索结果仅仅是搜索出加上@IndexId和@IndexColumn的字段，具体内容自行往业务数据库查询

#### 版本说明

1.接口列表总览

在列表中会显示该接口的运行耗时，如果为绿色则无需优化，如果为红色，需要详细查看问题所在


![输入图片说明](https://images.gitee.com/uploads/images/2020/1209/232411_3e487b47_1625471.png "屏幕截图.png")

2.调用详情

点开接口时，会显示该接口的调用链路以及运行时长

![输入图片说明](https://images.gitee.com/uploads/images/2020/1209/232636_4ec0556c_1625471.png "屏幕截图.png")

#### 版本说明

> V1.0-snapshots：提供基础索引创建、删除和检索功能

> V1.1：增加重启索引初始化功能

> V1.2：搜索时，如果未找到搜索，可走默认模式

#### 问题说明

1.  本项目中使用了aspectjweaver依赖，如果引入的项目中没有该依赖，自行引入
```
 <dependency>
        <groupId>org.aspectj</groupId>
        <artifactId>aspectjweaver</artifactId>
        <version>xxx</version>
  </dependency>
```
