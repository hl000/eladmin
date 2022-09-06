package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.util.List;

/**
 * @author HL
 * @create 2021/5/11 15:53
 */
@Data
public class UpdateStackQueryCriteria {
    @Query
    private String FCHEPAI;

    @Query(type = Query.Type.BETWEEN)
    private List<String> FDATE;

}
