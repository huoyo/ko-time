package cn.langpy.kotime.config;

import cn.langpy.kotime.annotation.KoListener;
import cn.langpy.kotime.handler.RunTimeHandler;
import cn.langpy.kotime.handler.InvokedHandler;
import cn.langpy.kotime.service.EmailSendService;
import cn.langpy.kotime.service.GraphService;
import cn.langpy.kotime.service.InvokedQueue;
import cn.langpy.kotime.util.Context;
import cn.langpy.kotime.util.DataBaseException;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * zhangchang
 */
@ComponentScan("cn.langpy.kotime")
@Configuration
public class LoadConfig {
    private static Logger log = Logger.getLogger(LoadConfig.class.toString());

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
    @Value("${server.port:8080}")
    private Integer serverPort;
    @Value("${server.servlet.context-path:}")
    private String serverContext;

    @Value("${ko-time.mail-host:smtp.qq.com}")
    private String host;
    @Value("${ko-time.mail-port:587}")
    private Integer port;
    @Value("${ko-time.mail-protocol:smtp}")
    private String protocol;
    @Value("${ko-time.mail-encoding:UTF-8}")
    private String encoding;
    @Value("${ko-time.mail-user:}")
    private String user;
    @Value("${ko-time.mail-code:}")
    private String mailCode;
    @Value("${ko-time.mail-enable:false}")
    private Boolean mailEnable;

    @Resource
    private DefaultConfig defaultConfig;
    @Resource
    private ApplicationContext applicationContext;


    @PostConstruct
    public void initConfig() {
        DefaultConfig config = new DefaultConfig();
        config.setLogEnable(defaultConfig.getLogEnable() == null ? logEnable : defaultConfig.getLogEnable());
        config.setLogLanguage(defaultConfig.getLogLanguage() == null ? logLanguage : defaultConfig.getLogLanguage());
        config.setThreshold(defaultConfig.getThreshold() == null ? timeThreshold : defaultConfig.getThreshold());
        config.setExceptionEnable(defaultConfig.getExceptionEnable() == null ? exceptionEnable : defaultConfig.getExceptionEnable());
        config.setSaver(defaultConfig.getSaver() == null ? saveSaver : defaultConfig.getSaver());
        config.setEnable(defaultConfig.getEnable() == null ? kotimeEnable : defaultConfig.getEnable());
        config.setDataPrefix(defaultConfig.getDataPrefix() == null ? (StringUtils.hasText(serverContext)?serverContext.substring(1):"KOTIME") : defaultConfig.getDataPrefix());
        config.setContextPath(defaultConfig.getContextPath());
        config.setLanguage(defaultConfig.getLanguage() == null ? "chinese" : defaultConfig.getLanguage());
        config.setThreadNum(defaultConfig.getThreadNum() == null ? 2 : defaultConfig.getThreadNum());
        config.setDiscardRate(defaultConfig.getDiscardRate() == null ? 0.3 : defaultConfig.getDiscardRate());
        config.setAuthEnable(defaultConfig.getAuthEnable() == null ? false : defaultConfig.getAuthEnable());
        config.setParamAnalyse(defaultConfig.getParamAnalyse() == null ? true : defaultConfig.getParamAnalyse());
        config.setDataReset(defaultConfig.getDataReset() == null ? false : defaultConfig.getDataReset());
        configDataSource(config);
        configRedisTemplate(config);
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
        if (null == Context.getSaver()) {
            throw new DataBaseException("error `ko-time.saver=" + config.getSaver() + "`, and you can only choose an option in {memory,database,redis} for `ko-time.saver=`!");
        }
        log.info("kotime=>loading config");

        if (StringUtils.hasText(config.getContextPath())) {
            log.info("kotime=>view:" + Context.getConfig().getContextPath() + "/koTime");
        } else {
            log.info("kotime=>view:http://localhost:" + serverPort + serverContext + "/koTime");
        }
        initMethodHandlers();
    }

    @Bean
    @Lazy
    public EmailSendService emailSendService() {
        EmailSendService sender = new EmailSendService();
        sender.setHost(host);
        sender.setPort(port);
        sender.setUsername(user);
        sender.setPassword(mailCode);
        sender.setProtocol(protocol);
        sender.setDefaultEncoding(encoding);
        return sender;
    }

    public void configDataSource(DefaultConfig config) {
        if (!"database".equals(config.getSaver())) {
            return;
        }
        try {
            DataSource dataSource = applicationContext.getBean(DataSource.class);
            Context.setDataSource(dataSource);
        } catch (NoUniqueBeanDefinitionException e) {
            if (StringUtils.isEmpty(config.getDataSource())) {
                log.severe("kotime=>No unique bean of type 'DataSource' available,you can define it by `ko-time.data-source=xxx`");
            } else {
                DataSource dataSource = applicationContext.getBean(config.getDataSource(), DataSource.class);
                Context.setDataSource(dataSource);
            }
        } catch (NoSuchBeanDefinitionException e) {
            log.severe("kotime=>No qualifying bean of type 'DataSource' available,but you can ignore it if your KoTime saver is `ko-time.saver=memory`");
        }
    }

    public void configRedisTemplate(DefaultConfig config) {
        if (!"redis".equals(config.getSaver())) {
            return;
        }
        try {
            StringRedisTemplate redisTemplate = applicationContext.getBean(StringRedisTemplate.class);
            Context.setStringRedisTemplate(redisTemplate);
        } catch (NoUniqueBeanDefinitionException e) {
            if (!StringUtils.isEmpty(config.getRedisTemplate())) {
                StringRedisTemplate redisTemplate = applicationContext.getBean(config.getRedisTemplate(), StringRedisTemplate.class);
                Context.setStringRedisTemplate(redisTemplate);
            } else {
                Map<String, StringRedisTemplate> beansOfType = applicationContext.getBeansOfType(StringRedisTemplate.class);
                log.warning("kotime=>No unique bean of type 'StringRedisTemplate' available,you can define it by `ko-time.redis-template=xxx`,and you can choose a name in " + beansOfType.keySet().stream().collect(Collectors.toList()));
                log.warning("kotime=>Now the firsr was be set.");
                Context.setStringRedisTemplate(beansOfType.values().stream().collect(Collectors.toList()).get(0));
            }

        } catch (NoSuchBeanDefinitionException e) {
            log.severe("kotime=>No qualifying bean of type 'StringRedisTemplate' available,but you can ignore it if your KoTime saver is `ko-time.saver=memory`");
        } catch (NoClassDefFoundError error) {
            log.severe("kotime=>No dependency named `spring-boot-starter-data-redis` found,please add a denpendency in pom.xml for redis.");
        }
    }

    public void initMethodHandlers() {
        String[] names = applicationContext.getBeanNamesForType(InvokedHandler.class);
        for (String name : names) {
            if ("emailHandler".equals(name) && !mailEnable) {
                continue;
            }
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
            new Thread(() -> InvokedQueue.onInveked()).start();
        }
    }


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
