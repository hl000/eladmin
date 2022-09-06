package me.zhengjie.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.FuelCellComponents;
import me.zhengjie.repository.FuelCellComponentsRepository;
import me.zhengjie.service.FuelCellComponentsService;
import me.zhengjie.service.dto.FuelCellComponentsQueryCriteria;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HL
 * @create 2022/7/5 14:21
 */
@Service
@RequiredArgsConstructor
public class FuelCellComponentsServiceImpl implements FuelCellComponentsService {

    private final FuelCellComponentsRepository fuelCellComponentsRepository;

    @Override
    public Object create(FuelCellComponents resources) {
        return fuelCellComponentsRepository.save(resources);
    }

    @Override
    public Object update(FuelCellComponents resources) {
        FuelCellComponents fuelCellComponents = fuelCellComponentsRepository.findById(resources.getId()).orElseGet(FuelCellComponents::new);
        ValidationUtil.isNull(fuelCellComponents.getId(), "fuelCellComponents", "id", resources.getId());
        fuelCellComponents.copy(resources);
        return fuelCellComponentsRepository.save(fuelCellComponents);
    }

    @Override
    public Map<String, Object> queryAll(FuelCellComponentsQueryCriteria criteria, Pageable pageable) {
        Page<FuelCellComponents> page = fuelCellComponentsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page);
    }

    @Override
    public List<FuelCellComponents> queryAll(FuelCellComponentsQueryCriteria criteria) {
        return fuelCellComponentsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
    }

    @Override
    public void downloadPartNumber(HttpServletResponse response, FuelCellComponentsQueryCriteria criteria) {
        List<FuelCellComponents> fuelCellComponentsList = queryAll(criteria);
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < fuelCellComponentsList.size(); i++) {
            FuelCellComponents fuelCellComponents = fuelCellComponentsList.get(i);
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("序号", i + 1);
            map.put("系统支架编号", fuelCellComponents.getBracketCode());
            map.put("系统电堆编号", fuelCellComponents.getElectricPileCode());
            map.put("系统压缩机编号", fuelCellComponents.getCompressorCode());
            map.put("压缩机控制器编号", fuelCellComponents.getCompressorctlCode());
            map.put("系统氢循环泵编号", fuelCellComponents.getHydrogenPumpCode());
            map.put("氢循环泵控制器编号", fuelCellComponents.getPumpctlCode());
//            map.put("系统氢喷编号①", fuelCellComponents.getHydrogenInjectorOne());
//            map.put("系统氢喷编号②", fuelCellComponents.getHydrogenInjectorTwo());
//            map.put("系统氢喷编号③", fuelCellComponents.getHydrogenInjectorThree());
//            map.put("系统节气门①", fuelCellComponents.getAirDamperOne());
//            map.put("系统节气门②", fuelCellComponents.getAirDamperTwo());
            map.put("系统电池堆控制器", fuelCellComponents.getPilectlCode());
//            map.put("系统电压控制器①", fuelCellComponents.getVoltagectlOne());
//            map.put("系统电压控制器②", fuelCellComponents.getVoltagectlTwo());
            map.put("PTC加热模块编号", fuelCellComponents.getPtcCode());
            map.put("加热尾排电磁阀编号", fuelCellComponents.getPtcRadiotube());
            map.put("三通阀编号", fuelCellComponents.getThreeWayValve());
            map.put("DCDC编号", fuelCellComponents.getDcdcCode());
            map.put("生产编码", fuelCellComponents.getProductCode());
            map.put("增湿器", fuelCellComponents.getHumidifierCode());
            map.put("水泵", fuelCellComponents.getWaterPump());
            list.add(map);
        }

        try {
            FileUtil.downloadExcel(list, response);
        } catch (Exception e) {
        }
    }


}
