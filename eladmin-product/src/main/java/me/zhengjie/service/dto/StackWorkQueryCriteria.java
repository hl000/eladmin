package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.sql.Timestamp;

/**
 * @author HL
 * @create 2021/6/7 9:47
 */
@Data
public class StackWorkQueryCriteria {

    @Query
    private String fillDate;
}
