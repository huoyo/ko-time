package cn.langpy.kotime.util;

import cn.langpy.kotime.config.DefaultConfig;
import cn.langpy.kotime.handler.InvokedHandler;
import cn.langpy.kotime.service.GraphService;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.sql.DataSource;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * zhangchang
 */
public class Context {

    private static DefaultConfig config;
    private static List<InvokedHandler> invokedHandlers;
    private static DataSource dataSource;
    private static StringRedisTemplate stringRedisTemplate;
    private static GraphService saver;
    private static Map<String,String> dynamicProperties;

    static {
        config = new DefaultConfig();
        config.setLogEnable(false);
        config.setEnable(true);
        config.setLogLanguage("chinese");
        invokedHandlers = new ArrayList<>();
        dynamicProperties = new ConcurrentHashMap<>();
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

    public static Map<String, String> getDynamicProperties() {
        return dynamicProperties;
    }

    public static void setDynamicProperties(Map<String, String> dynamicProperties) {
        Context.dynamicProperties = dynamicProperties;
    }
}
