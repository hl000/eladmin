package me.zhengjie.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author HL
 * @create 2021/4/23 10:57
 */
@Entity
@Data
@Table(name="report_product_parameter")
public class ProductParameter implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id")
    private Integer id;

    /**生产批次号**/
    @Column(name = "product_name")
    @ApiModelProperty(value = "产品名称")
    private String productName;

    /**部件名称**/
    @Column(name = "manufacture_name")
    @ApiModelProperty(value = "部件名称")
    private String manufactureName;


    /**部件数量**/
    @Column(name = "units_quantity")
    @ApiModelProperty(value = "部件数量")
    private Integer unitsQuantity;

    /**工序工人数量**/
    @Column(name = "worker_quantity")
    @ApiModelProperty(value = "工序工人数量")
    private Integer workerQuantity=0;

    /**工时**/
    @Column(name = "work_hours")
    @ApiModelProperty(value = "工时")
    private Double workHours=0.0;

    /**报工权限用户**/
    @Column(name = "permission_user_ids")
    @ApiModelProperty(value = "报工权限用户")
    private String permissionUserIds;

    @JoinColumn(name = "technique_info_id")
    @ManyToOne(fetch=FetchType.EAGER)
    private TechniqueInfo techniqueInfo;

    public void copy(ProductParameter source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
