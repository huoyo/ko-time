package cn.langpy.kotime.util;

import cn.langpy.kotime.model.MethodRelation;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * zhangchang
 */
public class Common {
    public static Logger log = Logger.getLogger(Common.class.toString());

    public static String getRoute(MethodInvocation pjp) {
        Class<?> targetClass = pjp.getThis().getClass();
        String[] classRoute = getRouteValue(targetClass);
        if (classRoute == null || classRoute.length==0) {
            return null;
        }
        StringBuilder routes = new StringBuilder(classRoute[0]);
        String[] methodRoute = getRouteValue(pjp.getMethod());
        if (methodRoute[0].startsWith("/")) {
            routes.append(methodRoute[0]);
        } else {
            routes.append("/" + methodRoute[0]);
        }
        return routes.toString();
    }

    private static String[] getRouteValue(Class<?> targetClass) {
        String[] methodRoute = null;
        RequestMapping methodAnnotationRequest = targetClass.getAnnotation(RequestMapping.class);
        if (methodAnnotationRequest == null) {
            PostMapping methodAnnotationPost = targetClass.getAnnotation(PostMapping.class);
            if (methodAnnotationPost == null) {
                GetMapping methodAnnotationGet = targetClass.getAnnotation(GetMapping.class);
                if (methodAnnotationGet == null) {
                    PutMapping methodAnnotationPut = targetClass.getAnnotation(PutMapping.class);
                    if (methodAnnotationPut == null) {
                        DeleteMapping methodAnnotationDelete = targetClass.getAnnotation(DeleteMapping.class);
                        if (methodAnnotationDelete == null) {
                            return null;
                        } else {
                            methodRoute = methodAnnotationDelete.value();
                        }
                    } else {
                        methodRoute = methodAnnotationPut.value();
                    }
                } else {
                    methodRoute = methodAnnotationGet.value();
                }
            } else {
                methodRoute = methodAnnotationPost.value();
            }
        } else {
            methodRoute = methodAnnotationRequest.value();
        }
        return methodRoute;
    }

    private static String[] getRouteValue(Method method) {
        String[] methodRoute = null;
        RequestMapping methodAnnotationRequest = method.getAnnotation(RequestMapping.class);
        if (methodAnnotationRequest == null) {
            PostMapping methodAnnotationPost = method.getAnnotation(PostMapping.class);
            if (methodAnnotationPost == null) {
                GetMapping methodAnnotationGet = method.getAnnotation(GetMapping.class);
                if (methodAnnotationGet == null) {
                    PutMapping methodAnnotationPut = method.getAnnotation(PutMapping.class);
                    if (methodAnnotationPut == null) {
                        DeleteMapping methodAnnotationDelete = method.getAnnotation(DeleteMapping.class);
                        if (methodAnnotationDelete == null) {
                            return null;
                        } else {
                            methodRoute = methodAnnotationDelete.value();
                        }
                    } else {
                        methodRoute = methodAnnotationPut.value();
                    }
                } else {
                    methodRoute = methodAnnotationGet.value();
                }
            } else {
                methodRoute = methodAnnotationPost.value();
            }
        } else {
            methodRoute = methodAnnotationRequest.value();
        }
        return methodRoute;
    }

    public static MethodType getMethodType(MethodInvocation pjp) {
        Class<?> targetClass = pjp.getThis().getClass();
        if (targetClass.getAnnotation(Controller.class) != null || targetClass.getAnnotation(RestController.class) != null) {
            return MethodType.Controller;
        } else if (targetClass.getAnnotation(Service.class) != null) {
            return MethodType.Service;
        } else if (targetClass.getAnnotation(Repository.class) != null) {
            return MethodType.Dao;
        }
        String className = pjp.getMethod().getDeclaringClass().getName().toLowerCase();
        if (className.contains("controller")) {
            return MethodType.Controller;
        } else if (className.contains("service")) {
            return MethodType.Service;
        } else if (className.contains("dao") || className.contains("mapper") || className.contains("com.sun.proxy.$Proxy")) {
            return MethodType.Dao;
        } else {
            return MethodType.Others;
        }
    }

    public static MethodType getMethodType(String className) {
        className = className.toLowerCase();
        if (className.contains("controller")) {
            return MethodType.Controller;
        } else if (className.contains("service")) {
            return MethodType.Service;
        } else if (className.contains("dao") || className.contains("mapper") || className.contains("com.sun.proxy.$Proxy")) {
            return MethodType.Dao;
        } else {
            return MethodType.Others;
        }
    }

    public static void showLog(String method, MethodRelation current) {
        if (Context.getConfig().getLogEnable() && "chinese".equals(Context.getConfig().getLogLanguage())) {
            log.info("调用方法=" + method + "，耗时=" + current.getAvgRunTime() + "毫秒");
        } else if (Context.getConfig().getLogEnable() && "english".equals(Context.getConfig().getLogLanguage())) {
            log.info("method=" + method + "，runTime=" + current.getAvgRunTime() + "ms");
        }
    }

}

