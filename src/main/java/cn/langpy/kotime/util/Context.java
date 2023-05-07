package cn.langpy.kotime.util;

import cn.langpy.kotime.config.DefaultConfig;
import cn.langpy.kotime.handler.InvokedHandler;
import cn.langpy.kotime.model.OrderlyProperties;
import cn.langpy.kotime.service.GraphService;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.sql.DataSource;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * zhangchang
 */
public class Context {

    private static DefaultConfig config;
    private static List<InvokedHandler> invokedHandlers;
    private static DataSource dataSource;
    private static StringRedisTemplate stringRedisTemplate;
    private static GraphService saver;
    private static Properties dynamicProperties;

    static {
        config = new DefaultConfig();
        config.setLogEnable(false);
        config.setEnable(true);
        config.setLogLanguage("chinese");
        invokedHandlers = new ArrayList<>();
        dynamicProperties = new OrderlyProperties();
    }




    public static String getPid() {
        String jvmName = ManagementFactory.getRuntimeMXBean().getName();
        return jvmName.split("@")[0];
    }

    public static StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    public static void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        Context.stringRedisTemplate = stringRedisTemplate;
    }

    public static void setConfig(DefaultConfig koTimeConfig) {
        config = koTimeConfig;
    }

    public static DefaultConfig getConfig() {
        return config;
    }

    public static void addInvokedHandler(InvokedHandler invokedHandler) {
        invokedHandlers.add(invokedHandler);
    }

    public static List<InvokedHandler> getInvokedHandlers() {
        return invokedHandlers;
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static void setDataSource(DataSource dataSource) {
        Context.dataSource = dataSource;
    }


    public static GraphService getSaver() {
        return saver;
    }

    public static void setSaver(GraphService saver) {
        Context.saver = saver;
    }

    public static Properties getDynamicProperties() {
        return dynamicProperties;
    }

    public static void setDynamicProperties(Properties dynamicProperties) {
        Context.dynamicProperties = dynamicProperties;
    }
}
