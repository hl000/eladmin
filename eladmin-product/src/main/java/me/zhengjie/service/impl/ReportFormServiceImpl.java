package me.zhengjie.service.impl;

import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.Manufacture;
import me.zhengjie.domain.ProductParameter;
import me.zhengjie.repository.ManufactureRepository;
import me.zhengjie.service.ReportFormService;
import me.zhengjie.service.dto.DailyPlanGroupDto;
import me.zhengjie.service.dto.ReportFormDto;
import me.zhengjie.service.dto.ReportFormQueryCriteria;
import me.zhengjie.utils.QueryHelp;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HL
 * @create 2021/10/19 9:06
 */
@Service
@RequiredArgsConstructor
public class ReportFormServiceImpl implements ReportFormService {
    private final ManufactureRepository manufactureRepository;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    public List<ReportFormDto> getReportForm(ReportFormQueryCriteria criteria) {
        if (criteria.getFillDate() == null) {
            criteria.setFillDate(dateFormat.format(new Date()));
        }
        List<Manufacture> manufactures = manufactureRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));

        manufactures = manufactures.stream().filter(a -> {
            return a.getPlanNumber() != null && !"".equals(a.getPlanNumber()) && a.getDailyOutput() != null && a.getDailyOutput() > 0;
        }).collect(Collectors.toList());

        Map<String, List<Manufacture>> manufactureMap = manufactures.stream().collect(Collectors.groupingBy(a -> {
            return a.getManufactureAddress() + "," + a.getManufactureName();
        }));

        List<ReportFormDto> reportFormDtos = new ArrayList<>();

        for (Map.Entry<String, List<Manufacture>> entry : manufactureMap.entrySet()) {
            ReportFormDto reportFormDto = new ReportFormDto();
            List<Manufacture> manufacturesList = entry.getValue();

            Map<String, Integer> map = manufacturesList.stream().collect(Collectors.groupingBy(a -> format.format(a.getFillDate()), Collectors.summingInt(Manufacture::getDailyOutput)));
            Integer count = manufacturesList.stream().mapToInt(Manufacture::getDailyOutput).sum();

            reportFormDto.setManufactureAddress(entry.getKey().split(",")[0]);
            reportFormDto.setManufactureName(entry.getKey().split(",")[1]);
            reportFormDto.setCount(count);
            reportFormDto.setMap(map);
            reportFormDtos.add(reportFormDto);
        }
        return reportFormDtos;
    }
}
