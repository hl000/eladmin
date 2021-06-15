package me.zhengjie.domain;

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

    private String manufactureName;

    private Integer dailyPlanQuantity;

    private Integer actualQuantity;

    private String incompleteReasons;
}
