package cn.langpy.kotime.util;

import cn.langpy.kotime.config.DefaultConfig;
import cn.langpy.kotime.handler.InvokedHandler;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * zhangchang
 */
public class Context {

    private static DefaultConfig config;
    private static List<InvokedHandler> invokedHandlers;
    private static ThreadPoolExecutor koThreadPool;
    private static DataSource dataSource;

    static {
        config = new DefaultConfig();
        config.setLogEnable(false);
        config.setEnable(true);
        config.setLogLanguage("chinese");
        invokedHandlers = new ArrayList<>();
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

    public static ThreadPoolExecutor getKoThreadPool() {
        return koThreadPool;
    }

    public static void setKoThreadPool(ThreadPoolExecutor koThreadPool) {
        Context.koThreadPool = koThreadPool;
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static void setDataSource(DataSource dataSource) {
        Context.dataSource = dataSource;
    }
}
