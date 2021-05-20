package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;

/**
 * @author HL
 * @create 2021/4/23 10:57
 */
@Data
public class ProductParameterQueryCriteria implements Serializable {

    @Query(type = Query.Type.RIGHT_LIKE)
    private String productName;

    /**部件名称**/
    @Query(type = Query.Type.RIGHT_LIKE)
    private String manufactureName;

    @Query
    private Integer serialNumber;
}
