package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author HL
 * @create 2021/6/7 9:47
 */
@Data
public class ProductionReportDetailsCriteria {

    @Query
    private String vSubmitDate;

    @Query(type = Query.Type.INNER_LIKE)
    private String vArcName;

    @Query(type = Query.Type.INNER_LIKE)
    private String vInvName;

    @Query(type = Query.Type.INNER_LIKE)
    private String vSpec;
}
