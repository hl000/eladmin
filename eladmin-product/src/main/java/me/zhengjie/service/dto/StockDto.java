package me.zhengjie.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author HL
 * @create 2021/5/19 13:54
 */
@Data
public class StockDto implements Serializable {

    private Integer id;
    private String manufactureAddress;
    private String processName;
    private String manufactureName;
    private Integer quantity;
    private Timestamp createTime;
    private Timestamp updateTime;
    private Integer processNumber;
    private Integer serialNumber;
}


