package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author HL
 * @create 2022/8/30 15:07
 */
@Data
public class ElectricPipeActivationQueryCriteria {

    @Query(propName = "stackNumber", joinName = "manufactureOrder")
    private String stackNumber;

    private Integer activeTimes;
}
