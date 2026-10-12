package com.fusion.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 接口文档配置（Knife4j / OpenAPI3）
 *
 * @author Fusion
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI fusionOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("融合管理系统平台 · API 文档")
                .description("SpringBoot 3 + MyBatis-Plus + Vue3 前后端分离脚手架")
                .version("1.0.0")
                .contact(new Contact().name("Fusion Platform")));
    }
}
