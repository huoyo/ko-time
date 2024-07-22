package cn.langpy.kotime.config;

import cn.langpy.kotime.handler.RunTimeHandler;
import jakarta.annotation.Resource;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;

/**
 * @author ccz
 */
@Configuration
public class SectionConfig {
    private static Logger log = Logger.getLogger(SectionConfig.class.toString());

    @Value("${koTime.pointcut:execution(* cn.langpy.kotime.controller.KoTimeController.*(..))}")
    private String pointcut;

    @Resource
    private DefaultConfig defaultConfig;

    @Bean
    public AspectJExpressionPointcutAdvisor configurabledvisor() {
        log.info("kotime=>loading method listener");
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        String cutRange = defaultConfig.getPointcut() == null ? pointcut : defaultConfig.getPointcut();
        cutRange = cutRange + " && !@annotation(cn.langpy.kotime.annotation.KoListener)";
        advisor.setExpression(cutRange);
        advisor.setAdvice(new RunTimeHandler());
        return advisor;
    }
}
