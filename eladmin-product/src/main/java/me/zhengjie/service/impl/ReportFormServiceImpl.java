package me.zhengjie.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.Manufacture;
import me.zhengjie.mapper.ReportFormMapper;
import me.zhengjie.repository.ManufactureRepository;
import me.zhengjie.repository.WorkFactoryProcessRepository;
import me.zhengjie.service.ReportFormService;
import me.zhengjie.service.dto.*;
import me.zhengjie.utils.DateUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.QueryHelp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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

    @Override
    public void allReportFromDownload(HttpServletResponse response, String startDate, String endDate, String address, String invName) {
        List<ReportFormGroupDto> reportFormDtos = getAllReportForm(startDate, endDate, address, invName);
        List<String> listDate = DateUtil.getBetweenDate(startDate, endDate);
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReportFormGroupDto reportFormGroupDto : reportFormDtos) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("工厂", reportFormGroupDto.getAddress());
            map.put("物料名称", reportFormGroupDto.getInvName());
            for (String date : listDate) {
                map.put(date, reportFormGroupDto.getMap().get(date) != null ? reportFormGroupDto.getMap().get(date) : "--");
            }
            map.put("合计", reportFormGroupDto.getCount());
            list.add(map);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("物料名称", "合计");
        for (String date : listDate) {
            int count = 0;
            for (Map<String, Object> report : list) {
                if (report.get(date) != null && !"--".equals(String.valueOf(report.get(date)))) {
                    count = count + Integer.parseInt(String.valueOf(report.get(date)));
                }
            }
            map.put(date, count);
        }

        int summary = 0;
        for (Map<String, Object> report : list) {
            if (report.get("合计") != null && !"--".equals(String.valueOf(report.get("合计")))) {
                summary = summary + Integer.parseInt(String.valueOf(report.get("合计")));
            }
        }
        map.put("合计", summary);
        list.add(map);

        try {
            FileUtil.downloadExcel(list, response);
        } catch (Exception e) {
        }
    }

    @Override
    public void processCompletedDownload(HttpServletResponse response, String startDate, String endDate, String address, String invProcess) {
        List<ProcessCompletedGroupDto> reportFormDtos = getProcessCompleted(startDate, endDate, address, invProcess);
        List<String> listDate = DateUtil.getBetweenDate(startDate, endDate);
        List<Map<String, Object>> list = new ArrayList<>();
        for (ProcessCompletedGroupDto processCompletedGroupDto : reportFormDtos) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("工厂", processCompletedGroupDto.getAddress());
            map.put("工序", processCompletedGroupDto.getInvProcess());
            for (String date : listDate) {
                map.put(date, processCompletedGroupDto.getMap().get(date) != null ? processCompletedGroupDto.getMap().get(date) : "--");
            }
            map.put("合计", processCompletedGroupDto.getCount());
            list.add(map);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("工序", "合计");
        for (String date : listDate) {
            int count = 0;
            for (Map<String, Object> report : list) {
                if (report.get(date) != null && !"--".equals(String.valueOf(report.get(date)))) {
                    count = count + Integer.parseInt(String.valueOf(report.get(date)));
                }
            }
            map.put(date, count);
        }

        int summary = 0;
        for (Map<String, Object> report : list) {
            if (report.get("合计") != null && !"--".equals(String.valueOf(report.get("合计")))) {
                summary = summary + Integer.parseInt(String.valueOf(report.get("合计")));
            }
        }
        map.put("合计", summary);
        list.add(map);

        try {
            FileUtil.downloadExcel(list, response);
        } catch (Exception e) {
        }

    }

    @Override
    public void repertoryDownload(HttpServletResponse response, String cInvUnit, String adrName) {
        List<SemiFinishedStockDto> semiFinishedStockDtos = getRepertory(cInvUnit, adrName);
        List<Map<String, Object>> list = new ArrayList<>();
        for (SemiFinishedStockDto semiFinishedStockDto : semiFinishedStockDtos) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("工厂", semiFinishedStockDto.getAdrName());
            map.put("节数", semiFinishedStockDto.getCInvUnit());
            map.put("数量", semiFinishedStockDto.getQty());
            list.add(map);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("工厂", "合计");
        int summary = 0;
        for (Map<String, Object> report : list) {
            if (report.get("数量") != null && !"--".equals(String.valueOf(report.get("数量")))) {
                summary = summary + Integer.parseInt(String.valueOf(report.get("数量")));
            }
        }
        map.put("数量", summary);
        list.add(map);
        try {
            FileUtil.downloadExcel(list, response);
        } catch (Exception e) {
        }
    }
}
