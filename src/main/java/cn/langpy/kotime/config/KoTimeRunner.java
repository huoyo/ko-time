package cn.langpy.kotime.config;

import cn.langpy.kotime.service.GraphService;
import cn.langpy.kotime.util.Context;
import cn.langpy.kotime.util.KoUtil;
import jakarta.annotation.Resource;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.sql.DataSource;
import java.util.logging.Logger;

/**
 * @author ccz
 * @date 2024-7-5 03:17:13
 */
@ComponentScan("cn.langpy.kotime")
@AutoConfiguration
public class KoTimeRunner implements ApplicationRunner {
    private static Logger log = Logger.getLogger(KoTimeRunner.class.toString());

    @Resource
    private LoadConfig loadConfig;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        loadConfig.initConfig();
        setFinalDataSource();
    }

    private void setFinalDataSource() {
        DataSource dataSource = KoUtil.getDataSource();
        if (null != dataSource) {
            log.info("kotime=>Setting the finnal DataSource for kotime so that previous DataSources will be invalid.");
            Context.setDataSource(dataSource);
        }
        StringRedisTemplate redisTemplate = KoUtil.getStringRedisTemplate();
        if (null != redisTemplate) {
            log.info("kotime=>Setting the finnal StringRedisTemplate for kotime so that previous StringRedisTemplate will be invalid.");
            Context.setStringRedisTemplate(redisTemplate);
        }

        if (Context.getConfig().getDataReset()) {
            log.info("kotime=>Deleting all data for kotime.");
            GraphService instance = GraphService.getInstance();
            instance.clearAll();
        }

        KoUtil.clearCaches();
    }
}
