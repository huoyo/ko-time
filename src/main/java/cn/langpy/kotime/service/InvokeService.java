package cn.langpy.kotime.service;

import cn.langpy.kotime.model.MethodNode;
import cn.langpy.kotime.util.Common;
import cn.langpy.kotime.util.MethodStack;
import cn.langpy.kotime.util.MethodType;
import org.aopalliance.intercept.MethodInvocation;

import java.math.BigDecimal;
import java.util.Stack;
import java.util.logging.Logger;


public class InvokeService {
    public static Logger log = Logger.getLogger(InvokeService.class.toString());

    public static MethodNode getParentMethodNode() {
        Stack<String> stack = MethodStack.get();
        if (null==stack) {
            MethodNode parent = new MethodNode();
            parent.setId("com.langpy.kotime.Cotroller.dispatch");
            parent.setClassName("Cotroller");
            parent.setMethodName("dispatch");
            parent.setName("Cotroller.dispatch");
            parent.setMethodType(MethodType.Others);
            return parent;
        }
        String classMethod = stack.peek();
        String[] classMethodSplit = classMethod.split("#");
        String parentClassName = classMethodSplit[0];
        String parentMothodName = classMethodSplit[1];
        MethodNode parent = new MethodNode();
        parent.setId(parentClassName + "." + parentMothodName);
        parent.setClassName(parentClassName);
        parent.setMethodName(parentMothodName);
        parent.setName(parentClassName.substring(parentClassName.lastIndexOf(".") + 1) + "." + parentMothodName);
        parent.setMethodType(Common.getMethodType(parentClassName));
        return parent;
    }

    public static MethodNode getCurrentMethodNode(MethodInvocation pjp, Double runTime) {
        String className = pjp.getMethod().getDeclaringClass().getName();
        String methodName = pjp.getMethod().getName();
        MethodNode current = new MethodNode();
        current.setName(className.substring(className.lastIndexOf(".") + 1) + "." + methodName);
        current.setId(className + "." + methodName);
        current.setClassName(className);
        current.setMethodName(methodName);
        BigDecimal bg = BigDecimal.valueOf(runTime);
        runTime = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        current.setValue(runTime);
        current.setMethodType(Common.getMethodType(pjp));
        return current;
    }
}
