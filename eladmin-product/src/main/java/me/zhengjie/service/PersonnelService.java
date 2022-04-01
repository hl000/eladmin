package me.zhengjie.service;

import com.taobao.api.ApiException;
import me.zhengjie.domain.DingDepartment;
import me.zhengjie.domain.DingUser;
import me.zhengjie.service.dto.DingUserDto;

import java.util.List;

/**
 * @author HL
 * @create 2022/3/15 20:33
 */
public interface PersonnelService {
    void saveDepartmentAndUser(List<DingDepartment> dingDepartments, List<DingUser> dingUserList);

    void getListSubId() throws ApiException;

    List<DingUserDto> getUserList();
}
