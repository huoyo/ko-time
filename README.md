# koTime

#### 介绍
koTime是一个springboot项目性能分析工具，通过追踪方法调用链路以及对应的运行时长快速定位性能瓶颈：

#### 预览

http://huoyo.gitee.io/ko-time/



优点：
> * 实时监听方法，统计运行时长
> * web展示方法调用链路，瓶颈可视化追踪



缺点：
> * 对项目中每个方法进行监控，在性能层面会有一定的影响，建议在开发阶段使用


#### 使用教程

1.  引入依赖 或者 下载发行版本
```
 <dependency>
    <groupId>cn.langpy</groupId>
    <artifactId>ko-time</artifactId>
    <version>1.6</version>
  </dependency>
```

```
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-${freemarker或者thymeleaf任选一个}</artifactId>
    </dependency>
```
2.  配置信息

在application.yml中配置
```
spring.profiles.active=koTime
koTime.log.enable=false  # 是否开启控制输出，非必填，默认false
koTime.log.language=chinese # 控制台输出语言（english/chinese）非必填，默认chinese
koTime.time.threshold=800.0 # 时间阈值，用于前端展示，大于阈值显示红色，小于阈值显示绿色，非必填，默认800
koTime.pointcut=execution(* com.huoyo..*.*(..)) 需要监测的切面范围，参考aop的@pointcut  v1.4开始加入的功能，用来替代下面的步骤3
koTime.ui.template=thymeleaf  前端页面模板，默认为freemarker，可选thymeleaf 与引入的pom依赖对应
```


3.  新建一个类，实现ComputeTimeHandlerInterface，并在 @Pointcut 写入 需要监测的范围

`注意：v1.3之前需要该步骤，v1.4及以上使用koTime.pointcut配置即可，无需此步骤`

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

4.  启动项目访问 `/koTime` 路径即可

比如：`http://localhost:8080/koTime`
如果项目自定义的contextpath，访问如`http://localhost:8080/xxx服务/koTime`

5.  其他使用

对于单个方法的监测，可以使用@ComputeTime注解

```java
    @ComputeTime
    public void test() {

    }
```

#### 可视化展示

1.接口调用统计

根据颜色判断需要优化的接口数，红色为待优化，绿色为正常

![输入图片说明](https://images.gitee.com/uploads/images/2020/1210/192544_932c9e75_1625471.png "屏幕截图.png")

2.接口列表总览

在列表中会显示该接口的运行耗时，如果为绿色则无需优化，如果为红色，需要详细查看问题所在

![输入图片说明](https://images.gitee.com/uploads/images/2020/1210/192615_192e1123_1625471.png "屏幕截图.png")

3.调用详情

点开接口时，会显示该接口的调用链路以及运行时长

![输入图片说明](https://images.gitee.com/uploads/images/2020/1211/191651_15b5424b_1625471.png "屏幕截图.png")

#### 版本说明

> V1.0：基本功能

> V1.1：接口统计

> V1.2：不可用，错误版本

> V1.3：添加日志、时间阈值可配置

> V1.4：添加koTime.pointcut配置

> V1.5：剔除lombok

> V1.6：兼容thymeleaf

#### 特别说明

1.本项目使用java8开发，其他版本未曾试验，如有什么bug还请告知！

2.v1.5及以下版本默认使用了freemarker模板（其余版本可选用thymeleaf），需要自行引入

```
  <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-freemarker</artifactId>
        <version>xxxx</version>
  </dependency>
```


