package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.util.List;

/**
 * @author HL
 * @create 2023/12/13 17:08
 */
@Data
public class WorkTicketDingQueryCriteria {

    @Query
    private String createdBy;

    @Query
    private String department;

    @Query
    private String process;

    @Query(type = Query.Type.BETWEEN)
    private List<String> date;
}
