package me.zhengjie.service.dto;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author HL
 * @create 2021/6/15 7:59
 */
@Data
public class UnfinishedReasonDto {

    private String fillDate;

    private String manufactureAddress;

    private String secondaryType;

    private Integer processSequence;

    private String manufactureName;

    private Integer serialNumber;

    private Integer dailyPlanQuantity;

    private Integer actualQuantity;

    private Integer unfinishedQuantity;

    private String incompleteReasons;
}
