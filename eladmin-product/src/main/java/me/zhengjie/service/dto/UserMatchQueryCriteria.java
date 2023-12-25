package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.util.Date;

/**
 * @author HL
 * @create 2023/12/13 17:08
 */
@Data
public class UserMatchQueryCriteria {
    @Query
    private String department;

    @Query
    private String process;

    @Query
    private Date date;
}
