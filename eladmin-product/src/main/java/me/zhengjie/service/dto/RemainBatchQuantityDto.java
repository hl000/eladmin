package me.zhengjie.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author HL
 * @create 2021/4/23 13:44
 */
@Data
public class RemainBatchQuantityDto implements Serializable {
    private Integer batchPlanId;
    private String batchNumber;

    private String productName;

    private List<RemainInfoDto> remainInfo;

    private String batchPlanName;

    private String manufactureAddress;


}
