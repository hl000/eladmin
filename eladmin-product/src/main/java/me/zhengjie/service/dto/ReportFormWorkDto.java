package me.zhengjie.service.dto;

import lombok.Data;

/**
 * @author HL
 * @create 2022/2/14 17:46
 */
@Data
public class ReportFormWorkDto {
    private String invName;

    private String fillDate;

    private String address;

    private String orderId;

    private Integer quantity;

    private String processCode;

}
