
## 简介
koTime是一个springboot项目性能分析工具，通过追踪方法调用链路以及对应的运行时长快速定位性能瓶颈


## 优点

> * 实时监听方法，统计运行时长
> * web展示方法调用链路，瓶颈可视化追踪




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


## 作者

> Huoyo/Zhang Chang

---

