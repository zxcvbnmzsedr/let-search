package com.shiyisoushu.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 启动器
 *
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019/10/22 10:07 上午
 */
@SpringBootApplication(scanBasePackages = "com.shiyisoushu.*")
@EntityScan({"com.shiyisoushu.repo.mysql.model"})
// 开启redis session存储，不至于一重启就退出登录
@EnableRedisHttpSession(redisNamespace = "account", maxInactiveIntervalInSeconds = 86400)
@EnableCaching
@EnableJpaRepositories({"com.shiyisoushu.repo.mysql"})
@EnableAsync
public class SearchApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SearchApplication.class);
        application.run(args);
    }
}