package me.zhengjie.service.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author HL
 * @create 2021/10/19 21:12
 */
@Data
public class ProcessCompletedDto {

    private String invProcess;

    private String fillDate;

    private String address;

    private String orderId;

    private Integer quantity;

    private String processCode;
}
