package me.zhengjie.service.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author HL
 * @create 2021/10/19 21:12
 */
@Data
public class ReportFormGroupDto {

    private String address;
    private String invName;
    private Integer count;
    private String orderId;
    private String processCode;
    private Map<String, Integer> map;

//    public ReportFormDto(String key, Map<String, Integer> map) {
//        this.name = key;
//        this.map = map;
//    }
}
