package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author HL
 * @create 2021/4/13 15:09
 */
@Data
public class BatchPlanQueryCriteria {
    @Query
    private String batchNumber;

    @Query
    private Long userId;
//    @Query
//    private String productCode;
}
