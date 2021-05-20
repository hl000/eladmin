package me.zhengjie.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.domain.TechniqueInfo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author HL
 * @create 2021/4/23 10:57
 */
@Data
public class ProductParameterDto implements Serializable {

    private Integer id;

    /**产品名称**/
    private String productName;

    /**部件名称**/
    private String manufactureName;

    /**部件数量**/
    private Integer unitsQuantity;

    /**工序工人数量**/
    private Integer workerQuantity;

    /**工时**/
    private Double workHours;

    private String permissionUserIds;

    private TechniqueInfo techniqueInfo;

    private Integer serialNumber;

}
