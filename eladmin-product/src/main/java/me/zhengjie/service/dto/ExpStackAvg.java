package me.zhengjie.service.dto;

import lombok.Data;

/**
 * @author HL
 * @create 2021/6/21 20:23
 */
@Data
public class ExpStackAvg {
    private String address;

    private String date;

    private Double avg500;

    private Double avg600;

    private Double avg700;

    private Double avg800;

    private Double avg900;

    private Double avg1000;
}
