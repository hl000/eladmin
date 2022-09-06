package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.domain.ManufactureOrder;

/**
 * @author HL
 * @create 2022/9/5 21:43
 */
@Data
public class ManufactureOrderActiveDto {
    private Integer activeTimes;
    private ManufactureOrder manufactureOrder;
}
