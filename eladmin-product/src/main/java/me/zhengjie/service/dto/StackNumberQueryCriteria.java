package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author HL
 * @create 2022/9/14 10:46
 */
@Data
public class StackNumberQueryCriteria {

    @Query(type = Query.Type.INNER_LIKE)
    private String stackNumber;

}
