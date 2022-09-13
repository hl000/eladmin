package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author HL
 * @create 2022/9/13 17:47
 */
@Data
public class WorkGroupQueryCriteria {
    @Query
    private Integer isActive = 1;

    @Query
    private String groupCode;

    @Query
    private String groupNumber;

    @Query
    private String groupName;
}
