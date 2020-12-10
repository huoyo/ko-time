package cn.langpy.kotime.util;

import cn.langpy.kotime.model.RunTimeNode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

public class Common {

    public static RunTimeNode getParentRunTimeNode(String packName) {
        String parentClassName = "";
        String parentMothodName = "";
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        String[] packNameSplit = packName.split("\\.");
        String filter = packNameSplit.length>1 ? packNameSplit[0]+"."+packNameSplit[1] : packNameSplit[0];
        int stacksLength = stacks.length;
        for (int i = 0; i < stacksLength; i++) {
            StackTraceElement stack = stacks[i];
            if (stack.getClassName().startsWith(filter)&& !stack.getClassName().contains("$")) {
                parentClassName = stack.getClassName();
                parentMothodName = stack.getMethodName();
                break;
            }
        }
        RunTimeNode parent = new RunTimeNode();
        parent.setClassName(parentClassName);
        parent.setMethodName(parentMothodName);
        parent.setName(parentClassName.substring(parentClassName.lastIndexOf(".")+1)+"."+parentMothodName);
        parent.setMethodType(getMethodType(parentClassName));
        parent.setChildren(new ArrayList<>());
        return parent;
    }

    public static RunTimeNode getCurrentRunTimeNode(ProceedingJoinPoint pjp,Double runTime) {
        String className = pjp.getTarget().getClass().getName();
        String methodName = pjp.getSignature().getName();
        RunTimeNode current = new RunTimeNode();
        current.setName(className.substring(className.lastIndexOf(".")+1)+"."+methodName);
        current.setClassName(className);
        current.setMethodName(methodName);
        current.setAvgRunTime(runTime);
        current.setChildren(new ArrayList<>());
        current.setMethodType(getMethodType(pjp));
        return current;
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
            }else if (className.contains("dao") || className.contains("mapper")) {
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
        }else if (className.contains("dao") || className.contains("mapper")) {
            methodType = MethodType.Dao;
        }else{
            methodType = MethodType.Others;
        }
        return methodType;
    }

}
