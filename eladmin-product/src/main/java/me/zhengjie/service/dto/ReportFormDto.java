package me.zhengjie.service.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author HL
 * @create 2021/10/19 21:12
 */
@Data
public class ReportFormDto {

    private String manufactureName;
    private String manufactureAddress;
    private Integer count;
    private Map<String, Integer> map;

//    public ReportFormDto(String key, Map<String, Integer> map) {
//        this.name = key;
//        this.map = map;
//    }
}
