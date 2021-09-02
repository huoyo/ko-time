package cn.langpy.kotime.util;

import cn.langpy.kotime.model.MethodRelation;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

public class Common {
    public static Logger log = Logger.getLogger(Common.class.toString());


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

