package me.zhengjie.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.annotation.Query;

import javax.persistence.Column;

/**
 * @author HL
 * @create 2021/12/22 10:36
 */
@Data
public class WorkStepAdjustQueryCriteria {
    @Query
    private String arcCode;

    @Query
    private String invCode;
}
