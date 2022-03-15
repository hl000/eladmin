package me.zhengjie.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.Manufacture;
import me.zhengjie.mapper.ReportFormMapper;
import me.zhengjie.repository.ManufactureRepository;
import me.zhengjie.repository.WorkFactoryProcessRepository;
import me.zhengjie.service.ReportFormService;
import me.zhengjie.service.dto.*;
import me.zhengjie.utils.QueryHelp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    private final WorkFactoryProcessRepository workFactoryProcessRepository;

    @Resource
    private final ReportFormMapper reportFormMapper;

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

    @Override
    public List<ReportFormGroupDto> getAllReportForm(String startDate, String endDate, String address, String invName) {
        List<ReportFormWorkDto> workDtos = reportFormMapper.queryReportFormWorkDto(startDate, endDate, address, invName);

        Map<String, List<ReportFormWorkDto>> manufactureMap = workDtos.stream().collect(Collectors.groupingBy(a -> {
            return a.getAddress() + "," + a.getInvName() + "," + a.getProcessCode() + "," + a.getOrderId();
        }));

        List<ReportFormGroupDto> reportFormDtos = new ArrayList<>();

        for (Map.Entry<String, List<ReportFormWorkDto>> entry : manufactureMap.entrySet()) {
            ReportFormGroupDto reportFormDto = new ReportFormGroupDto();
            List<ReportFormWorkDto> manufacturesList = entry.getValue();
            Map<String, Integer> map = manufacturesList.stream().collect(Collectors.groupingBy(a -> a.getFillDate(), Collectors.summingInt(ReportFormWorkDto::getQuantity)));
            Integer count = manufacturesList.stream().mapToInt(ReportFormWorkDto::getQuantity).sum();
            reportFormDto.setAddress(entry.getKey().split(",")[0]);
            reportFormDto.setInvName(entry.getKey().split(",")[1]);
            reportFormDto.setProcessCode(entry.getKey().split(",")[2]);
            reportFormDto.setOrderId(entry.getKey().split(",")[3]);
            reportFormDto.setCount(count);
            reportFormDto.setMap(map);
            reportFormDtos.add(reportFormDto);
        }
        reportFormDtos = reportFormDtos.stream().sorted(Comparator.comparing(ReportFormGroupDto::getOrderId).thenComparing(ReportFormGroupDto::getProcessCode)).collect(Collectors.toList());
        return reportFormDtos;
    }

    @Override
    public List<ProcessCompletedGroupDto> getProcessCompleted(String startDate, String endDate, String address, String invProcess) {
        List<ProcessCompletedDto> workDtos = reportFormMapper.queryProcessCompleted(startDate, endDate, address, invProcess);

        Map<String, List<ProcessCompletedDto>> manufactureMap = workDtos.stream().collect(Collectors.groupingBy(a -> {
            return a.getAddress() + ";" + a.getInvProcess() + ";" + a.getProcessCode() + ";" + a.getOrderId();
        }));

        List<ProcessCompletedGroupDto> reportFormDtos = new ArrayList<>();

        for (Map.Entry<String, List<ProcessCompletedDto>> entry : manufactureMap.entrySet()) {
            ProcessCompletedGroupDto reportFormDto = new ProcessCompletedGroupDto();
            List<ProcessCompletedDto> manufacturesList = entry.getValue();

            Map<String, Integer> map = manufacturesList.stream().collect(Collectors.groupingBy(a -> a.getFillDate(), Collectors.summingInt(ProcessCompletedDto::getQuantity)));
            Integer count = manufacturesList.stream().mapToInt(ProcessCompletedDto::getQuantity).sum();
            reportFormDto.setAddress(entry.getKey().split(";")[0]);
            reportFormDto.setInvProcess(entry.getKey().split(";")[1]);
            reportFormDto.setProcessCode(entry.getKey().split(";")[2]);
            reportFormDto.setOrderId(entry.getKey().split(";")[3]);
            reportFormDto.setCount(count);
            reportFormDto.setMap(map);
            reportFormDtos.add(reportFormDto);
        }
        reportFormDtos = reportFormDtos.stream().sorted(Comparator.comparing(ProcessCompletedGroupDto::getOrderId).thenComparing(ProcessCompletedGroupDto::getProcessCode)).collect(Collectors.toList());
        return reportFormDtos;
    }

    @Override
    public Object getFactoryProcess() {
        return workFactoryProcessRepository.findAll();
    }

    @Override
    public List<SemiFinishedStockDto> getRepertory(String cInvUnit, String adrName) {
        List<SemiFinishedStockDto> semiFinishedStockDtos = reportFormMapper.getRepertory(cInvUnit, adrName);
        return semiFinishedStockDtos;
    }

    @Override
    public Object getPitchList() {
        return reportFormMapper.getPitchList();
    }
}
