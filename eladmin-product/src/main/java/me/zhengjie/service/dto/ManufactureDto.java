package me.zhengjie.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * @author HL
 * @create 2021/4/13 18:48
 */

@Data
public class ManufactureDto implements Serializable {

    private Integer id;

//    private String productCode;

    /**生产计划编号**/
    private String planNumber;

    /**工序结存数**/
    private Integer inventoryBalance;

    /**意外消耗材料1**/
    private Double unexpectedMaterial1;

   /**意外消耗材料2**/
    private Double unexpectedMaterial2;

    /**意外消耗材料3**/
   private Double unexpectedMaterial3;

    /**意外消耗材料4**/
    private Double unexpectedMaterial4;

   /**班组人员数**/
   private Integer workerQuantity;

   /**工时（含加班）**/
   private Double workingHours;

   /**日实际产量（含不良品）**/
   private Integer dailyOutput;

   /**不良品数量**/
   private Integer rejectsQuantity;

   /**废品原因说明**/
   private String  rejectReasons;

   /**日产能未完成原因**/
   private String incompleteReasons;

   /**填报日期**/
   private Timestamp fillDate;

   /**用户Id**/
   private Long userId;

   /**报工名称**/
   private String manufactureName;

   /**修改时间**/
   private Timestamp updateTime;

    private String material1Name;

    private String material2Name;

    private String material3Name;

    private String material4Name;

    private String material1Unit;

    private String material2Unit;

    private String material3Unit;

    private String material4Unit;

//   /**生产批次号**/
//   private String batch_number;

    private String manufactureAddress;

    private Integer serialNumber;

    private Integer dailyPlanQuantity;

    private Double dailyCompletionRate;

    private String userName;

    private Integer transferQuantity;

    private Boolean isSame;

    private String processName;

    private String note ;
}
