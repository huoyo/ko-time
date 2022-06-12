## 存储多样性

v2.2.0开始支持数据库存储接口信息功能，可在内存和数据库中进行切换

### 内存存储

更改配置：

> ko-time.saver=memory #默认存储方式，无需此配置也行

### 数据库存储

> 注： 使用mysql

1.更改配置：

> ko-time.saver=database


> 注：默认使用项目的数据源，多数据源情况下默认使用主数据源，如果没有主数据源，请在配置中指定数据源：`ko-time.data-source=数据源名称`

2.数据表创建

```sql
create table ko_method_node (
     id varchar(200) not null primary key comment '主键' ,
     name varchar(200) null comment '类名+方法名' ,
     class_name varchar(200) null comment '类名' ,
     method_name varchar(200) null comment '方法名' ,
     route_name varchar(200) null comment '路由，controller才有' ,
     method_type varchar(64) null comment '方法类型'
) comment '方法信息表';


create table ko_method_relation (
     id varchar(200) not null primary key comment '' ,
     source_id varchar(200) null comment '调用方id' ,
     target_id varchar(200) null comment '被调用方id' ,
     avg_run_time numeric(10,2) null comment '平均耗时' ,
     max_run_time numeric(10,2) null comment '最大耗时' ,
     min_run_time numeric(10,2) null comment '最小耗时'
) comment '方法调用关系表';

;
create table ko_exception_node (
    id varchar(200) not null primary key comment '主键' ,
    name varchar(200) null comment '异常名' ,
    class_name varchar(200) null comment '类名' ,
    message varchar(200) null comment '异常消息'
) comment '异常表';


create table ko_exception_relation (
    id varchar(200) not null primary key comment '' ,
    source_id varchar(200) null comment '调用方法id' ,
    target_id varchar(200) null comment '异常id' ,
    location int null comment '异常位置'
) comment '异常关系表';

create table ko_param_ana (
       source_id varchar(200) null comment '调用方法id' ,
       params varchar(200) null comment '参数组合，-分隔' ,
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


