package me.zhengjie.service.dto;

import lombok.Data;

/**
 * @author HL
 * @create 2022/3/1 15:05
 */
@Data
public class SemiFinishedStockDto {

    private String adrCode;
    private String adrName;

    private String cInvUnit;

    private Integer qty;


}
