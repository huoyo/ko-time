package cn.langpy.kotime.handler;


import cn.langpy.kotime.model.RunTimeNode;
import cn.langpy.kotime.service.InvokeService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;

public interface ComputeTimeHandlerInterface {
    @Pointcut("")
     void prog();

    @Around("prog()")
    default Object globalCompute(ProceedingJoinPoint pjp) throws Throwable{
        long begin = System.nanoTime();
        Object obj=pjp.proceed();
        long end =System.nanoTime();
        String packName = this.getClass().getPackage().getName();
        RunTimeNode parent = InvokeService.getParentRunTimeNode(packName);
        RunTimeNode current = InvokeService.getCurrentRunTimeNode(pjp,((end-begin)/1000000.0));
        InvokeService.createGraph(parent,current);
        return obj;
    }

}
