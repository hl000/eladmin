package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author HL
 * @create 2021/7/14 23:47
 */
@Data
public class ManufactureOrderQueryCriteria {

    @Query
    private String stackNumber;

    @Query
    private Integer pitchNumber;

    @Query
    private String FBIP;

    @Query
    private String assemblyPersonnel;
}
