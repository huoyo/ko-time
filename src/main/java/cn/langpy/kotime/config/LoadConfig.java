package cn.langpy.kotime.config;

import cn.langpy.kotime.annotation.KoListener;
import cn.langpy.kotime.handler.RunTimeHandler;
import cn.langpy.kotime.handler.InvokedHandler;
import cn.langpy.kotime.service.GraphService;
import cn.langpy.kotime.service.InvokedQueue;
import cn.langpy.kotime.util.Context;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.logging.Logger;

/**
 * zhangchang
 */
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
    @Value("${koTime.saver:memory}")
    private String saveSaver;
    @Value("${koTime.thread-num:2}")
    private Integer threadNum;
    @Value("${server.port:8080}")
    private Integer serverPort;
    @Value("${server.servlet.context-path:}")
    private String serverContext;

    @Resource
    private DefaultConfig defaultConfig;
    @Resource
    private ApplicationContext applicationContext;


    @PostConstruct
    public void initConfig() {
        DefaultConfig config = new DefaultConfig();
        config.setLogEnable(defaultConfig.getLogEnable() == null ? logEnable : defaultConfig.getLogEnable());
        config.setPointcut(defaultConfig.getPointcut() == null ? pointcut : defaultConfig.getPointcut());
        config.setLogLanguage(defaultConfig.getLogLanguage() == null ? logLanguage : defaultConfig.getLogLanguage());
        config.setThreshold(defaultConfig.getThreshold() == null ? timeThreshold : defaultConfig.getThreshold());
        config.setExceptionEnable(defaultConfig.getExceptionEnable() == null ? exceptionEnable : defaultConfig.getExceptionEnable());
        config.setSaver(defaultConfig.getSaver() == null ? saveSaver : defaultConfig.getSaver());
        config.setEnable(defaultConfig.getEnable() == null ? kotimeEnable : defaultConfig.getEnable());
        config.setContextPath(defaultConfig.getContextPath());
        config.setThreadNum(defaultConfig.getThreadNum() == null ? 2 : defaultConfig.getThreadNum());
        config.setAuthEnable(defaultConfig.getAuthEnable() == null ? false : defaultConfig.getAuthEnable());
        config.setParamAnalyse(defaultConfig.getParamAnalyse() == null ? true : defaultConfig.getParamAnalyse());
        if (null != config) {
            config.setPointcut("(" + config.getPointcut() + " ) && !@annotation(javax.websocket.server.ServerEndpoint) && !@annotation(cn.langpy.kotime.annotation.KoListener)");
        }
        try {
            DataSource dataSource = applicationContext.getBean(DataSource.class);
            Context.setDataSource(dataSource);
        }catch (NoUniqueBeanDefinitionException e){
            if (StringUtils.isEmpty(config.getDataSource())) {
                log.warning("kotime=>No unique bean of type 'DataSource' available,you can define it by `ko-time.data-source=xxx`");
            }else {
                DataSource dataSource = applicationContext.getBean(config.getDataSource(),DataSource.class);
                Context.setDataSource(dataSource);
            }
        }catch (NoSuchBeanDefinitionException e){
            log.warning("kotime=>No qualifying bean of type 'DataSource' available,you can ignore it if your KoTime saver is `ko-time.saver=memory`");
        }

        Context.setConfig(config);
        String[] names = applicationContext.getBeanNamesForType(GraphService.class);
        for (String name : names) {
            GraphService bean = (GraphService) applicationContext.getBean(name);
            if (null != bean) {
                Component annotation = bean.getClass().getAnnotation(Component.class);
                if (config.getSaver().equals(annotation.value())) {
                    Context.setSaver(bean);
                    break;
                }
            }
        }
        log.info("kotime=>loading config");

        if (StringUtils.hasText(config.getContextPath())) {
            log.info("kotime=>view:" + Context.getConfig().getContextPath() + "/koTime");
        } else {
            log.info("kotime=>view:http://localhost:" + serverPort + serverContext + "/koTime");
        }
        initMethodHandlers();
    }

    public void initMethodHandlers() {
        String[] names = applicationContext.getBeanNamesForType(InvokedHandler.class);
        for (String name : names) {
            InvokedHandler bean = (InvokedHandler) applicationContext.getBean(name);
            if (null != bean) {
                KoListener annotation = bean.getClass().getAnnotation(KoListener.class);
                if (null == annotation) {
                    continue;
                }
                log.info("kotime=>loading InvokedHandler:" + bean.getClass().getSimpleName());
                Context.addInvokedHandler(bean);
            }
        }
        for (int i = 0; i < Context.getConfig().getThreadNum(); i++) {
            new Thread(()->InvokedQueue.onInveked()).start();
        }
    }


    @Bean
    public AspectJExpressionPointcutAdvisor configurabledvisor() {
        log.info("kotime=>loading method listener");
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(defaultConfig.getPointcut() == null ? pointcut : defaultConfig.getPointcut());
        advisor.setAdvice(new RunTimeHandler());
        return advisor;
    }
}
