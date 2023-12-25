package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author HL
 * @create 2023/9/1 9:28
 */
@Data
public class InventoryQueryCriteria {
    //料品编码
    @Query
    private String code;

    //料品名称
    @Query
    private String name;

    //料品规格
    @Query
    private String specs;

}
