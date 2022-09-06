package me.zhengjie.service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author HL
 * @create 2022/6/29 11:50
 */
@Data
public class VehicleMileageStackDto {

    private String licencePlate;

    private Map<String, VehicleMileageDto> vehicleMileageDtoMap;
}
