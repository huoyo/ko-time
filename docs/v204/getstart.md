## 引入依赖

在pom.xml文件中引入


```
 <dependency>
    <groupId>cn.langpy</groupId>
    <artifactId>ko-time</artifactId>
    <version>2.1.1</version>
  </dependency>
    <!--可到右上角选择最新版本，体验新功能-->

```

## 配置

在`application.properties`文件中进行配置

* 必填配置

> 
> ko-time.pointcut=`execution(public * com.huoyo..*.*(..))` # 需要监测的切面范围，参考aop的@pointcut 或者左侧常见问题
>


* 可选配置（以下配置一般不用设置）

> 
> ko-time.enable=true  # 是否开启koTime，默认开启，当为false时，关闭koTime   
> ko-time.log-enable=false  # 是否开启控制输出，默认false  
> ko-time.log-language=chinese # 控制台输出语言（english/chinese）默认chinese  
> ko-time.threshold=800.0 # 时间阈值，用于前端展示，大于阈值显示红色，小于阈值显示绿色，默认800  
> ko-time.context-path=http://localhost:80 # 前端页面调用接口的上下文环境，无法自动获取时可手动配置，一般情况切记不要配置   v2.0.1开始支持  
> ko-time.exception-enable=true # 是否开启异常检测，默认为false,开启后会对方法内部抛出的异常进行统计 v2.0.0开始支持  
> ko-time.auth-enable=true # 是否开启认证，默认为false,开启后需要登录才能访问调用链路 v2.0.2开始支持  
> ko-time.user-name=xxxx # 登录用户 v2.0.2开始支持  
> ko-time.password=xxxx # 登录密码 v2.0.2开始支持  
> ko-time.param-analyse=true #是否开启入参组合分析 默认开启 v2.0.8开始支持  双击方法节点即可看到效果

## 访问

> 注意：    
> 1.引入了上面的依赖和配置以后，确认项目中是否有aop相关的包，koTime使用了@Aspect注解，未引入的自行引入，如aspectj或者spring-boot-starter-aop        
> 2.做完前面的步骤，koTime的集成已经完毕，无需进行其他配置   
> 3.如果后台有权限认证，需要放开`/koTime`和`/koTime/**`
                                   


* 启动项目访问 `/koTime` 路径即可

* 如果仅仅只是想统计某个方法，在方法上加上`@ComputeTime`即可，控制台会输出耗时


> 建议使用谷歌浏览器或者Edge浏览器，IE是不可能支持的

如果项目自定义的contextpath，访问如`http://localhost:8080/xxx服务/koTime`

如：application.properties中定义了 `server.servlet.context-path=/myservice`，那么访问路径为`http://localhost:8080/myservice/koTime`

如果页面能正常显示，但是无法获取方法链路，可配置`ko-time.context-path=http://localhost:8080/myservice`


---


**为了让作者不要偷懒，督促他好好维护和开发，我准备用金钱对他进行鞭笞**

<img src="v202/pay.jpg"  width="15%" height="15%">


