## 额外依赖引入

koTime使用了@Aspect注解，未引入 aop相关包的自行引入，如aspectj或者spring-boot-starter-aop

## 前端展示模板冲突

* 前后端分离的项目

koTime提供freemark和thymeleaf两种模板，可根据自己的爱好自由选择，如果喜欢其他的，koTime暂不支持你的喜欢

* 前后端不分离的项目

如果项目中使用了除了freemarker和thymeleaf之外的模板，那么koTime暂时不支持

如果发生资源文件和配置的冲突，可取消`spring.profiles.active=koTime`这一步配置
然后将koTime中对于freemarker和thymeleaf的配置与自身的进行合并



## 是否支持前后端分离项目

支持！目前来看，对前后端分离的项目比较友好，不分离的项目可能存在资源的文件影响

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
filterChainDefinitionMap.put("/static/**", "anon");
shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

```

## 运行时间计算

运行时间分了三个指标：**平均运行时间、最大运行时间、最小运行时间**

平均运行时间会将方法下每一次的运行时间与之前的求平均


## 数据是否会保存

目前暂不支持数据的存储，也就是每一次项目重启之后，以往的统计数据都归0

## 集成是否需要复制资源文件

不需要复制  static和template 等资源文件，引入依赖时已自动集成

## 问题咨询

访问[koTime开源地址](https://gitee.com/huoyo/ko-time)进行咨询

---

**我觉得作者列的问题太少了，不能满足我，我准备用金钱对他进行鞭笞，并备注上本大爷的要求**

<img src="pay.jpg"  width="15%" height="15%">
