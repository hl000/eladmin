package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author HL
 * @create 2021/5/29 10:20
 */
@Data
public class MaterialRatioQueryCriteria {

    @Query(propName = "manufactureName",joinName = "productParameter")
    private String manufactureName;

    @Query(propName = "manufactureName",joinName = "material")
    private String materialName;
}
