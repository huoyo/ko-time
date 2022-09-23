
> Translating..........

## Various saver

 You can choose different saver in {memory,database,redis} since v2.2.0

### Memory saver

Configurate:

> ko-time.saver=memory #default saver


### Redis saver

1.Configurate：

> ko-time.saver=redis
>
> ko-time.data-prefix=xxx #Configuratate this when many projects use one redis
>
> #redis Configuratations
>
> spring.redis.host: xxx
>
> spring.redis.port: xxx
>
> spring.redis.password: xxx
>


2.Add a redis denpendecy

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

3.Others

This is the end oof all the operations.

We use Springboot's StringRedisTemplate to save data for redis,and you can create a StringRedisTemplate's Bean if you want more.

```java
@Bean("redisbean")
public StringRedisTemplate getRedisTemplate(RedisConnectionFactory connectionFactory){
    StringRedisTemplate template = new StringRedisTemplate();
    template.setConnectionFactory(connectionFactory);
    return template;
}
```

then Configurate:

> ko-time.redis-template: redisbean


### Database saver

> Note： use mysql

1.更改配置：

> ko-time.saver=database

> 请引入MySQL相关依赖
> 注：默认使用项目的数据源，多数据源情况下默认使用主数据源，如果没有主数据源，请在配置中指定数据源：`ko-time.data-source=数据源名称`

2.数据表创建

> 数据库字段长度不够的自行调整

```sql
-- v2.2.4及以上版本
create table ko_method_node
(
    id          varchar(400) not null primary key comment '主键',
    name        varchar(400) null comment '类名+方法名',
    class_name  varchar(400) null comment '类名',
    method_name varchar(400) null comment '方法名',
    route_name  varchar(400) null comment '路由，controller才有',
    method_type varchar(64)  null comment '方法类型'
) comment '方法信息表';


create table ko_method_relation
(
    id           varchar(400)   not null primary key comment '',
    source_id    varchar(400)   null comment '调用方id',
    target_id    varchar(400)   null comment '被调用方id',
    avg_run_time numeric(10, 2) null comment '平均耗时',
    max_run_time numeric(10, 2) null comment '最大耗时',
    min_run_time numeric(10, 2) null comment '最小耗时'
) comment '方法调用关系表';
;
create table ko_exception_node
(
    id         varchar(400) not null primary key comment '主键',
    name       varchar(400) null comment '异常名',
    class_name varchar(400) null comment '类名'
) comment '异常表';


create table ko_exception_relation
(
    id        varchar(400) not null primary key comment '',
    source_id varchar(400) null comment '调用方法id',
    target_id varchar(400) null comment '异常id',
    location  int          null comment '异常位置',
    message    varchar(400) null comment '异常消息'
) comment '异常关系表';

create table ko_param_ana
(
    source_id    varchar(400)   null comment '调用方法id',
    params       varchar(400)   null comment '参数组合，-分隔',
    avg_run_time numeric(10, 2) null comment '平均耗时',
    max_run_time numeric(10, 2) null comment '最大耗时',
    min_run_time numeric(10, 2) null comment '最小耗时'
) comment '参数分析表';

```

```sql
-- v2.2.3及以下版本
create table ko_method_node (
     id varchar(400) not null primary key comment '主键' ,
     name varchar(400) null comment '类名+方法名' ,
     class_name varchar(400) null comment '类名' ,
     method_name varchar(400) null comment '方法名' ,
     route_name varchar(400) null comment '路由，controller才有' ,
     method_type varchar(64) null comment '方法类型'
) comment '方法信息表';


create table ko_method_relation (
     id varchar(400) not null primary key comment '' ,
     source_id varchar(400) null comment '调用方id' ,
     target_id varchar(400) null comment '被调用方id' ,
     avg_run_time numeric(10,2) null comment '平均耗时' ,
     max_run_time numeric(10,2) null comment '最大耗时' ,
     min_run_time numeric(10,2) null comment '最小耗时'
) comment '方法调用关系表';

;
create table ko_exception_node (
    id varchar(400) not null primary key comment '主键' ,
    name varchar(400) null comment '异常名' ,
    class_name varchar(400) null comment '类名' ,
    message varchar(400) null comment '异常消息'
) comment '异常表';


create table ko_exception_relation (
    id varchar(400) not null primary key comment '' ,
    source_id varchar(400) null comment '调用方法id' ,
    target_id varchar(400) null comment '异常id' ,
    location int null comment '异常位置'
) comment '异常关系表';

create table ko_param_ana (
       source_id varchar(400) null comment '调用方法id' ,
       params varchar(400) null comment '参数组合，-分隔' ,
       avg_run_time numeric(10,2) null comment '平均耗时' ,
       max_run_time numeric(10,2) null comment '最大耗时' ,
       min_run_time numeric(10,2) null comment '最小耗时'
) comment '参数分析表';
```


## 方法调用信息扩展监听

如果需要做方法调用信息进行监听，然后做一些扩展的，可以使用此方法

1.新建监听类，实现InvokedHandler，并能加上注解@KoListener即可

```java

@KoListener
public class TestInvoke implements InvokedHandler {
    @Override
    public void onInvoked(MethodNode current, MethodNode parent, Parameter[] names, Object[] values) {
        System.out.println("调用的方法："+current);
        System.out.println("调用当前方法的上一级方法："+parent);
        System.out.println("调用的方法-参数名称："+names);
        System.out.println("调用的方法-具体参数："+values);
    }
}
```

2.如果需要监听异常情况

实现InvokedHandler的默认方法即可

```java

@KoListener
public class TestInvoke implements InvokedHandler {
    @Override
    public void onInvoked(MethodNode current, MethodNode parent, Parameter[] names, Object[] values) {
       
    }

    @Override
    public void onException(MethodNode current, MethodNode parent, ExceptionNode exception, Parameter[] names, Object[] values) {
        System.out.println("异常："+exception);
    }
}
```

## 异常监听说明

开启了`ko-time.exception-enable=true`后：

> 自动开始监听方法的异常
> 在浏览器面板上可以显示
> 与全局异常捕获@ControllerAdvice不冲突

如果自己手动进行`try-catch`捕获，无法进行监听和显示，
可以使用`KoUtil.throwException(e)`进行改造，即可监听并显示：

```java
 try {
    //你的代码
} catch (Exception e) {
    //做一些你自己的处理
    KoUtil.throwException(e);
    //经过throwException代码和正常throw e一样，无法继续往下执行了
}
```

## 耗时预警通知

> v2.2.5开始加入了邮件通知功能，当方法耗时超过阈值之后，可以选择进行邮件通知

### 配置

1.必填

```properties
ko-time.mail-user=xxxx@qq.com # 设置发送者 可以设置为你的QQ邮箱 必填
ko-time.mail-code=xxxxxxx # 邮箱授权码 请到QQ邮箱申请 必填
ko-time.mail-receivers=xxxx@qq.com # 邮件接收者 多个请用英文逗号隔开 必填
```

2.选填

> 默认为qq邮箱服务器

> qq邮箱授权码申请：https://service.mail.qq.com/cgi-bin/help?subtype=1&id=28&no=1001256


```properties
ko-time.mail-enable=true # 开启邮件通知 默认false
ko-time.mail-protocol=smtp # 邮件协议 默认smtp 可以不配置
ko-time.mail-host=smtp.qq.com # 邮件服务器 默认smtp.qq.com（QQ邮件） 可以不配置
ko-time.mail-port=587 # 邮件服务器 默认587（QQ邮件端口） 可以不配置
ko-time.mail-encoding=UTF-8 # 邮件编码 默认UTF-8 可以不配置
ko-time.mail-threshold=4 # 邮件触发阈值 默认4 耗时n次超过阈值即发送邮件  可以不配置
ko-time.mail-scope=Controller # 邮件检测范围 默认Controller（接口层）   可选{All,Controller,Service,Dao,Other}
```

### 添加依赖

```

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>

```

### 接收邮件

---


