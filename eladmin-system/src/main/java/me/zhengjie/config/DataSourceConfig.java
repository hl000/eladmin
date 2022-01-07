package me.zhengjie.config;

import com.alibaba.druid.pool.DruidDataSource;
import me.zhengjie.annotation.DatabaseType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    @ConfigurationProperties(prefix = "spring.datasource.druid")
    @Bean("jpaDatasource")
    @Primary  //该注解表明该数据源是项目默认使用的
    public DataSource jpaDatasource() {
        return new DruidDataSource();
    }

    @ConfigurationProperties(prefix = "spring.datasource.mybatis")
    @Bean("mybatisDatasource")
    public DataSource mybatisDatasource() {
        return new DruidDataSource();
    }

    @ConfigurationProperties(prefix = "spring.datasource.second")
    @Bean("secondDatasource")
    public DataSource secondDatasource() {
        return new DruidDataSource();
    }

    @Bean
    public DynamicDataSource dataSource(@Qualifier("mybatisDatasource") DataSource mybatisDatasource,
                                        @Qualifier("secondDatasource") DataSource secondDatasource,
                                        @Qualifier("jpaDatasource") DataSource jpaDatasource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DatabaseType.mybatis, mybatisDatasource);
        targetDataSources.put(DatabaseType.second, secondDatasource);
        targetDataSources.put(DatabaseType.jpaDatasource, jpaDatasource);
        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSources);// 该方法是AbstractRoutingDataSource的方法
        dataSource.setDefaultTargetDataSource(mybatisDatasource);// 默认的datasource设置为myTestDbDataSource

        return dataSource;
    }


//    @Bean
//    public SqlSessionFactory sqlSessionFactory(DynamicDataSource ds) throws Exception {
//        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
//        fb.setDataSource(ds);// 指定数据源(这个必须有，否则报错)
//        fb.setTypeAliasesPackage("me.zhengjie.mapper");// 指定基包
//        fb.setMapperLocations(
//                new PathMatchingResourcePatternResolver().getResources("classpath*:mappers/**/*Mapper.xml"));//
//
//        return fb.getObject();
//    }

    /**
     * 配置事务管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DynamicDataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }
}
