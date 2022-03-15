package me.zhengjie.mapper;

import me.zhengjie.annotation.DatabaseType;
import me.zhengjie.annotation.DynamicDao;

import java.util.List;

/**
 * @author HL
 * @create 2022/1/14 10:16
 */
@DynamicDao(type = DatabaseType.jpaDatasource)
public interface SystemUserMapper {

    List<String> queryDutyPerson();
}
