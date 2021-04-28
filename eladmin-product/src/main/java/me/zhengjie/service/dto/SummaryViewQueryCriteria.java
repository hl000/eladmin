package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.sql.Timestamp;

/**
 * @author HL
 * @create 2021/4/20 14:18
 */
@Data
public class SummaryViewQueryCriteria {
    @Query
    private String baNum;

    @Query
    private Timestamp sumDate;

    @Query
    private String proName;
}
