package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author HL
 * @create 2023/12/13 17:08
 */
@Data
public class WorkTicketManagerQueryCriteria {

    @Query
    private String username;
}
