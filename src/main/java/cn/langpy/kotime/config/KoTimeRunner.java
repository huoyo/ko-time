package cn.langpy.kotime.config;

import jakarta.annotation.Resource;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author ccz
 * @date 2024-7-5 03:17:13
 */
@ComponentScan("cn.langpy.kotime")
@AutoConfiguration
public class KoTimeRunner implements ApplicationRunner {
    @Resource
    private LoadConfig loadConfig;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        loadConfig.initConfig();
    }
}
