package me.zhengjie.service;

import me.zhengjie.service.dto.VehicleMileageStackDto;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author HL
 * @create 2022/6/26 22:30
 */
public interface MileageService {

    List<VehicleMileageStackDto> getVehicleMileage(String startTs, String endTs, String entityIdStr, boolean updateStackFlag);

    void vehicleMileageDownload(HttpServletResponse response, String startTs, String endTs, String label, boolean updateStackFlag);

    List<String> getVehicleList();
}
