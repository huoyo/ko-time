package cn.langpy.kotime.util;

import cn.langpy.kotime.config.DefaultConfig;
import cn.langpy.kotime.service.InvokedHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * zhangchang
 */
public class Context {

    private static DefaultConfig config;
    private static List<InvokedHandler> invokedHandlers;
    private static ThreadPoolExecutor threadPoolExecutor;

    static {
        config = new DefaultConfig();
        config.setLogEnable(false);
        config.setEnable(true);
        config.setLogLanguage("chinese");
        invokedHandlers = new ArrayList<>();
        threadPoolExecutor = new ThreadPoolExecutor(10, 1000,60L, TimeUnit.SECONDS,new SynchronousQueue<Runnable>());
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

    public static ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public static void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        Context.threadPoolExecutor = threadPoolExecutor;
    }
}
