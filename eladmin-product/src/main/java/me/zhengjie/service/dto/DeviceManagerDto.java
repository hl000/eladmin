package me.zhengjie.service.dto;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author HL
 * @create 2022/3/17 9:50
 */
@Data
public class DeviceManagerDto {

    private Integer deviceId;

    private String deviceCode;

    private String deviceSite;

    private String userId;

    private String userName;

    private Long deptId;

    private String deptName;

    private String assetCode;

    private String lanAddressOne;

    private String lanAddressTwo;

    private String lanAddressThree;

    private String lanAddressFour;

    private Integer deviceTypeId;

    private String deviceTypeName;

    private Date purchaseDate;

    private String pictureAddress;

    private Timestamp createDate;
}
