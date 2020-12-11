package cn.langpy.kotime.util;

import cn.langpy.kotime.model.RunTimeNode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
public class Common {

    public static StackTraceElement filter(StackTraceElement[] stacks,String packName) {
        String[] packNameSplit = packName.split("\\.");
        String filter = packNameSplit.length>1 ? packNameSplit[0]+"."+packNameSplit[1] : packNameSplit[0];
        int stacksLength = stacks.length;
        for (int i = 0; i < stacksLength; i++) {
            StackTraceElement stack = stacks[i];
            if (stack.getClassName().startsWith(filter)&& !stack.getClassName().contains("$")) {
                return stack;
            }
        }
        return null;
    }



    public static MethodType getMethodType(ProceedingJoinPoint pjp) {
        MethodType methodType = null;
        Class<?> targetClass = pjp.getTarget().getClass();
        if (targetClass.getAnnotation(Controller.class)!=null || targetClass.getAnnotation(RestController.class)!=null) {
            methodType = MethodType.Controller;
        }else if (targetClass.getAnnotation(Service.class)!=null) {
            methodType = MethodType.Service;
        }else if (targetClass.getAnnotation(Repository.class)!=null) {
            methodType = MethodType.Dao;
        }
        if (methodType == null) {
            String className = pjp.getTarget().getClass().getName().toLowerCase();
            if (className.contains("controller")) {
                methodType = MethodType.Controller;
            }else if (className.contains("service")) {
                methodType = MethodType.Service;
            }else if (className.contains("dao") || className.contains("mapper")|| className.contains( "com.sun.proxy.$Proxy")) {
                methodType = MethodType.Dao;
            }else{
                methodType = MethodType.Others;
            }
        }
        return methodType;
    }

    public static MethodType getMethodType(String className) {
        MethodType methodType = null;
        className = className.toLowerCase();
        if (className.contains("controller")) {
            methodType = MethodType.Controller;
        }else if (className.contains("service")) {
            methodType = MethodType.Service;
        }else if (className.contains("dao") || className.contains("mapper")|| className.contains( "com.sun.proxy.$Proxy")) {
            methodType = MethodType.Dao;
        }else{
            methodType = MethodType.Others;
        }
        return methodType;
    }

    public static void showLog(RunTimeNode current) {
        String currentKey = current.getClassName()+"."+current.getMethodName();
        if (Context.getConfig().getLogEnable() && "chinese".equals(Context.getConfig().getLogLanguage())) {
            log.info("调用方法="+currentKey+"，耗时="+current.getAvgRunTime()+"毫秒");
        }else if (Context.getConfig().getLogEnable() && "english".equals(Context.getConfig().getLogLanguage())) {
            log.info("method="+currentKey+"，runTime="+current.getAvgRunTime()+"ms");
        }
    }

}

