package me.zhengjie.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author HL
 * @create 2021/4/14 14:02
 */
@Entity
@Data
@Table(name="batch_plan")
public class BatchPlan implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id")
    private Integer id;

//    @Column(name="technique_info_id")
//    private Integer techniqueInfoId;

    /**生产批次号**/
    @Column(name = "batch_number")
    @ApiModelProperty(value = "生产批次号")
    private String batchNumber;

    @JoinColumn(name = "technique_info_id")
    @ManyToOne(fetch=FetchType.EAGER)
    private TechniqueInfo techniqueInfo;

//    @Column(name = "product_code")
//    @ApiModelProperty(value = "产品代码")
//    private String productCode;

    @Column(name = "start_date")
    @ApiModelProperty(value = "计划开始日期")
    private String startDate;

    @Column(name = "end_date")
    @ApiModelProperty(value = "计划结束日期")
    private String endDate;

    @Column(name = "batch_plan_quantity")
    @ApiModelProperty(value = "计划产量")
    private Integer batchPlanQuantity = 0;

    /**用户Id**/
    @Column(name = "user_id")
    @ApiModelProperty(value = "用户Id")
    private Long userId;

    /**已完成数量**/
    @Column(name = "completed_quantity")
    @ApiModelProperty(value = "完成数量")
    private Integer completedQuantity = 0;

    /**可下达日计划总量**/
    @Column(name = "daily_plan_remain")
    @ApiModelProperty(value = "可下达日计划总量")
    private Integer dailyPlanRemain = 0;

    public void copy(BatchPlan source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }


}
