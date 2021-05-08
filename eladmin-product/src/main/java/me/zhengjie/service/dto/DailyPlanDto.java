package me.zhengjie.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.domain.BatchPlan;
import me.zhengjie.domain.TechniqueInfo;

import javax.persistence.Column;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author HL
 * @create 2021/4/14 9:18
 */
@Data
public class DailyPlanDto implements Serializable {

    private Integer id;

//    private Integer batchPlanId;


//    /**
//     * 生产批次号
//     **/
//    private String batchNumber;

    /**
     * 生产计划编号
     **/
    private String planNumber;

//    /**
//     * 产品代码
//     **/
//    private String productCode;

    /**
     * 计划生产日期
     **/
    private String startDate;

    /**
     * 日计划产量
     **/
    private int dailyPlanQuantity;

    /**
     * 批量计划
     */
    private BatchPlan batchPlan;

    /**
     * 是否生效
     */
    private Boolean effect;

    private Long userId;

    private Timestamp updateTime;

    /**报工名称**/
    private String manufactureName;

    /**已完成数量**/
    private Integer completedQuantity;

    private Integer workerQuantity;

    private Double workHours;

    private String material1Name;

    private String material2Name;

    private String material3Name;

    private String material4Name;

    private String material1Unit;

    private String material2Unit;

    private String material3Unit;

    private String material4Unit;

    private String manufactureAddress;
}
