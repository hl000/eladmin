package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author HL
 * @create 2021/4/20 14:18
 */
@Data
public class ManufactureSummaryQueryCriteria {
    @Query
    private String planNumber;

    @Query
    private Timestamp updateTime;

    @Query
    private String manufactureName;
}
