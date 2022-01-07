package me.zhengjie.service.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author HL
 * @create 2022/1/4 10:03
 */
@Data
public class WorkListMapDto {
    private String fSubDate;

//    private String fWorkShop;

    private String fArcName;

    private String fWorkOrder;

    private String fInvCode;

    private String fInvName;

    private String fInvStd;

    private Map<String, Integer> map;

    private Integer total;
}
