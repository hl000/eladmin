package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author HL
 * @create 2022/9/2 10:02
 */
@Data
public class ElectricPipeActivationDetailQueryCriteria {

    @Query(propName = "id", joinName = "electricPipeActivation")
    private Integer electricPipeActivationId;
}
