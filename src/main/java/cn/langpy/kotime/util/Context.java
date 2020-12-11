package cn.langpy.kotime.util;
import cn.langpy.kotime.model.KoTimeConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Context {

    private static KoTimeConfig config;
    static {
        config = new KoTimeConfig();
        config.setLogEnable(false);
        config.setLogLanguage("chinese");
    }

    public static void setConfig(KoTimeConfig koTimeConfig) {
        config = koTimeConfig;
    }

    public static KoTimeConfig getConfig() {
        return config ;
    }

}
