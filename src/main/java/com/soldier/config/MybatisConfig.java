package com.soldier.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 【请填写功能名称】
 *
 * @author soldier97
 * @date 2022/8/1 23:06
 */
@Configuration
@Slf4j
public class MybatisConfig {

        @Bean
        public MybatisPlusInterceptor mybatisPlusInterceptor(){
            log.info("MP 分页插件");
            MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
            mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
            return mybatisPlusInterceptor;
        }

}
