package com.soldier;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Slf4j
@SpringBootApplication
@MapperScan("com.soldier.dao")
public class TakeawayApplication {

    public static void main(String[] args) {
        SpringApplication.run(TakeawayApplication.class, args);
        log.info("项目启动成功");
    }

}
