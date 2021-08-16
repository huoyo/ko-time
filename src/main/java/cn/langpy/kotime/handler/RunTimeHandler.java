package cn.langpy.kotime.handler;

import cn.langpy.kotime.model.ExceptionNode;
import cn.langpy.kotime.service.GraphService;
import cn.langpy.kotime.model.MethodNode;
import cn.langpy.kotime.service.InvokeService;
import cn.langpy.kotime.util.Context;
import cn.langpy.kotime.util.MethodStack;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class RunTimeHandler implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        boolean kotimeEnable = Context.getConfig().getEnable();
        if (!kotimeEnable) {
            return invocation.proceed();
        }
        boolean exceptionEnable = Context.getConfig().getExceptionEnable();
        long begin = System.nanoTime();
        Object obj = null;
        MethodNode parent = InvokeService.getParentMethodNode();
        MethodStack.record(invocation);
        if (exceptionEnable) {
            try {
                obj = invocation.proceed();
            } catch (Exception e) {
                ExceptionNode exception = new ExceptionNode();
                exception.setName(e.getClass().getSimpleName());
                exception.setClassName(e.getClass().getName());
                exception.setMessage(e.getMessage());
                exception.setValue(e.getStackTrace()[0].getLineNumber());
                exception.setId(exception.getClassName() + exception.getName() + exception.getMessage());
                MethodNode current = InvokeService.getCurrentMethodNode(invocation, 0.0);
                if (current.getClassName().equals(e.getStackTrace()[0].getClassName())) {
                    GraphService graphService = GraphService.getInstance();
                    graphService.addMethodNode(current);
                    graphService.addExceptionNode(exception);
                    graphService.addExceptionRelation(current, exception);
                }
                MethodStack.clear();
                throw e;
            }
        } else {
            obj = invocation.proceed();
        }
        long end = System.nanoTime();
        MethodNode current = InvokeService.getCurrentMethodNode(invocation, ((end - begin) / 1000000.0));
        GraphService graphService = GraphService.getInstance();
        graphService.addMethodNode(parent);
        graphService.addMethodNode(current);
        graphService.addMethodRelation(parent, current);
        MethodStack.clear();
        return obj;
    }
}
