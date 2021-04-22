package me.zhengjie.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author HL
 * @create 2021/4/13 18:23
 */
@Entity
@Data
@Table(name="manufacture")
public class Manufacture implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id")
    private Integer id;

//    @Column(name = "product_code")
//    @ApiModelProperty(value = "产品代码")
//    private String productCode;

    /**生产计划编号**/
    @Column(name = "plan_number")
    @ApiModelProperty(value = "生产计划编号")
    private String planNumber;

    /**工序结存数**/
    @Column(name = "inventory_balance")
    @ApiModelProperty(value = "工序结存数")
    private Integer inventoryBalance;

    /**意外消耗材料1**/
    @Column(name = "unexpected_Material1")
    @ApiModelProperty(value = "意外消耗材料1")
    private Double unexpectedMaterial1;

    /**意外消耗材料2**/
    @Column(name = "unexpected_Material2")
    @ApiModelProperty(value = "意外消耗材料2")
    private Double unexpectedMaterial2;

    /**意外消耗材料3**/
    @Column(name = "unexpected_Material3")
    @ApiModelProperty(value = "意外消耗材料3")
    private Double unexpectedMaterial3;

    /**班组人员数**/
    @Column(name = "worker_quantity")
    @ApiModelProperty(value = "班组人员数")
    private Integer workerQuantity;

    /**工时（含加班）**/
    @Column(name = "working_hours")
    @ApiModelProperty(value = "工时（含加班）")
    private Double workingHours;

    /**日实际产量（含不良品）**/
    @Column(name = "daily_output")
    @ApiModelProperty(value = "日实际产量（含不良品）")
    private Integer dailyOutput;

    /**不良品数量**/
    @Column(name = "rejects_quantity")
    @ApiModelProperty(value = "不良品数量")
    private Integer rejectsQuantity;

    /**废品原因说明**/
    @Column(name = "reject_reasons")
    @ApiModelProperty(value = "废品原因说明")
    private String  rejectReasons;

    /**日产能未完成原因**/
    @Column(name = "incomplete_reasons")
    @ApiModelProperty(value = "日产能未完成原因")
    private String incompleteReasons;

    /**填报日期**/
    @Column(name = "fill_date")
    @ApiModelProperty(value = "填报日期")
    @UpdateTimestamp
    private Timestamp fillDate;


    /**填报用户Id**/
    @Column(name = "user_id")
    @ApiModelProperty(value = "填报用户Id")
    private Long userId;

//    /**生产批次**/
//    @Column(name = "batch_number")
//    @ApiModelProperty(value = "生产批次号")
//    private String batch_number;


    public void copy(Manufacture source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
