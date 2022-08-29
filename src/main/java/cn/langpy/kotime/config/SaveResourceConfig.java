package cn.langpy.kotime.config;

import cn.langpy.kotime.util.Context;
import cn.langpy.kotime.util.KoUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;


@Component
public class SaveResourceConfig implements CommandLineRunner{
    @Override
    public void run(String... args) throws Exception {
        DataSource dataSource = KoUtil.getDataSource();
        if (null!=dataSource) {
            Context.setDataSource(dataSource);
        }
        StringRedisTemplate redisTemplate = KoUtil.getStringRedisTemplate();
        if (null!=redisTemplate) {
            Context.setStringRedisTemplate(redisTemplate);
        }

        KoUtil.clearCaches();
    }
}
