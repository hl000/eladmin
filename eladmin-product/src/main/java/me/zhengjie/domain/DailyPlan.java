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
 * @create 2021/4/14 14:16
 */

@Entity
@Data
@Table(name="report_daily_plan")
public class DailyPlan  implements Serializable {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id")
    private Integer id;

//    @Column(name="batch_plan_id")
//    private Integer batchPlanId;

    @JoinColumn(name = "batch_plan_id")
    @ManyToOne(fetch=FetchType.EAGER)
    private BatchPlan batchPlan;

    //自动生成
    @Column(name = "plan_number")
    @ApiModelProperty(value = "生产计划编号")
    private String planNumber;

    @Column(name = "start_date")
    @ApiModelProperty(value = "计划生产日期")
    private String startDate;

    @Column(name = "daily_plan_quantity")
    @ApiModelProperty(value = "日计划产量")
    private Integer dailyPlanQuantity = 0;

    @Column(name = "effect")
    @ApiModelProperty(value = "是否生效")
    private Boolean effect;

    /**用户Id**/
    @Column(name = "user_id")
    @ApiModelProperty(value = "用户Id")
    private Long userId;

    @Column(name = "manufacture_name")
    @ApiModelProperty(value = "报工名称")
    private String manufactureName;

    @Column(name = "update_time")
    @ApiModelProperty(value = "更新时间")
    @UpdateTimestamp
    private Timestamp updateTime;

    /**已完成数量**/
    @Column(name = "completed_quantity")
    @ApiModelProperty(value = "完成数量")
    private Integer completedQuantity = 0;


    /**报工名称**/
    @Column(name = "manufacture_address")
    @ApiModelProperty(value = "生产基地")
    private String manufactureAddress;

    @Column(name = "serial_number")
    @ApiModelProperty(value = "工序号")
    private Integer serialNumber;

    public void copy(DailyPlan source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
