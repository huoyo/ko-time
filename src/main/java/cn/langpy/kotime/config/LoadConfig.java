package cn.langpy.kotime.config;
import cn.langpy.kotime.handler.RunTimeHandler;
import cn.langpy.kotime.util.Context;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.logging.Logger;

@ComponentScan("cn.langpy.kotime")
@Configuration
public class LoadConfig {
    public static Logger log = Logger.getLogger(LoadConfig.class.toString());

    @Value("${koTime.enable:true}")
    private Boolean kotimeEnable;
    @Value("${koTime.log.language:chinese}")
    private String logLanguage;
    @Value("${koTime.log.enable:false}")
    private Boolean logEnable;
    @Value("${koTime.time.threshold:800.0}")
    private Double timeThreshold;
    @Value("${koTime.pointcut:execution(* cn.langpy.kotime.controller.KoTimeController.*(..))}")
    private String pointcut;
    @Value("${koTime.exception.enable:false}")
    private Boolean exceptionEnable;
    @Value("${koTime.save.saver:memory}")
    private String saveSaver;
    @Value("${koTime.save.async:false}")
    private Boolean saveAsync;
    @Value("${koTime.save.thread-num:4}")
    private Integer threadNum;

    @Resource
    private DefaultConfig defaultConfig;


    @PostConstruct
    public void function() {
        DefaultConfig config = new DefaultConfig();
        config.setLogEnable(defaultConfig.getLogEnable()==null?logEnable:defaultConfig.getLogEnable());
        config.setPointcut(defaultConfig.getPointcut()==null?pointcut:defaultConfig.getPointcut());
        config.setLogLanguage(defaultConfig.getLogLanguage()==null?logLanguage:defaultConfig.getLogLanguage());
        config.setThreshold(defaultConfig.getThreshold()==null?timeThreshold:defaultConfig.getThreshold());
        config.setExceptionEnable(defaultConfig.getExceptionEnable()==null?exceptionEnable:defaultConfig.getExceptionEnable());
        config.setSaveSaver(defaultConfig.getSaveSaver()==null?saveSaver:defaultConfig.getSaveSaver());
        config.setEnable(defaultConfig.getEnable()==null?kotimeEnable:defaultConfig.getEnable());
        config.setContextPath(defaultConfig.getContextPath());
        Context.setConfig(config);
        log.info("kotime=>loading config");
    }

    @Bean
    public AspectJExpressionPointcutAdvisor configurabledvisor() {
        log.info("kotime=>loading method listener");
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(defaultConfig.getPointcut()==null?pointcut:defaultConfig.getPointcut());
        advisor.setAdvice(new RunTimeHandler());
        return advisor;
    }
}
