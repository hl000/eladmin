package me.zhengjie.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author HL
 * @create 2021/4/15 17:56
 */

@Entity
@Data
@Table(name="manufacture_summary")
public class ManufactureSummary implements Serializable {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id")
    private Integer id;

//    @Column(name = "batchPlanId")
//    @ApiModelProperty(value = "生产批次号")
//    private Integer batchPlanId;
//

    /**生产计划编号**/
    @Column(name = "plan_number")
    @ApiModelProperty(value = "生产计划编号")
    private String planNumber;

    /**
     * 材料1消耗
     **/
    @Column(name = "consume_Material1")
    @ApiModelProperty(value = "材料1消耗")
    private Double consumeMaterial1;
    /**
     * 材料2消耗
     **/
    @Column(name = "consume_Material2")
    @ApiModelProperty(value = "材料2消耗")
    private Double consumeMaterial2;
    /**
     * 材料3消耗
     **/
    @Column(name = "consume_Material3")
    @ApiModelProperty(value = "材料3消耗")
    private Double consumeMaterial3;

    /**
     * 实际工时定额达成率
     **/
    @Column(name = "actual_hour_quota")
    @ApiModelProperty(value = "实际工时定额达成率")
    private Double actualHourQuota;

    /**
     * 实际材料1定额达成率
     **/
    @Column(name = "actual_Material1_quota")
    @ApiModelProperty(value = "实际材料1定额达成率")
    private Double actualMaterial1Quota;

    /**
     * 实际材料2定额达成率
     **/
    @Column(name = "actual_Material2_quota")
    @ApiModelProperty(value = "实际材料2定额达成率")
    private Double actualMaterial2Quota;

    /**
     * 实际材料3定额达成率
     **/
    @Column(name = "actual_Material3_quota")
    @ApiModelProperty(value = "实际材料3定额达成率")
    private Double actualMaterial3Quota;

    /**
     * 工时实际
     **/
    @Column(name = "actual_hours")
    @ApiModelProperty(value = "工时实际")
    private Double actualHours;

    /**
     * 理论工时
     **/
    @Column(name = "theory_hours")
    @ApiModelProperty(value = "理论工时")
    private Double theoryHours;

    /**
     * 日计划完成率
     **/
    @Column(name = "daily_completion_rate")
    @ApiModelProperty(value = "日计划完成率")
    private Double dailyCompletionRate;

    /**
     * 批次不良数
     **/
    @Column(name = "rejects_total")
    @ApiModelProperty(value = "批次不良数")
    private Double rejectsTotal;

    /**
     * 日不良率
     **/
    @Column(name = "daily_reject_rate")
    @ApiModelProperty(value = "日不良率")
    private Double dailyRejectRate;

    /**
     * 批次平均不良率
     **/
    @Column(name = "batch_reject_rate")
    @ApiModelProperty(value = "批次平均不良率")
    private Double batchRejectRate;
    /**
     * 年度平均不良率
     **/
    @Column(name = "annual_reject_rate")
    @ApiModelProperty(value = "年度平均不良率")
    private Double annualRejectRate;

    /**
     * 批次计划累计完成百分比
     **/
    @Column(name = "accumulative_total_percentage")
    @ApiModelProperty(value = "批次计划累计完成百分比")
    private Double accumulativeTotalPercentage;


    /**
     * 年度不良品总数
     **/
    @Column(name = "annual_reject_total")
    @ApiModelProperty(value = "年度不良品总数")
    private Integer annualRejectTotal;

    /**
     * 年度生产累计总量
     **/
    @Column(name = "annual_output_total")
    @ApiModelProperty(value = "年度生产累计总量")
    private Integer annualOutputTotal;

    /**
     * 产能利用率
     **/
    @Column(name = "capacity_utilization")
    @ApiModelProperty(value = "产能利用率")
    private Double capacityUtilization;

    /**
     * 8小时工作产能（7.5小时）
     **/
    @Column(name = "hour8_capacity")
    @ApiModelProperty(value = "8小时工作产能（7.5小时）")
    private Double hour8Capacity;

    /**
     * 10小时工作产能（9.5小时）
     **/
    @Column(name = "hour10_capacity")
    @ApiModelProperty(value = "10小时工作产能（9.5小时）")
    private Double hour10Capacity;

    /**
     * 当前状态最大周产量预测
     **/
    @Column(name = "weekly_capacity")
    @ApiModelProperty(value = "当前状态最大周产量预测")
    private Double weeklyCapacity;

    /**
     * 当前状态最大月产量预测
     **/
    @Column(name = "monthly_capacity")
    @ApiModelProperty(value = "当前状态最大月产量预测")
    private Double monthlyCapacity;
    /**
     * 当前状态年产能预测
     **/
    @Column(name = "annual_capacity")
    @ApiModelProperty(value = "当前状态年产能预测")
    private Double annualCapacity;

    /**
     * 设备最大年产量
     **/
    @Column(name = "max_annual_capacity")
    @ApiModelProperty(value = "设备最大年产量")
    private Double maxAnnualCapacity;

    /**
     * 260节电堆可制造数量 /年
     **/
    @Column(name = "stack260_quantity")
    @ApiModelProperty(value = "260节电堆可制造数量 /年")
    private Integer stack260Quantity;
    /**
     * 340节电堆可制造数量 /年
     **/
    @Column(name = "stack340_quantity")
    @ApiModelProperty(value = "340节电堆可制造数量 /年")
    private Integer stack340Quantity;

    /**
     * 450节电堆可制造数量 /年
     **/
    @Column(name = "stack450_quantity")
    @ApiModelProperty(value = "450节电堆可制造数量 /年")
    private Integer stack450Quantity;

    /**
     * 批次计划完成数量
     **/
    @Column(name = "batch_plan_quantity")
    @ApiModelProperty(value = "批次计划完成数量")
    private Integer batchPlanQuantity;

    /**
     * 批次累计完成数量（合格）
     **/
    @Column(name = "batch_completed_quantity")
    @ApiModelProperty(value = "批次累计完成数量（合格）")
    private Integer batchCompletedQuantity;

    /**
     * 总计划制造量
     **/
    @Column(name = "total_plan_quantity")
    @ApiModelProperty(value = "总计划制造量")
    private Integer totalPlanQuantity;

    /**
     * 日计划总量
     **/
    @Column(name = "daily_plan_quantity")
    @ApiModelProperty(value = "日计划产量")
    private Integer dailyPlanQuantity;

    @Column(name="update_time")
    @ApiModelProperty(value="更新时间")
    @UpdateTimestamp
    private Timestamp updateTime;


    public void copy(ManufactureSummary source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
