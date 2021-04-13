package me.zhengjie.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @ConfigurationProperties(prefix = "spring.datasource.druid")
    @Bean("jpaDatasource")
    @Primary  //该注解表明该数据源是项目默认使用的
    public DataSource jpaDatasource(){
        return new DruidDataSource();
    }

    @ConfigurationProperties(prefix = "spring.datasource.mybatis")
    @Bean("mybatisDatasource")
    public DataSource mybatisDatasource(){
        return new DruidDataSource();
    }
}
