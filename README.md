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
    <artifactId>koTime</artifactId>
    <version>1.0</version>
  </dependency>
```
2.  配置信息

在application.yml中配置
```
spring.profiles.active=koTime
```

#### 使用说明

1.  新建一个类，实现ComputeTimeHandlerInterface，并在 @Pointcut 写入 需要监测的范围
```java
@Component
@Aspect
public class RunTimeHandler implements ComputeTimeHandlerInterface {
    @Override
    @Pointcut("execution(* com.huoyo..*.*(..))")
    public void prog() {

    }
}

```

2.  启动项目访问 `/koTime` 路径即可

比如：`http://localhost:8080/koTime`
如果项目自定义的contextpath，访问如`http://localhost:8080/xxx服务/koTime`


#### 版本说明

![输入图片说明](https://images.gitee.com/uploads/images/2020/1210/192544_932c9e75_1625471.png "屏幕截图.png")

1.接口列表总览

在列表中会显示该接口的运行耗时，如果为绿色则无需优化，如果为红色，需要详细查看问题所在

![输入图片说明](https://images.gitee.com/uploads/images/2020/1210/192615_192e1123_1625471.png "屏幕截图.png")

2.调用详情

点开接口时，会显示该接口的调用链路以及运行时长

![输入图片说明](https://images.gitee.com/uploads/images/2020/1210/192639_e49ad1fa_1625471.png "屏幕截图.png")

#### 版本说明

> V1.0：-



#### 问题说明

1.  本项目中使用了aspectjweaver依赖，如果引入的项目中没有该依赖，自行引入
```
 <dependency>
        <groupId>org.aspectj</groupId>
        <artifactId>aspectjweaver</artifactId>
        <version>xxx</version>
  </dependency>
```
