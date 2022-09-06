package me.zhengjie.service.dto;

import lombok.Data;

/**
 * @author HL
 * @create 2022/9/2 15:53
 */
@Data
public class WorkDeviceQueryCriteria {

    private Integer isActive = 1;

    private String deviceType;
}
