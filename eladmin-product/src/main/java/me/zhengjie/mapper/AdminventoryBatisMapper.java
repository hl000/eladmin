package me.zhengjie.mapper;


import me.zhengjie.service.dto.AdminventoryDto;
import me.zhengjie.service.dto.AdminventoryQueryCriteria;

import java.util.List;

public interface AdminventoryBatisMapper {
    int deleteByPrimaryKey(Integer fid);

    int insert(AdminventoryDto record);

    int insertSelective(AdminventoryDto record);

    AdminventoryDto selectByPrimaryKey(Integer fid);

    int updateByPrimaryKeySelective(AdminventoryDto record);

    int updateByPrimaryKey(AdminventoryDto record);

    List<AdminventoryDto> getAdminventory(AdminventoryQueryCriteria criteria);

}
