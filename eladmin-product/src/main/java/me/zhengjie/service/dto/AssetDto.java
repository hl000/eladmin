package me.zhengjie.service.dto;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author HL
 * @create 2022/3/17 16:59
 */
@Data
public class AssetDto {
    private String assetCode;

    private Timestamp startDate;

    private String deviceStyle;

    private String assetName;

    private String supplierName;
}
