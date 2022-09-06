package me.zhengjie.mapper;

import me.zhengjie.annotation.DatabaseType;
import me.zhengjie.annotation.DynamicDao;
import me.zhengjie.domain.VehicleMileage;
import me.zhengjie.service.dto.VehicleMileageDto;
import me.zhengjie.service.dto.WorkStepDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author HL
 * @create 2021/12/23 16:44
 */
public interface VehicleMileageMapper {
    List<VehicleMileageDto> getVehicleMileage(@Param("mileageTable") String mileageTable, @Param("ADMXTGHTable") String ADMXTGHTable, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("licencePlate") String licencePlate, @Param("updateStackFlag") boolean updateStackFlag);

    List<String> getVehicleList(@Param("table") String table);
}
