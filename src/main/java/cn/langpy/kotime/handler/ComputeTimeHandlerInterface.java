package cn.langpy.kotime.handler;


import cn.langpy.kotime.model.RunTimeNode;
import cn.langpy.kotime.util.Common;
import cn.langpy.kotime.util.Context;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public interface ComputeTimeHandlerInterface {

    @Pointcut("")
     void prog();

    @Around("prog()")
    default Object globalCompute(ProceedingJoinPoint pjp) throws Throwable{
        long begin = System.nanoTime();
        Object obj=pjp.proceed();
        long end =System.nanoTime();
        String packName = this.getClass().getPackage().getName();
        RunTimeNode parent = Common.getParentRunTimeNode(packName);
        RunTimeNode current = Common.getCurrentRunTimeNode(pjp,((end-begin)/1000000.0));
        Context.set(parent,current);
        return obj;
    }

}
