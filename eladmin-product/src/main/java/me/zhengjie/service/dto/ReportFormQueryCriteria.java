package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author HL
 * @create 2021/10/19 8:51
 */
@Data
public class ReportFormQueryCriteria {
    @Query(type = Query.Type.INNER_LIKE)
    private String manufactureName;

    @Query(type = Query.Type.RIGHT_LIKE)
    private String fillDate;

    @Query
    private String manufactureAddress;
}
