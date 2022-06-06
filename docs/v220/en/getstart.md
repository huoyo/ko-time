## Add a ependency

add a dependency in pom.xml.


```
 <dependency>
    <groupId>cn.langpy</groupId>
    <artifactId>ko-time</artifactId>
    <version>2.2.0</version>
  </dependency>
```

## Config

Add some options in `application.properties`

* Required options

> 
> ko-time.pointcut=`execution(public * com.huoyo..*.*(..))` # monitoring scope(refer to aop's @pointcut)
>


* Optional options

```
ko-time.enable=true  # KoTime switch,default true
ko-time.log-enable=false  # KoTime's console log switch,default false
ko-time.log-language=chinese # console's language（english/chinese）,default chinese  
ko-time.threshold=800.0 # time threshold for the method node whose color is red if its avgRunTime>threshold,default 800  
ko-time.context-path=http://localhost:80 # context path for the viwer,and you need not set it under normal circumstances. added in v2.0.1
ko-time.exception-enable=true # exception detect switch，default false. added in v2.0.0
ko-time.auth-enable=true # authentication switch，default false. added in v2.0.2
ko-time.user-name=xxxx # username added in v2.0.2 
ko-time.password=xxxx # password added in v2.0.2 
ko-time.param-analyse=true # param-analyse switch, default true. added in v2.0.8(you can see it when you dblclick on the method node) 
ko-time.saver=memory # a place to store data，options in {memory,database} default memory. added in v2.2.0-BETA 
ko-time.thread-num=2 # thread-num to to store data,default 2. this value is up to the performance of cpu and database.added in v2.2.0-BETA
```

## Visit

> Notice：    
> 1.after adding above configurations，you need also add some dependencies about aop such as aspectj or spring-boot-starter-aop.   
> 2.allow `/koTime` and `/koTime/**` to access you service without authorization 
> 3.stop here,you done all jobs.  
                                   


* run you service and visit `/koTime`



> use chrome or edge

visit `http://localhost:8080/context-path/koTime`  if you set context-path

for example: visit `http://localhost:8080/myservice/koTime` if you set `server.servlet.context-path=/myservice` in application.properties

set `ko-time.context-path=http://localhost:8080/myservice` if you can not access you service,but it is not recommended!


---


**Donate**

<img src="v202/pay.jpg"  width="15%" height="15%">


