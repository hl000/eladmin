package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author HL
 * @create 2021/6/7 9:47
 */
@Data
public class StackSummary {

    private String fillDate;

    private String manufactureAddress;

    private String manufactureName;

    private String component;

    private Integer dailyOutput;

    private Integer remainQuantity;
}
