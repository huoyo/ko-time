package cn.langpy.kotime.handler;

import cn.langpy.kotime.model.ExceptionNode;
import cn.langpy.kotime.model.MethodNode;
import cn.langpy.kotime.service.MethodNodeService;
import cn.langpy.kotime.util.Context;
import cn.langpy.kotime.util.MethodStack;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Parameter;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * zhangchang
 */
public class RunTimeHandler implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        boolean kotimeEnable = Context.getConfig().getEnable();
        if (!kotimeEnable) {
            return invocation.proceed();
        }
        boolean exceptionEnable = Context.getConfig().getExceptionEnable();
        Parameter[] parameters = invocation.getMethod().getParameters();
        ThreadPoolExecutor pool = Context.getKoThreadPool();
        long begin = System.nanoTime();
        Object obj = null;
        MethodNode parent = MethodNodeService.getParentMethodNode();
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
                MethodNode current = MethodNodeService.getCurrentMethodNode(invocation, 0.0);
                if (current.getClassName().equals(e.getStackTrace()[0].getClassName())) {
                    for (InvokedHandler invokedHandler : Context.getInvokedHandlers()) {
                        pool.execute(()->invokedHandler.onInvoked(current,parent,exception,parameters, invocation.getArguments()));
                    }                }
                MethodStack.clear();
                throw e;
            }
        } else {
            obj = invocation.proceed();
        }
        long end = System.nanoTime();
        MethodNode current = MethodNodeService.getCurrentMethodNode(invocation, ((end - begin) / 1000000.0));

        for (InvokedHandler invokedHandler : Context.getInvokedHandlers()) {
            pool.execute(()->invokedHandler.onInvoked(current,parent,parameters, invocation.getArguments()));
        }
        MethodStack.clear();
        return obj;
    }
}
