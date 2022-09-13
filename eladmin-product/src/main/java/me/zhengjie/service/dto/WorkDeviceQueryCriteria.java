package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author HL
 * @create 2022/9/2 15:53
 */
@Data
public class WorkDeviceQueryCriteria {

    @Query
    private Integer isActive = 1;

    @Query
    private String deviceType;
}
