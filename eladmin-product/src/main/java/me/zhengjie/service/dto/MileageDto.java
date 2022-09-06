package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.domain.UpdateStack;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author HL
 * @create 2022/6/26 19:57
 */
@Data
public class MileageDto {
    private String label;
    private String id;
    private Map<String, BigDecimal> mileage;
    private Boolean updateStack;
    private List<UpdateStack> updateStackList;
}
