package com.fusion;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 融合管理系统平台 - 启动类
 *
 * @author Fusion
 */
@SpringBootApplication
@MapperScan("com.fusion.mapper")
public class FusionApplication {

    public static void main(String[] args) {
        SpringApplication.run(FusionApplication.class, args);
        System.out.println("""

                ============================================================
                  融合管理系统平台 · 后端服务启动成功
                  Fusion Management Platform :: Backend
                  API  :  http://localhost:8080
                  文档 :  http://localhost:8080/doc.html
                ============================================================
                """);
    }
}
