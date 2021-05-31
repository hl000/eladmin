package me.zhengjie.service.dto;

import lombok.Data;
import org.apache.tomcat.jni.Time;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author HL
 * @create 2021/5/26 14:12
 */
@Data
public class OutputDto implements Serializable {

    private Timestamp startDate;

    private Timestamp endDate;

    private String manufactureAddress;

    private String processName;

    private String manufactureName;

    private Integer output;

    private Integer rejectQuantity;

    private Integer serialNumber;


}
