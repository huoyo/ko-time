package cn.langpy.kotime.util;

import cn.langpy.kotime.model.KoTimeConfig;

public class Context {

    private static KoTimeConfig config;

    static {
        config = new KoTimeConfig();
        config.setLogEnable(false);
        config.setKotimeEnable(true);
        config.setLogLanguage("chinese");
    }

    public static void setConfig(KoTimeConfig koTimeConfig) {
        config = koTimeConfig;
    }

    public static KoTimeConfig getConfig() {
        return config;
    }

}
