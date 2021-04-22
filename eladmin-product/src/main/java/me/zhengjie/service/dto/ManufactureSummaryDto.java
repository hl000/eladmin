package me.zhengjie.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author HL
 * @create 2021/4/14 10:04
 */
@Data
public class ManufactureSummaryDto {

    private Integer id;

    private String planNumber;

    /**
     * 材料1消耗
     **/
    private String consumeMaterial1;
    /**
     * 材料2消耗
     **/
    private String consumeMaterial2;
    /**
     * 材料3消耗
     **/
    private String consumeMaterial3;

    /**
     * 实际工时定额达成率
     **/
    private Double actualHourQuota;

    /**
     * 实际材料1定额达成率
     **/
    private String actualMaterial1Quota;

    /**
     * 实际材料2定额达成率
     **/
    private String actualMaterial2Quota;

    /**
     * 实际材料3定额达成率
     **/
    private String actualMaterial3Quota;

    /**
     * 工时实际
     **/
    private Double actualHours;

    /**
     * 理论工时
     **/
    private Double theoryHours;

    /**
     * 日计划完成率
     **/
    private Double dailyCompletionRate;

    /**
     * 批次不良数
     **/
    private Double rejectsTotal;

    /**
     * 日不良率
     **/
    private Double dailyRejectRate;

    /**
     * 批次平均不良率
     **/
    private Double batchRejectRate;
    /**
     * 年度平均不良率
     **/
    private Double annualRejectRate;

    /**
     * 批次计划累计完成百分比
     **/
    private Double accumulativeTotalPercentage;

    /**
     * 批次计划完成数量
     **/
    private Integer batchPlanQuantity;

    /**
     * 年度不良品总数
     **/
    private Integer annualRejectTotal;

    /**
     * 年度生产累计总量
     **/
    private Integer annualOutputTotal;

    /**
     * 产能利用率
     **/
    private Double capacityUtilization;

    /**
     * 8小时工作产能（7.5小时）
     **/
    private Double hour8Capacity;

    /**
     * 10小时工作产能（9.5小时）
     **/
    private Double hour10Capacity;

    /**
     * 当前状态最大周产量预测
     **/
    private Double weeklyCapacity;

    /**
     * 当前状态最大月产量预测
     **/
    private Double monthlyCapacity;
    /**
     * 当前状态年产能预测
     **/
    private Double annualCapacity;

    /**
     * 设备最大年产量
     **/
    private Double maxAnnualCapacity;

    /**
     * 260节电堆可制造数量 /年
     **/
    private Integer stack260Quantity;
    /**
     * 340节电堆可制造数量 /年
     **/
    private Integer stack340Quantity;
    /**
     * 450节电堆可制造数量 /年
     **/
    private Integer stack450Quantity;

    /**
     * 批次累计完成数量（合格）
     **/
    private Integer batchCompletedQuantity;

    /**
     * 总计划制造量
     **/
    private Integer totalPlanQuantity;

    /**
     * 日计划总量
     **/
    private Integer dailyPlanQuantity;

    private Timestamp updateTime;

}
