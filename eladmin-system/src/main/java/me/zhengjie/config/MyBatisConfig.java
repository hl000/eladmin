package me.zhengjie.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "me.zhengjie.mapper", sqlSessionFactoryRef = "myBatisSqlSessionFactory") // basePackages mapper所在的包名
public class MyBatisConfig {
    @Bean(name = "myBatisSqlSessionFactory")
    public SqlSessionFactory clusterSqlSessionFactory(@Qualifier("mybatisDatasource")DataSource mybatisDatasource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(mybatisDatasource);
        org.springframework.core.io.Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources("classpath*:mappers/**/*Mapper.xml");  //MyBatis XML文件所在路径
        sessionFactory.setMapperLocations(resources);
        return sessionFactory.getObject();
    }
}
