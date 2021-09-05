
## 引入依赖

在pom.xml文件中引入

```
 <dependency>
    <groupId>cn.langpy</groupId>
    <artifactId>ko-time</artifactId>
    <version>2.0.0</version>
  </dependency>
  
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-${freemarker或者thymeleaf任选一个}</artifactId>
</dependency>
```

## 配置

在`application.properties`文件中进行配置

* 必填配置

> 
> spring.profiles.active=koTime # 如果项目中还有其他配置请用英文逗号分隔，如：koTime,dev     
> koTime.pointcut=execution(public * com.huoyo..*(..)) # 需要监测的切面范围，参考aop的@pointcut 或者左侧常见问题  v1.4开始加入的功能


* 可选配置

> 
> koTime.enable=true  # 是否开启koTime，默认开启，当为false时，关闭koTime   
> koTime.log.enable=false  # 是否开启控制输出，默认false  
> koTime.log.language=chinese # 控制台输出语言（english/chinese）默认chinese  
> koTime.time.threshold=800.0 # 时间阈值，用于前端展示，大于阈值显示红色，小于阈值显示绿色，默认800  
> koTime.ui.template=freemarker # 前端页面模板，默认为freemarker，可选thymeleaf 与引入的pom依赖对应  
> koTime.exception.enable=true # 是否开启异常检测，默认为false,开启后会对方法内部抛出的异常进行统计 v2.0.0开始支持  


## 访问

> 注意：    
> 1.引入了上面的依赖和配置以后，确认项目中是否有aop相关的包，koTime使用了@Aspect注解，未引入的自行引入，如aspectj或者spring-boot-starter-aop        
> 2.做完前面的步骤，koTime的集成已经完毕，无需进行其他配置   
                                   


* 启动项目访问 `/koTime` 路径即可

> 建议使用谷歌浏览器或者Edge浏览器，IE是不可能支持

如果项目自定义的contextpath，访问如`http://localhost:8080/xxx服务/koTime`

如：application.properties中定义了 `server.servlet.context-path=myservice`，那么访问路径为`http://localhost:8080/myservice/koTime`


---

**为了让作者不要偷懒，督促他好好维护和开发，我准备用金钱对他进行鞭笞**

<img src="v200/pay.jpg"  width="15%" height="15%">


