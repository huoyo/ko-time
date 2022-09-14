
<div align="center">
    <h1 >KoTime</h1> 
</div>

<div align="center">
    <img src='https://shields.io/badge/version-2.2.3-green.svg'>
    <img src='https://shields.io/badge/author-Chang Zhang-dbab09.svg'>
    <img src='https://shields.io/badge/dependencies-Spring|aspectjweaver|tomcat|UIKit-r.svg'>
    <h4>koTime是一个springboot项目性能分析工具，通过追踪方法调用链路以及对应的运行时长快速定位性能瓶颈</h4>
    <ul>
        <li>实时监听方法，统计运行时长</li>
        <li>web展示方法调用链路，瓶颈可视化追踪</li>
        <li>追踪系统异常，精确定位到方法</li>
    </ul>

---

<h4>A springboot tool for tracking the paths of methods,which can help you to find methods's performances easily.</h4>
<ul>
    <li>Tracking the Java methods and calculating their execution times</li>
    <li>Show the paths of methods</li>
    <li>Find exceptions occured in methods</li>
</ul>
</div>


## 开源地址

[Gitee](https://gitee.com/huoyo/ko-time.git)

[Github](https://github.com/huoyo/ko-time.git)


## 文档

[英文简介/ENGLISH](README-EN.md)

[使用教程](http://kotimedoc.langpy.cn/)




## 可视化展示

1.接口调用统计

根据颜色判断需要优化的接口数，红色为待优化，绿色为正常

![输入图片说明](docs/v201/zl.png)

2.接口列表总览

在列表中会显示该接口的运行耗时，如果为绿色则无需优化，如果为红色，需要详细查看问题所在

![输入图片说明](docs/v201/apis.png)

3.调用详情

点开接口时，会显示该接口的调用链路以及运行时长，红色节点即为需要优化的节点

![输入图片说明](docs/v204/ff.png)

## 重要版本说明

> V1.0：基本功能

> V1.1：接口统计

> V1.2：不可用，错误版本

> V1.3：添加日志、时间阈值可配置

> V1.4：添加koTime.pointcut配置

> V1.8：支持Mybatis的Mapper监测、新增最大/最小运行时间、修复小数位数过长页面边界溢出的bug

> V2.0.0：添加异常监测，开放数据接口，修复与swagger冲突bug，添加配置动态更新功能以及重构数据存储机制

> V2.0.1：移除freemarker与thymeleaf；
         移除spring.profiles.active=koTime配置；
         优化方法链路获取机制(移除getAllStackTraces())；
         替换layui；
         优化配置方式；
         优化页面显示

> V2.0.2：新增登录认证；
          优化页面加载；
          修复方法循环调用栈溢出的bug

> V2.0.7：Controller层显示路由

> V2.0.8：入参组合分析

> V2.2.0：添加数据库存储支持

> V2.2.2：添加英文支持

> V2.2.3：添加redis存储支持

## 特别说明

1.本项目使用java8开发，其他版本未曾试验，如有什么bug还请告知！

## 版权说明

> 1.本项目版权属作者所有，并使用 Apache-2.0进行开源；
>
> 2.您可以使用本项目进行学习、商用或者开源，但任何使用了本项目的代码的软件和项目请尊重作者的原创权利；
>
> 3.如果您使用并修改了本项目的源代码，请注明修改内容以及出处；
>
> 4.其他内容请参考Apache-2.0


## 交流群

![输入图片说明](docs/image.png)


