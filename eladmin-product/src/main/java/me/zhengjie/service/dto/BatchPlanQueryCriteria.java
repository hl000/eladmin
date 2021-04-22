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

    @Query(propName = "manufactureName",joinName = "techniqueInfo")
    private String manufactureName;

    @Query(propName = "productCode",joinName = "techniqueInfo")
    private String productCode;

    @Query(type = Query.Type.GREATER_THAN )
    private String startDate;

    @Query(type = Query.Type.LESS_THAN )
    private String endDate;

}
