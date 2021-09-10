package cn.langpy.kotime.util;

import cn.langpy.kotime.config.DefaultConfig;

/**
 * zhangchang
 */
public class Context {

    private static DefaultConfig config;

    static {
        config = new DefaultConfig();
        config.setLogEnable(false);
        config.setEnable(true);
        config.setLogLanguage("chinese");
    }

    public static void setConfig(DefaultConfig koTimeConfig) {
        config = koTimeConfig;
    }

    public static DefaultConfig getConfig() {
        return config;
    }

}
