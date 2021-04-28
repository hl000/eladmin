package me.zhengjie.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author HL
 * @create 2021/4/27 22:14
 */
@Data
public class SummaryViewDto implements Serializable {
    private String class1;

    private String class2;

    private Timestamp sumDate;

    private String baNum;

    private String planName;

    private String proName;

    private String proNum;

    private Integer outPuts;

    private Integer rejects;

    private Double rejectRate;

    private String reasonRej;

    private Double workHour;

    private Double realWork;

    private Double rateWork;

    private Integer balance;

    private Double rateCap;

    private String reasonCap;

    private Double capMaxHour;

    private Double capEight;

    private Double capTen;

    private Double capForW;

    private Double manMaxHour;

    private Integer yieldDay;

    private Double rateDp;

    private Double theWork;

    private Integer menNum;

    private Double quaHour;

    private Double quaMeta1;

    private Double quaMeta2;

    private Double quaMeta3;

    private Double conMate1;

    private Double conMate2;

    private Double conMate3;

    private Double rateMate1;

    private Double rateMate2;

    private Double rateMate3;

    private Double conMeta1;

    private Double conMeta2;

    private Double conMeta3;

    private String insNum;

    private String insVer;

    private String proceName;

    private Timestamp proceCdate;

    private String proceCname;

}
