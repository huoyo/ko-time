package cn.langpy.kotime.util;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.StringStack;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Stack;

/**
 * zhangchang
 */
public class MethodStack {

    private static ThreadLocal<Stack> threadMethods = new ThreadLocal<>();

    public static void record(MethodInvocation pjp) {
        String className = pjp.getMethod().getDeclaringClass().getName();
        String methodName = pjp.getMethod().getName();
        Stack<String> queue = null;
        if (null==threadMethods.get()) {
            queue = new StringStack();
        }else {
             queue = threadMethods.get();
        }
        queue.add(className+"#"+methodName);
        threadMethods.set(queue);
    }

    public static Stack get() {
        return threadMethods.get();
    }

    public static void clear() {
        Stack<String> queue = threadMethods.get();
        if (queue==null) {
            return;
        }
        queue.pop();
        if (queue.isEmpty()) {
            threadMethods.remove();
        }
    }
}
