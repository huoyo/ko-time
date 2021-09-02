package cn.langpy.kotime.handler;

import cn.langpy.kotime.annotation.Auth;
import cn.langpy.kotime.constant.KoConstant;
import cn.langpy.kotime.util.Context;
import cn.langpy.kotime.util.KoUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.logging.Logger;


@Aspect
@Component
public class AuthHandler {
    public static Logger log = Logger.getLogger(AuthHandler.class.toString());

    @Pointcut(KoConstant.authRange)
    public void preProcess() {
    }

    @Around("preProcess()")
    public Object doAroundCompute(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        boolean needAuth = method.isAnnotationPresent(Auth.class);
        if (needAuth&& Context.getConfig().getAuthEnable()) {
            KoUtil.checkLogin();
        }
        return pjp.proceed();
    }
}
