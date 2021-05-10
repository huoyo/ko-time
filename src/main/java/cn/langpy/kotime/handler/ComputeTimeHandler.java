package cn.langpy.kotime.handler;

import cn.langpy.kotime.annotation.ComputeTime;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;


@Aspect
@Component
public class ComputeTimeHandler {
    public static Logger log = Logger.getLogger(ComputeTimeHandler.class.toString());

    @Pointcut("@annotation(cn.langpy.kotime.annotation.ComputeTime)")
    public void preProcess() {
    }

    @Around("preProcess()")
    public Object doAroundCompute(ProceedingJoinPoint pjp) throws Throwable {
        ComputeTime computeTime = ((MethodSignature) pjp.getSignature()).getMethod().getAnnotation(ComputeTime.class);
        long begin = System.nanoTime();
        Object obj = pjp.proceed();
        long end = System.nanoTime();
        if ("chinese".equals(computeTime.value())) {
            log.info("调用方法=" + pjp.getTarget().getClass().getName() + "." + pjp.getSignature().getName() + "，耗时=" + ((end - begin) / 1000000) + "毫秒");
        } else {
            log.info("method=" + pjp.getTarget().getClass().getName() + "." + pjp.getSignature().getName() + "，runTime=" + ((end - begin) / 1000000) + "ms");
        }
        return obj;
    }
}
