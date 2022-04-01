package me.zhengjie.mapper;

import me.zhengjie.annotation.DatabaseType;
import me.zhengjie.annotation.DynamicDao;
import me.zhengjie.domain.DingDepartment;
import me.zhengjie.domain.DingUser;
import me.zhengjie.service.dto.DingUserDto;

import java.util.List;

/**
 * @author HL
 * @create 2022/3/15 20:37
 */
@DynamicDao(type = DatabaseType.second)
public interface PersonnelMapper {
    void insertDepartment(DingDepartment dingDepartment);

    List<DingDepartment> findAllDepartment();

    void insertUser(DingUser dingUser);

    List<DingUser> findAllUser();

    List<DingUserDto> getUserList();
}
