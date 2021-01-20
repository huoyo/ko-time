package cn.langpy.kotime.service;


import cn.langpy.kotime.model.RunTimeNode;
import cn.langpy.kotime.util.Common;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class InvokeService {

    public static RunTimeNode getParentRunTimeNode(String packName) {
        String parentClassName = "";
        String parentMothodName = "";
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        StackTraceElement stack = Common.filter(stacks,packName);
        if (stack!=null) {
            parentClassName = stack.getClassName();
            parentMothodName = stack.getMethodName();
        }
        RunTimeNode parent = new RunTimeNode();
        parent.setClassName(parentClassName);
        parent.setMethodName(parentMothodName);
        parent.setName(parentClassName.substring(parentClassName.lastIndexOf(".")+1)+"."+parentMothodName);
        parent.setMethodType(Common.getMethodType(parentClassName));
        parent.setChildren(new ArrayList<>());
        return parent;
    }

    public static RunTimeNode getCurrentRunTimeNode(MethodInvocation pjp, Double runTime) {
        String className = pjp.getThis().getClass().getName();
        String methodName = pjp.getMethod().getName();
        RunTimeNode current = new RunTimeNode();
        current.setName(className.substring(className.lastIndexOf(".")+1)+"."+methodName);
        current.setClassName(className);
        current.setMethodName(methodName);
        current.setAvgRunTime(runTime);
        current.setChildren(new ArrayList<>());
        current.setMethodType(Common.getMethodType(pjp));
        return current;
    }
    public static RunTimeNode getCurrentRunTimeNode(ProceedingJoinPoint pjp, Double runTime) {
        String className = pjp.getTarget().getClass().getName();
        String methodName = pjp.getSignature().getName();
        RunTimeNode current = new RunTimeNode();
        current.setName(className.substring(className.lastIndexOf(".")+1)+"."+methodName);
        current.setClassName(className);
        current.setMethodName(methodName);
        current.setAvgRunTime(runTime);
        current.setChildren(new ArrayList<>());
        current.setMethodType(Common.getMethodType(pjp));
        return current;
    }

    public static void createGraph(RunTimeNode parent, RunTimeNode current) {
        if (current.getMethodName().contains("$")) {
            return;
        }
        Common.showLog(current);
        String parentKey = parent.getClassName()+"."+parent.getMethodName();
        String currentKey = current.getClassName()+"."+current.getMethodName();
        if (".".equals(parentKey)) {
            RunTimeNodeService.addOrUpdate(currentKey,current);
        }else if (RunTimeNodeService.containsNode(parent)) {
            RunTimeNodeService.addOrUpdateChildren(parent,current);
        }else{
            List<RunTimeNode> list = new ArrayList<>();
            list.add(current);
            parent.setChildren(list);
            RunTimeNodeService.add(parentKey,parent);
        }
    }

}
