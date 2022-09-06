package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author HL
 * @create 2021/5/11 15:53
 */
@Data
public class UpdateStackDtoQueryCriteria {
    @Query
    private String FCHEPAI;

    @Query
    private String FDATE;
}
