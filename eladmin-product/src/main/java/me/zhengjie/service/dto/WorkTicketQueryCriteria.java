package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author HL
 * @create 2023/12/13 17:08
 */
@Data
public class WorkTicketQueryCriteria {

//    @Query
//    private String createdBy;
//
//    @Query
//    private String department;
//
//    @Query
//    private String process;

    @Query
    private String date;
}
