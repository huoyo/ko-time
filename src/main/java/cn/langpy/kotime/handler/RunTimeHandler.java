package cn.langpy.kotime.handler;

import cn.langpy.kotime.model.InvokedInfo;
import cn.langpy.kotime.model.MethodNode;
import cn.langpy.kotime.service.InvokedQueue;
import cn.langpy.kotime.service.MethodNodeService;
import cn.langpy.kotime.util.Common;
import cn.langpy.kotime.util.Context;
import cn.langpy.kotime.util.MethodStack;
import cn.langpy.kotime.util.RecordException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Parameter;

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
        long begin = System.nanoTime();
        Object obj = null;
        MethodNode parent = MethodNodeService.getParentMethodNode();
        MethodStack.record(invocation);
        InvokedInfo invokedInfo = new InvokedInfo();
        if (exceptionEnable) {
            try {
                obj = invocation.proceed();
            } catch (Exception te) {
                Exception e = null;
                if (te instanceof RecordException) {
                    e = ((RecordException) te).getOriginalException();
                }else {
                    e = te;
                }
                long end = System.nanoTime();
                invokedInfo = Common.getInvokedInfoWithException(invocation,parent,e,((end - begin) / 1000000.0));
                if (!(te instanceof RecordException)) {
                    throw te;
                }
            }finally {
                InvokedQueue.add(invokedInfo);
                InvokedQueue.wake();
                MethodStack.clear();
                return obj;
            }
        }
        obj = invocation.proceed();
        long end = System.nanoTime();
        MethodNode current = MethodNodeService.getCurrentMethodNode(invocation, ((end - begin) / 1000000.0));
        invokedInfo.setCurrent(current);
        invokedInfo.setParent(parent);
        invokedInfo.setNames(parameters);
        invokedInfo.setValues(invocation.getArguments());
        InvokedQueue.add(invokedInfo);
        InvokedQueue.wake();
        MethodStack.clear();
        return obj;
    }
}
