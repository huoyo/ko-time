
## 引入依赖

在pom.xml文件中引入

```
 <dependency>
    <groupId>cn.langpy</groupId>
    <artifactId>ko-time</artifactId>
    <version>1.8</version>
  </dependency>
  
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-${freemarker或者thymeleaf任选一个}</artifactId>
</dependency>
```

## 配置

在application.properties文件中进行配置

* 必填配置

```
spring.profiles.active=koTime # 如果项目中还有其他配置请用英文逗号分隔，如：koTime,dev 
koTime.pointcut=execution(* com.huoyo..*(..)) 需要监测的切面范围，参考aop的@pointcut  v1.4开始加入的功能
```

* 可选配置

```
koTime.log.enable=false  # 是否开启控制输出，默认false
koTime.log.language=chinese # 控制台输出语言（english/chinese）默认chinese
koTime.time.threshold=800.0 # 时间阈值，用于前端展示，大于阈值显示红色，小于阈值显示绿色，默认800
koTime.ui.template=thymeleaf # 前端页面模板，默认为freemarker，可选thymeleaf 与引入的pom依赖对应
koTime.exception.enable=true # 是否开启异常检测，默认为false,开启后会对方法内部抛出的异常进行统计 v1.9开始支持
```

## 访问

* 启动项目访问 `/koTime` 路径即可


如果项目自定义的contextpath，访问如`http://localhost:8080/xxx服务/koTime`

如：application.properties中定义了 `server.servlet.context-path=myservice`，那么访问路径为`http://localhost:8080/myservice/koTime`

