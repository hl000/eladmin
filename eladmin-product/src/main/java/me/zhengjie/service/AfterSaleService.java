package me.zhengjie.service;

import me.zhengjie.domain.Maintenance4CarItem;
import me.zhengjie.domain.VehicleInfo;
import me.zhengjie.service.dto.Maintenance4CarItemQueryCriteria;
import me.zhengjie.service.dto.VehicleInfoQueryCriteria;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author HL
 * @create 2021/9/13 8:54
 */
public interface AfterSaleService {
    List<Maintenance4CarItem> queryAll(Maintenance4CarItemQueryCriteria criteria);

    List<VehicleInfo> queryAllVehicleInfo(VehicleInfoQueryCriteria criteria);

    void downloadMaintenance4CarItem(HttpServletResponse response, List<Maintenance4CarItem> result);

    void downloadVehicleInfo(HttpServletResponse response, List<VehicleInfo> result);
}
