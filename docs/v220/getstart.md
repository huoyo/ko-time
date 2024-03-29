## 引入依赖

在pom.xml文件中引入


```
 <dependency>
    <groupId>cn.langpy</groupId>
    <artifactId>ko-time</artifactId>
    <version>2.3.9</version>
  </dependency>
  <!--阿里云maven仓库更新会慢一两天左右，拉取失败的切换到maven中央仓库-->
```

> [试用专业版](http://www.kotime.cn/docs/kaiyuan#/v220/pro)，体验更加完备的功能：计算分离、接口访问量、超时率等

> [试用本地化插件](http://www.kotime.cn/docs/kaiyuan#/v220/plugin)，体验Idea内一键代码热更新、方法耗时来源分析、maven依赖版本检查等

## 配置

在`application.properties`文件中进行配置

* 必填配置

>
> ko-time.pointcut=`execution(public * com.huoyo..*.*(..))`  #需要监测的范围，是链路包含的范围，不仅仅是接口层，参考aop的@pointcut 或者左侧`常见问题`，另外需要排除final相关类
>


* 可选配置（以下配置一般不用设置）

```
ko-time.enable=true  # 是否开启koTime，默认开启，当为false时，关闭koTime   
ko-time.log-enable=false  # 是否开启控制输出，默认false  
ko-time.language=chinese # 语言（english/chinese）默认chinese  
ko-time.threshold=800.0 # 时间阈值，用于前端展示，大于阈值显示红色，小于阈值显示绿色，默认800  
# ko-time.context-path=http://localhost:80 # 前端页面调用接口的上下文环境，无法自动获取时可手动配置，一般情况切记不要配置   v2.0.1开始支持  
ko-time.exception-enable=true # 是否开启异常检测，默认为false,开启后会对方法内部抛出的异常进行统计 v2.0.0开始支持  
ko-time.auth-enable=true # 是否开启认证，默认为false,开启后需要登录才能访问调用链路 v2.0.2开始支持  
ko-time.user-name=xxxx # 登录用户 v2.0.2开始支持  
ko-time.password=xxxx # 登录密码 v2.0.2开始支持  
# ko-time.static-token=xxxx # 使用静态的token值进行认证访问（/koTime?kotoken=xxx） v2.3.7开始支持，该模式和user-name认证模式二选一 
ko-time.param-analyse=true # 是否开启入参组合分析 默认开启 v2.0.8开始支持 双击方法节点即可看到效果 
ko-time.data-reset=false # 启动时是否删除过往数据 默认false v2.2.3开始支持
ko-time.thread-num=2 # 调用信息存储线程数（为了不影响项目本身性能，链路存储异步进行），默认2，该值并非越大越好，瓶颈取决于数据库性能和服务器配置，尽可能少占用项目资源为上  v2.2.0-BETA开始支持
ko-time.discard-rate=0.3 # 丢弃率（0-1） 同一个方法在多次连续调用时，耗时差距并不大，为了存储的IO性能考虑，可以随机丢弃一部分耗时数据。默认30% v2.2.5开始支持
ko-time.auth-expire=43200 # 登录超时时间考虑 默认43200s(12个小时) v2.3.0开始支持
ko-time.version-notice=true # 获取最新版本通知开关 在页面上可以看到 默认true v2.3.3开始支持
```

## 访问

> 注意：    
> 1.引入了上面的依赖和配置以后，确认项目中是否有aop相关的包，koTime使用了@Aspect注解，未引入的自行引入，如aspectj或者spring-boot-starter-aop        
> 2.做完前面的步骤，koTime的集成已经完毕，无需进行其他配置   
> 3.如果后台有权限认证，需要放开`/koTime`和`/koTime/**`  
> 4.如果项目对返回体做了全局的拦截包装处理，请记得把KoTime放生，KoTime有自己的返回格式
> 5.v2.2.5开始集成了邮件功能，如果项目报错javamail相关的类NotFoundException时，需要引入：

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```



* 启动项目访问 `/koTime` 路径即可

* 如果仅仅只是想统计某个方法，在方法上加上`@ComputeTime`即可，控制台会输出耗时


> 建议使用谷歌浏览器(请升级的90.xx以后的版本，早起版本可能存在部分样式不兼容)或者Edge浏览器，IE是不可能支持的

如果项目自定义的contextpath，访问如`http://localhost:8080/xxx服务/koTime`

如：application.properties中定义了 `server.servlet.context-path=/myservice`，那么访问路径为`http://localhost:8080/myservice/koTime`

如果页面能正常显示，但是无法获取方法链路，可配置ko-time.context-path=http://正确路径/myservice  进行koTime接口访问路径纠正

> 如果出现ClassNotFound之类的错误，一般都是依赖冲突或者对应的类没有引入，自行解决，kotime的依赖可参见源码的pom文件



---


