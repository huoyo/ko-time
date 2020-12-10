package cn.langpy.kotime.handler;

import cn.langpy.kotime.annotation.ComputeTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;


@Slf4j
@Aspect
@Component
public class ComputeTimeHandler {



    @Pointcut("@annotation(cn.langpy.kotime.annotation.ComputeTime)")
    public void preProcess(){
    }

    @Around("preProcess()")
    public Object doAroundCompute(ProceedingJoinPoint pjp) throws Throwable{
        ComputeTime computeTime = ((MethodSignature)pjp.getSignature()).getMethod().getAnnotation(ComputeTime.class);
        long begin = System.nanoTime();
        Object obj=pjp.proceed();
        long end =System.nanoTime();
        if ("chinese".equals(computeTime.value())) {
            log.info("调用方法={}，耗时={}毫秒",pjp.getTarget().getClass().getName()+"."+pjp.getSignature().getName(),(end-begin)/1000000);
        }else{
            log.info("method={},runTime={}ms",pjp.getTarget().getClass().getName()+"."+pjp.getSignature().getName(),(end-begin)/1000000);
        }
        return obj;
    }
}
