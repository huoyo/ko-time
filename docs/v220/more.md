## 存储多样性

v2.2.0开始支持数据库存储接口信息功能，可在内存和数据库中进行切换

### 内存存储

更改配置：

> ko-time.saver=memory #默认存储方式，无需此配置也行


### Redis存储

1.更改配置：

> ko-time.saver=redis
> 
> ko-time.data-prefix=xxx #如果多个项目共用一个redis，最好配置此项，通过该名称区分数据
> 
> #redis配置
> 
> spring.redis.host: xxx
> 
> spring.redis.port: xxx
> 
> spring.redis.password: xxx
> 


2.引入依赖

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

3.其他

到此配置结束，需要说明的是项目默认使用Springboot的StringRedisTemplate对象，无需其他操作
如果对其不满意，可以自定Bean如：

```java
@Bean("redisbean")
public StringRedisTemplate getRedisTemplate(RedisConnectionFactory connectionFactory){
    StringRedisTemplate template = new StringRedisTemplate();
    template.setConnectionFactory(connectionFactory);
    return template;
}
```
然后配置

> ko-time.redis-template: redisbean


### 数据库存储

> 注： 使用mysql

1.更改配置：

> ko-time.saver=database

> 请引入MySQL相关依赖
> 注：默认使用项目的数据源，多数据源情况下默认使用主数据源，如果没有主数据源，请在配置中指定数据源：`ko-time.data-source=数据源名称`

2.数据表创建

> 数据库字段长度不够的自行调整

```sql
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

---


