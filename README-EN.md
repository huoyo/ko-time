<div align="center">
    <h1 >KoTime</h1> 
</div>


<div align="center">
    <img src='https://shields.io/badge/version-2.0.9-green.svg'>
    <img src='https://shields.io/badge/author-Chang Zhang-dbab09.svg'>
    <img src='https://shields.io/badge/dependencies-Spring|Java JWT|aspectjweaver|tomcat|UIKit-r.svg'>
    <h4>A springboot tool for tracking the paths of the methods,which can help you to find methods's performances easily.</h4>
        <ul>
            <li>Tracking the Java methods and calculating their execution times</li>
            <li>Show the paths of the methods</li>
        </ul>
</div>


## Documents

[中文简介/CHINESE](README.md)

[documents](http://kotimedoc.langpy.cn/)



## Display

1.Calculating the execution times of Java methods

Finding the methods that need optimize

![输入图片说明](docs/v201/zl.png)

2. Interface methods can be seen as follows

You can see every interface method that was called, and you need optimize them when they are red

![输入图片说明](docs/v201/apis.png)

3.Calling details

After clicking a inteface method,you can find paths of the method

![输入图片说明](docs/v203/ff.png)

## Important Versions

> V1.0：Basic functions

> V1.2：Error version

> V1.3：Log and time threshold

> V1.4：Add koTime.pointcut


> V1.8：Add the support for Mybatis and some metrics

> V1.9：--

> V2.0.0：Add the support for exceptions and data interfaces

> V2.0.1：Remove freemarker and thymeleaf;
          Remove 'spring.profiles.active=koTime';
          Remove getAllStackTraces();
          Remove layui;
          Optimize configs；

> V2.0.2：Add auth;


> V2.0.7：Display routes for Controllers

> V2.0.8：times for various params combinations


## Copyright

> 1.This project belongs to Chang Zhang,and Its open source protocol is Apache-2.0；
>
> 2.You can use it freely  but please respect copyright;
>
> 3.Please specify the differences if you update codes；
>




