## 额外依赖引入

koTime使用了@Aspect注解，未引入 aop相关包的自行引入，如aspectj或者spring-boot-starter-aop


## pointcut写法参考

pointcut直接引用了aop中的写法，下面简要提供几个写法：

假设项目的包路径为：

```
com.huoyo.demo
        |-controller
        |-service
        |-mapper
        |-others
            |-other1
            |-other2
            |-Test.java
        
```

想要切`cn.langpy.demo`下面的所有方法（包括子包中的）,可以写：

> `execution(public * com.huoyo.demo..*.*(..))` #切记，是两个点.


只想要切`cn.langpy.demo.controller`下面的类的所有方法（不包括子包的）,可以写：

> `execution(public * com.huoyo.demo.controller.*.*(..))` #切记，是一个点.

只想要切`cn.langpy.demo.others`下面的类的所有方法（不包括other1和other2下面的）,可以写：

> `execution(public * com.huoyo.demo.others.*.*(..))` 

只想要切`cn.langpy.demo.others`下面的类的所有方法（包括other1和other2）,可以写：

> `execution(public * com.huoyo.demo.others..*.*(..))`

更多写法请详细参考aop

## 是否支持前后端分离项目

支持！

## 如何与shiro集成

koTime没有做相关方面的限制，在shiro的配置中将相关路径放开即可，如：

```Java
ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
/*设置过滤*/
Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
/*authc:所有url都必须认证通过才可以访问;*/
/*anon:所有url都都可以匿名访问*/
filterChainDefinitionMap.put("/koTime", "anon");
filterChainDefinitionMap.put("/koTime/**", "anon");
shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

```

## 运行时间计算

运行时间分了三个指标：**平均运行时间、最大运行时间、最小运行时间**

平均运行时间会将方法下每一次的运行时间与之前的求平均


## 数据是否会保存

目前暂不支持数据的存储，也就是每一次项目重启之后，以往的统计数据都归0

## 集成是否需要复制资源文件

不需要复制  static和template 等资源文件，引入依赖时已自动集成

## 能正常启动但是页面样式不对

打开f12查看静态资源路径加载是否正确，如果不正确，手动配置属性`ko-time.context-path=http://ip:port/contextPath`

## V2.0.1开始更改了配置，V2.0.0的配置方式是否可用

V2.0.1开始，两种配置均生效，建议使用新的配置方式

## 问题咨询

访问[koTime开源地址](https://gitee.com/huoyo/ko-time)进行咨询

---

**我觉得作者列的问题太少了，不能满足我，我准备用金钱对他进行鞭笞，并备注上本大爷的要求**

<img src="v201/pay.jpg"  width="15%" height="15%">
