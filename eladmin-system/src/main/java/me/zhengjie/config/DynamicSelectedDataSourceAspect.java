package me.zhengjie.config;

import me.zhengjie.annotation.DatabaseType;
import me.zhengjie.annotation.DynamicDao;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DynamicSelectedDataSourceAspect {


    @Pointcut("execution(* me.zhengjie.mapper.*Mapper.*(..))")
    public void dataSourcePointcut() {
    }

    @Before("dataSourcePointcut()")
    public void selectDataSource(JoinPoint joinPoint) {
        System.out.println("-------------> selected dataSource aspect ");
        Signature signature = joinPoint.getSignature();
        Class mapper = signature.getDeclaringType();
        DynamicDao annos = (DynamicDao) mapper.getAnnotation(DynamicDao.class);
        if (null == annos) {
            DatabaseContextHolder.setDatabaseType(DatabaseType.mybatis);
            System.out.println("-------------> selected First dataSource");
        } else if (annos.type() == DatabaseType.second) {
            DatabaseContextHolder.setDatabaseType(DatabaseType.second);
            System.out.println("-------------> selected Second dataSource");
        }else if(annos.type() == DatabaseType.jpaDatasource){
            DatabaseContextHolder.setDatabaseType(DatabaseType.jpaDatasource);
            System.out.println("-------------> selected Mysql dataSource");
        }
    }
}