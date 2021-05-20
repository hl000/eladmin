package me.zhengjie.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HL
 * @create 2021/4/23 13:55
 */

@Data
public class RemainInfoDto implements Serializable {
        private String manufactureName;
        private Integer remainDailyQuantity;
        private Integer unitsQuantity;
        private Integer serialNumber;
}
