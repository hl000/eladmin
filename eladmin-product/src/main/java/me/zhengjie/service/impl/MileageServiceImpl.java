package me.zhengjie.service.impl;

import com.alibaba.fastjson.JSONArray;
import lombok.RequiredArgsConstructor;
import me.zhengjie.Utils.HttpClientUtil;
import me.zhengjie.mapper.VehicleMileageMapper;
import me.zhengjie.service.MileageService;
import me.zhengjie.service.UpdateStackService;
import me.zhengjie.service.dto.MileageDto;
import me.zhengjie.service.dto.VehicleMileageDto;
import me.zhengjie.service.dto.VehicleMileageStackDto;
import me.zhengjie.utils.DateUtil;
import me.zhengjie.utils.EnvironmentUtils;
import me.zhengjie.utils.FileUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HL
 * @create 2022/6/26 22:32
 */
@Service
@RequiredArgsConstructor
public class MileageServiceImpl implements MileageService {
    @Resource
    private final VehicleMileageMapper vehicleMileageMapper;

    private final UpdateStackService updateStackService;

    @Override
    public List<VehicleMileageStackDto> getVehicleMileage(String startDate, String endDate, String licencePlate, boolean updateStackFlag) {
        List<VehicleMileageStackDto> vehicleMileageStackDtos = new ArrayList<>();
        List<VehicleMileageDto> vehicleMileages = vehicleMileageMapper.getVehicleMileage(getMileageTable(), getADMXTGHTable(), startDate, endDate, licencePlate, updateStackFlag);
        Map<String, List<VehicleMileageDto>> vehicleMileageMap = vehicleMileages.stream().collect(Collectors.groupingBy(VehicleMileageDto::getLicencePlate));
        for (String key : vehicleMileageMap.keySet()) {
            VehicleMileageStackDto vehicleMileageStackDto = new VehicleMileageStackDto();
            vehicleMileageStackDto.setLicencePlate(key);
            List<VehicleMileageDto> vehicleMileageDtoList = vehicleMileageMap.get(key);
            Map<String, VehicleMileageDto> map = new HashMap<>();
            for (VehicleMileageDto vehicleMileageDto : vehicleMileageDtoList) {
                map.put(vehicleMileageDto.getDriveDate(), vehicleMileageDto);
            }
            vehicleMileageStackDto.setVehicleMileageDtoMap(map);
            vehicleMileageStackDtos.add(vehicleMileageStackDto);
        }

        return vehicleMileageStackDtos;
    }

    @Override
    public void vehicleMileageDownload(HttpServletResponse response, String startTs, String endTs, String label, boolean updateStackFlag) {
        List<VehicleMileageStackDto> vehicleMileageStackDtos = getVehicleMileage(startTs, endTs, label, updateStackFlag);
        List<String> listDate = DateUtil.getBetweenDate(startTs, endTs);
        List<Map<String, Object>> list = new ArrayList<>();
        for (VehicleMileageStackDto vehicleMileageStackDto : vehicleMileageStackDtos) {
            Map<String, Object> mileageMap = new LinkedHashMap<>();
            mileageMap.put("车牌号码", vehicleMileageStackDto.getLicencePlate());
            for (String date : listDate) {
                mileageMap.put(date, vehicleMileageStackDto.getVehicleMileageDtoMap().get(date) != null ? (vehicleMileageStackDto.getVehicleMileageDtoMap().get(date).getDailyMileage()) + (vehicleMileageStackDto.getVehicleMileageDtoMap().get(date).isUpdateStack() ? "(换堆)" : "") : "--");
            }
            list.add(mileageMap);
        }

        try {
            FileUtil.downloadExcel(list, response);
        } catch (Exception e) {
        }
    }

    @Override
    public List<String> getVehicleList() {
        return vehicleMileageMapper.getVehicleList(getMileageTable());
    }

    public String getMileageTable() {
        return EnvironmentUtils.isProd() ? "[CEMT]..[Vehicle_Mileage]" : "[CEMT_TEST]..[Vehicle_Mileage]";
    }

    public String getADMXTGHTable() {
        return EnvironmentUtils.isProd() ? "[CEMT]..[ADMXTGH]" : "[CEMT_TEST]..[ADMXTGH]";
    }
}
