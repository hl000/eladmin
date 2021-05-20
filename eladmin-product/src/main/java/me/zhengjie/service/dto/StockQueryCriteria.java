package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author HL
 * @create 2021/5/19 13:55
 */

@Data
public class StockQueryCriteria {
    @Query
    private String manufactureAddress;
    @Query
    private String processName;
    @Query
    private String manufactureName;
}

