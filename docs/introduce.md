
## 简介
koTime是一个springboot项目性能分析工具，通过追踪方法调用链路以及对应的运行时长快速定位性能瓶颈

## 预览

http://huoyo.gitee.io/ko-time/example



## 优点
> * 实时监听方法，统计运行时长
> * web展示方法调用链路，瓶颈可视化追踪



## 缺点
> * 由于对项目中每个方法进行监控，在性能层面会有一点影响，建议在开发阶段使用


## 版本说明

> V1.0：基本功能

> V1.1：接口统计

> V1.2：不可用，错误版本

> V1.3：添加日志、时间阈值可配置

> V1.4：添加koTime.pointcut配置

> V1.5：剔除lombok

> V1.6：兼容thymeleaf

> V1.7：修复未调用接口时No value present异

> V1.8：支持Mybatis的Mapper监测、新增最大/最小运行时间、修复小数位数过长页面边界溢出的bug

> V1.9：新增异常检测支持（开发中）

## 作者
> Huoyo/Zhang Chang
