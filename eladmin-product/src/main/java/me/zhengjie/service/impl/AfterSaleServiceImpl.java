package me.zhengjie.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.Maintenance4CarItem;
import me.zhengjie.domain.VehicleInfo;
import me.zhengjie.mapper.Maintenance4CarItemMapper;
import me.zhengjie.mapper.VehicleInfoMapper;
import me.zhengjie.service.AfterSaleService;
import me.zhengjie.service.dto.Maintenance4CarItemQueryCriteria;
import me.zhengjie.service.dto.VehicleInfoQueryCriteria;
import me.zhengjie.utils.EnvironmentUtils;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.RetryT;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HL
 * @create 2021/9/13 8:55
 */
@Service
@RequiredArgsConstructor
public class AfterSaleServiceImpl implements AfterSaleService {

    @Resource
    private Maintenance4CarItemMapper maintenance4CarItemMapper;

    @Resource
    private VehicleInfoMapper vehicleInfoMapper;


    @Override
    public List<Maintenance4CarItem> queryAll(Maintenance4CarItemQueryCriteria criteria) {
        try {
            return new RetryT<List<Maintenance4CarItem>>() {
                @Override
                protected List<Maintenance4CarItem> doAction() throws Exception {
                    return maintenance4CarItemMapper.queryAll(getTable());
                }
            }.execute();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<VehicleInfo> queryAllVehicleInfo(VehicleInfoQueryCriteria criteria) {
        try {
            return new RetryT<List<VehicleInfo>>() {
                @Override
                protected List<VehicleInfo> doAction() throws Exception {
                    return vehicleInfoMapper.queryAll(getVehicleInfoTable());
                }
            }.execute();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void downloadMaintenance4CarItem(HttpServletResponse response, List<Maintenance4CarItem> result) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Maintenance4CarItem maintenance4CarItem : result) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("车牌号", maintenance4CarItem.getFplateNo());
            map.put("行驶里程", maintenance4CarItem.getFrange());
            map.put("维护日期", maintenance4CarItem.getFDate());
            map.put("电导率", maintenance4CarItem.getFconductance());
            map.put("物理过滤器检查", maintenance4CarItem.getFitemCheck1());
            map.put("离子过滤器检查", maintenance4CarItem.getFitemCheck2());
            map.put("离子过滤器更换", maintenance4CarItem.getFitem2ChangePart());
            map.put("离子过滤器编码", maintenance4CarItem.getFitem2PartCode());
            map.put("空气滤芯检查", maintenance4CarItem.getFitemCheck3());
            map.put("空气滤芯更换", maintenance4CarItem.getFitem3ChangePart());
            map.put("空气泵检查", maintenance4CarItem.getFitemCheck4());
            map.put("消音器检查", maintenance4CarItem.getFitemCheck5());
            map.put("氢气检查", maintenance4CarItem.getFitemCheck6());
            map.put("循环泵检查", maintenance4CarItem.getFitemCheck7());
            map.put("尾排检查", maintenance4CarItem.getFitemCheck8());
            map.put("软管及接头检查", maintenance4CarItem.getFitemCheck9());
            map.put("传感器检查", maintenance4CarItem.getFitemCheck10());
            map.put("系统检查", maintenance4CarItem.getFitemCheck11());
            map.put("氢气测漏检查", maintenance4CarItem.getFitemCheck12());
            map.put("备注", maintenance4CarItem.getFremark());
            list.add(map);
        }
        try {
            FileUtil.downloadExcel(list, response);
        } catch (Exception e) {
        }

    }

    @Override
    public void downloadVehicleInfo(HttpServletResponse response, List<VehicleInfo> result) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (VehicleInfo vehicleInfo : result) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("车牌号", vehicleInfo.getFplateNo());
            map.put("系统编号", vehicleInfo.getFCEVSys());
            map.put("电堆编号", vehicleInfo.getElectricPile());
            map.put("加氢量", vehicleInfo.getFtotal4H2());
            map.put("里程数", vehicleInfo.getFrange());
            map.put("上次维护日期", vehicleInfo.getFdate());
            list.add(map);
        }
        try {
            FileUtil.downloadExcel(list, response);
        } catch (Exception e) {
        }
    }

    public String getTable() {
        return EnvironmentUtils.isProd() ? "[CEMT]..[ADM_Maintenance4CarItem]" : "[CEMT_TEST]..[ADM_Maintenance4CarItem]";
    }

    public String getVehicleInfoTable() {
        return EnvironmentUtils.isProd() ? "[CEMT]..[ADM_VehicleInfo]" : "[CEMT_TEST]..[ADM_VehicleInfo]";
    }
}
