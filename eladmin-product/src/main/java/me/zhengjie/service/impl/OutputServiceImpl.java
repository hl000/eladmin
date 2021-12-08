package me.zhengjie.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.Balance;
import me.zhengjie.domain.Manufacture;
import me.zhengjie.domain.ProductParameter;
import me.zhengjie.mapper.ManufactureMapper;
import me.zhengjie.repository.ManufactureRepository;
import me.zhengjie.repository.ProductParameterRepository;
import me.zhengjie.service.OutputService;
import me.zhengjie.service.dto.ManufactureDto;
import me.zhengjie.service.dto.ManufactureDtoQueryCriteria;
import me.zhengjie.service.dto.OutputDto;
import me.zhengjie.service.dto.ProductParameterQueryCriteria;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.QueryHelp;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HL
 * @create 2021/5/26 14:20
 */
@Service
@RequiredArgsConstructor
public class OutputServiceImpl implements OutputService {

    @Resource(name = "manufactureMapperImpl")
    private ManufactureMapper manufactureMapper;

    private final ManufactureRepository manufactureRepository;


    private final ProductParameterRepository productParameterRepository;


    @Override
    public List<OutputDto> queryManufacture(ManufactureDtoQueryCriteria criteria, Timestamp startDate, Timestamp endDate) {
        List<Manufacture> manufactureList = manufactureRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));

        if (manufactureList == null || manufactureList.size() == 0)
            return null;

        manufactureList = manufactureList.stream().filter(aa -> {
            return aa.getFillDate().getTime() >= startDate.getTime() && aa.getFillDate().getTime() <= endDate.getTime();
        }).collect(Collectors.toList());

        if (manufactureList == null || manufactureList.size() == 0)
            return null;
        List<ManufactureDto> manufactureDtoList = manufactureMapper.toDto(manufactureList);

        manufactureDtoList.stream().forEach(manufactureDto -> {
            ProductParameterQueryCriteria productParameterQueryCriteria = new ProductParameterQueryCriteria();
            productParameterQueryCriteria.setManufactureName(manufactureDto.getManufactureName());
            List<ProductParameter> productParameterList = productParameterRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, productParameterQueryCriteria, criteriaBuilder));

            if (productParameterList != null && productParameterList.size() > 0) {
                manufactureDto.setProcessName(productParameterList.get(0).getTechniqueInfo().getCategory().getSecondaryType());
            }
        });

        if (criteria.getProcessName() != null) {
            manufactureDtoList = manufactureDtoList.stream().filter(a -> criteria.getProcessName().equals(a.getProcessName())).collect(Collectors.toList());
        }

        if (manufactureDtoList == null || manufactureDtoList.size() == 0)
            return null;
        Map<String, List<ManufactureDto>> groupByMap = manufactureDtoList.stream().collect(Collectors.groupingBy(manufacture -> {
            return manufacture.getManufactureAddress() + "_" + manufacture.getProcessName() + "_" + manufacture.getManufactureName();
        }));

        List<OutputDto> outputDtoList = new ArrayList<>();
        for (Map.Entry<String, List<ManufactureDto>> entry : groupByMap.entrySet()) {
            OutputDto outputDto = new OutputDto();
            List<ManufactureDto> list = entry.getValue();

            outputDto.setStartDate(startDate);
            outputDto.setEndDate(endDate);
            outputDto.setManufactureAddress(list.get(0).getManufactureAddress());
            outputDto.setProcessName(list.get(0).getProcessName());
            outputDto.setManufactureName(list.get(0).getManufactureName());
            outputDto.setSerialNumber(list.get(0).getSerialNumber());

            Integer output = list.stream().mapToInt(manufacture -> {
                return manufacture.getDailyOutput();
            }).sum();

            Integer reject = list.stream().mapToInt(manufacture -> {
                return manufacture.getRejectsQuantity();
            }).sum();

            outputDto.setOutput(output);
            outputDto.setRejectQuantity(reject);
            outputDtoList.add(outputDto);

        }

        outputDtoList = outputDtoList.stream()
                .sorted(Comparator.comparing(OutputDto::getManufactureAddress,Comparator.reverseOrder()).thenComparing(OutputDto::getSerialNumber))
                .collect(Collectors.toList());
        return outputDtoList;
    }

    @Override
    public void download(List<OutputDto> outputDtoList, Timestamp startDate, Timestamp endDate, HttpServletResponse response) {

        List<Map<String, Object>> list = new ArrayList<>();
        for (OutputDto outputDto : outputDtoList) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("开始时间", startDate);
            map.put("结束时间", endDate);
            map.put("基地", outputDto.getManufactureAddress());
            map.put("工序", outputDto.getProcessName());
            map.put("产品名称", outputDto.getManufactureName());
            map.put("产量", outputDto.getOutput());
            map.put("不良品数量", outputDto.getRejectQuantity());
            list.add(map);
        }
        try {
            FileUtil.downloadExcel(list, response);
        } catch (Exception e) {
        }

    }
}
