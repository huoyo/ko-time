package cn.langpy.kotime.config;


import cn.langpy.kotime.handler.RunTimeHandler;
import cn.langpy.kotime.model.KoTimeConfig;
import cn.langpy.kotime.util.Context;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class DefaultConfig {
    @Value("${koTime.log.language:chinese}")
    private String logLanguage;
    @Value("${koTime.log.enable:false}")
    private Boolean logEnable;
    @Value("${koTime.time.threshold:800.0}")
    private Double timeThreshold;
    @Value("${koTime.pointcut:execution(* cn.langpy.kotime.controller.KoTimeController.*(..))}")
    private String pointcut;

    @PostConstruct
    public void function() {
        KoTimeConfig config = new KoTimeConfig();
        config.setLogEnable(logEnable);
        config.setLogLanguage(logLanguage);
        config.setTimeThreshold(timeThreshold);
        Context.setConfig(config);
    }

    @Bean
    public AspectJExpressionPointcutAdvisor configurabledvisor() {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(pointcut);
        advisor.setAdvice(new RunTimeHandler());
        return advisor;
    }

    public Double getTimeThreshold() {
        return timeThreshold;
    }

    public void setTimeThreshold(Double timeThreshold) {
        this.timeThreshold = timeThreshold;
    }

    public String getLogLanguage() {
        return logLanguage;
    }

    public void setLogLanguage(String logLanguage) {
        this.logLanguage = logLanguage;
    }

    public Boolean getLogEnable() {
        return logEnable;
    }

    public void setLogEnable(Boolean logEnable) {
        this.logEnable = logEnable;
    }
}
