package com.beanions.config;


import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.beanions")
@MapperScan(basePackages = "com.beanions", annotationClass = Mapper.class)
public class BeanionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeanionsApplication.class, args);
    }

}
