package cn.langpy.kotime.config;

import cn.langpy.kotime.annotation.KoListener;
import cn.langpy.kotime.handler.InvokedHandler;
import cn.langpy.kotime.service.GraphService;
import cn.langpy.kotime.service.InvokedQueue;
import cn.langpy.kotime.util.Common;
import cn.langpy.kotime.util.Context;
import cn.langpy.kotime.util.DataBaseException;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * zhangchang
 * Note:
 * I deleted the oldest configurations that start with koTime.xxx since v3.0.0,such as koTime.pointcut and koTime.log.*
 */
@Component
public class LoadConfig {
    private static Logger log = Logger.getLogger(LoadConfig.class.toString());

    @Value("${server.port:8080}")
    private Integer serverPort;
    @Value("${server.servlet.context-path:}")
    private String serverContext;

    @Value("${ko-time.mail-enable:false}")
    private Boolean mailEnable;

    @Resource
    private DefaultConfig defaultConfig;
    @Resource
    private ApplicationContext applicationContext;

    public void initConfig() {
        DefaultConfig config = improveConfig();

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

        loadPropertyFile();
    }

    public DefaultConfig improveConfig() {
        DefaultConfig config = new DefaultConfig();
        config.setLogEnable( defaultConfig.getLogEnable());
        config.setThreshold(defaultConfig.getThreshold());
        config.setExceptionEnable( defaultConfig.getExceptionEnable());
        config.setSaver(defaultConfig.getSaver());
        config.setEnable( defaultConfig.getEnable());
        config.setDataPrefix(defaultConfig.getDataPrefix() == null ? (StringUtils.hasText(serverContext) ? serverContext.substring(1) : "KOTIME") : defaultConfig.getDataPrefix());
        config.setContextPath(defaultConfig.getContextPath());
        config.setLanguage(defaultConfig.getLanguage() == null ? "chinese" : defaultConfig.getLanguage());
        config.setThreadNum(defaultConfig.getThreadNum() == null ? 2 : defaultConfig.getThreadNum());
        config.setDiscardRate(defaultConfig.getDiscardRate() == null ? 0.3 : defaultConfig.getDiscardRate());
        config.setAuthExpire(defaultConfig.getAuthExpire() == null ? (12 * 60 * 60l) : defaultConfig.getAuthExpire());
        config.setAuthEnable(defaultConfig.getAuthEnable() == null ? false : defaultConfig.getAuthEnable());
        config.setParamAnalyse(defaultConfig.getParamAnalyse() == null ? true : defaultConfig.getParamAnalyse());
        config.setDataReset(defaultConfig.getDataReset() == null ? false : defaultConfig.getDataReset());
        config.setVersionNotice(defaultConfig.getVersionNotice() == null ? true : defaultConfig.getVersionNotice());
        config.setStaticToken(defaultConfig.getStaticToken());
        config.setAbbreviationEnable(defaultConfig.getAbbreviationEnable() == null ? true : defaultConfig.getAbbreviationEnable());

        config.setMailEnable(defaultConfig.getMailEnable());
        config.setMailProtocol(defaultConfig.getMailProtocol() == null ? "smtp" : defaultConfig.getMailProtocol());
        config.setMailHost(defaultConfig.getMailHost() == null ? "smtp.qq.com" : defaultConfig.getMailHost());
        config.setMailPort(defaultConfig.getMailPort() == null ? 587 : defaultConfig.getMailPort());
        config.setMailEncoding(defaultConfig.getMailEncoding() == null ? "UTF-8" : defaultConfig.getMailEncoding());
        config.setMailThreshold(defaultConfig.getMailThreshold() == null ? 4 : defaultConfig.getMailThreshold());
        config.setMailScope(defaultConfig.getMailScope() == null ? "Controller" : defaultConfig.getMailScope());
        config.setMailUser(defaultConfig.getMailUser());
        config.setMailCode(defaultConfig.getMailCode());
        config.setMailReceivers(defaultConfig.getMailReceivers());
        config.setPropertyFile(defaultConfig.getPropertyFile() == null ? "dynamic.properties" : defaultConfig.getPropertyFile());
        return config;
    }

    public void loadPropertyFile() {
        ClassPathResource classPathResource = new ClassPathResource(Context.getConfig().getPropertyFile());
        try (
                InputStream inputStream = classPathResource.getInputStream()
               ) {
            Context.getDynamicProperties().load(inputStream);
        } catch (UnsupportedEncodingException e) {
            log.severe("kotime=>dynamic.properties requires utf-8.");
            e.printStackTrace();
        } catch (FileNotFoundException e){
            log.warning("kotime=>No dynamic.properties found so that you can not use dynamic properties to set.");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void configDataSource(DefaultConfig config) {
        if (!"database".equals(config.getSaver())) {
            return;
        }
        try {
            DataSource dataSource = applicationContext.getBean(DataSource.class);
            Context.setDataSource(dataSource);
        } catch (NoUniqueBeanDefinitionException e) {
            if (Common.isEmpty(config.getDataSource())) {
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
            if (!Common.isEmpty(config.getRedisTemplate())) {
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

}
