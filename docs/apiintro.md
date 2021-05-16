
## 获取接口方法列表

* 接口名

>`GET`  /koTime/getApis

* 返回示例

```json
[
    {
        "id":"com.example.demo.controller.IndexController.test1",
        "name":"IndexController.test1",
        "className":"com.example.demo.controller.IndexController",
        "methodName":"test1",
        "value":2001.38,
        "avgRunTime":2001.38,
        "maxRunTime":2001.9,
        "minRunTime":2001.19,
        "methodType":"Controller",
        "exceptionNum":0,
        "children":[

        ],
        "exceptions":[

        ]
    },
    {
        "id":"com.example.demo.controller.IndexController.test2",
        "name":"IndexController.test1",
        "className":"com.example.demo.controller.IndexController",
        "methodName":"test1",
        "value":2001.38,
        "avgRunTime":2001.38,
        "maxRunTime":2001.9,
        "minRunTime":2001.19,
        "methodType":"Controller",
        "exceptionNum":0,
        "children":[

        ],
        "exceptions":[

        ]
    }
]
```


## 获取方法列表调用链路

* 接口名

>`GET`  /koTime/getTree?methodName=每一个方法的id

* 返回示例
```json
{
    "id":"com.example.demo.controller.IndexController.test1",
    "name":"IndexController.test1",
    "className":"com.example.demo.controller.IndexController",
    "methodName":"test1",
    "value":2001.38,
    "avgRunTime":2001.38,
    "maxRunTime":2001.9,
    "minRunTime":2001.19,
    "methodType":"Controller",
    "exceptionNum":0,
    "children":[
        {
            "id":"com.example.demo.service.impl.IndexServiceImpl.getParents",
            "name":"IndexServiceImpl.getParents",
            "className":"com.example.demo.service.impl.IndexServiceImpl",
            "methodName":"getParents",
            "value":0.47,
            "avgRunTime":0.47,
            "maxRunTime":0.59,
            "minRunTime":0.43,
            "methodType":"Service",
            "exceptionNum":1,
            "children":[
                {
                    "id":"com.example.demo.service.impl.IndexServiceImpl.index2",
                    "name":"IndexServiceImpl.index2",
                    "className":"com.example.demo.service.impl.IndexServiceImpl",
                    "methodName":"index2",
                    "value":0.35,
                    "avgRunTime":0.35,
                    "maxRunTime":0.4,
                    "minRunTime":0.32,
                    "methodType":"Service",
                    "exceptionNum":0,
                    "children":[

                    ],
                    "exceptions":[

                    ]
                }
            ],
            "exceptions":[
                {
                    "id":"java.lang.RuntimeExceptionRuntimeException获取信息失败",
                    "name":"RuntimeException",
                    "className":"java.lang.RuntimeException",
                    "message":"获取信息失败",
                    "location":91,
                    "methodName":null,
                    "occurClassName":null
                }
            ]
        },
        {
            "id":"com.example.demo.service.impl.IndexServiceImpl.getUsers",
            "name":"IndexServiceImpl.getUsers",
            "className":"com.example.demo.service.impl.IndexServiceImpl",
            "methodName":"getUsers",
            "value":1000.91,
            "avgRunTime":1000.91,
            "maxRunTime":1006.25,
            "minRunTime":1000.79,
            "methodType":"Service",
            "exceptionNum":1,
            "children":[
                {
                    "id":"com.example.demo.service.impl.IndexServiceImpl.index1",
                    "name":"IndexServiceImpl.index1",
                    "className":"com.example.demo.service.impl.IndexServiceImpl",
                    "methodName":"index1",
                    "value":0.42,
                    "avgRunTime":0.42,
                    "maxRunTime":3.81,
                    "minRunTime":0.37,
                    "methodType":"Service",
                    "exceptionNum":0,
                    "children":[
                        {
                            "id":"com.example.demo.service.impl.IndexServiceImpl.test2",
                            "name":"IndexServiceImpl.test2",
                            "className":"com.example.demo.service.impl.IndexServiceImpl",
                            "methodName":"test2",
                            "value":0.01,
                            "avgRunTime":0.01,
                            "maxRunTime":0.02,
                            "minRunTime":"0.0",
                            "methodType":"Service",
                            "exceptionNum":0,
                            "children":[

                            ],
                            "exceptions":[

                            ]
                        },
                        {
                            "id":"com.example.demo.service.impl.IndexServiceImpl.test1",
                            "name":"IndexServiceImpl.test1",
                            "className":"com.example.demo.service.impl.IndexServiceImpl",
                            "methodName":"test1",
                            "value":0.04,
                            "avgRunTime":0.04,
                            "maxRunTime":0.08,
                            "minRunTime":0.03,
                            "methodType":"Service",
                            "exceptionNum":0,
                            "children":[

                            ],
                            "exceptions":[

                            ]
                        }
                    ],
                    "exceptions":[

                    ]
                }
            ],
            "exceptions":[
                {
                    "id":"java.lang.RuntimeExceptionRuntimeException认证失败",
                    "name":"RuntimeException",
                    "className":"java.lang.RuntimeException",
                    "message":"认证失败",
                    "location":82,
                    "methodName":null,
                    "occurClassName":null
                }
            ]
        }
    ],
    "exceptions":[

    ]
}
```

## 获取异常列表

* 接口名

>`GET`  /koTime/getExceptions

* 返回示例

```json
[
    {
        "id":"java.lang.RuntimeExceptionRuntimeException认证失败",
        "name":"RuntimeException",
        "className":"java.lang.RuntimeException",
        "message":"认证失败",
        "value":82
    },
    {
        "id":"java.lang.RuntimeExceptionRuntimeException获取信息失败",
        "name":"RuntimeException",
        "className":"java.lang.RuntimeException",
        "message":"获取信息失败",
        "value":91
    }
]
```

## 获取异常详情

* 接口名

> `GET` /koTime/getMethodsByExceptionId?exceptionId=xx

* 返回示例
```json
[
    {
        "id":"java.lang.RuntimeExceptionRuntimeException获取信息失败",
        "name":"RuntimeException",
        "className":"java.lang.RuntimeException",
        "message":"获取信息失败",
        "location":91,
        "methodName":"getParents",
        "occurClassName":"com.example.demo.service.impl.IndexServiceImpl"
    }
]
```

## 获取当前配置信息

* 接口名

>`GET` /koTime/getConfig

* 返回示例

```json
{
    "logLanguage":"chinese",
    "kotimeEnable":true,
    "logEnable":false,
    "timeThreshold":"800.0",
    "exceptionEnable":true,
    "dataSaver":"memory"
}
```

## 更新当前配置信息

* 接口名

>`POST` /koTime/updateConfig

* 参数示例

```json
{
    "kotimeEnable":true,
    "logEnable":false,
    "timeThreshold":"800.0",
    "exceptionEnable":true
}
```