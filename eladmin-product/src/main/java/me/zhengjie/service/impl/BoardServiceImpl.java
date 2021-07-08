package me.zhengjie.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.*;
import me.zhengjie.repository.BatchPlanRepository;
import me.zhengjie.repository.DailyPlanRepository;
import me.zhengjie.repository.ManufactureRepository;
import me.zhengjie.repository.ProductParameterRepository;
import me.zhengjie.service.BoardService;
import me.zhengjie.service.dto.*;
import me.zhengjie.utils.QueryHelp;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HL
 * @create 2021/6/15 8:35
 */
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final ManufactureRepository manufactureRepository;

    private final DailyPlanRepository dailyPlanRepository;

    private final BatchPlanRepository batchPlanRepository;

    private final ProductParameterRepository productParameterRepository;

    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<PlanBoardDto> getPlanBoard(BoardQueryCriteria boardQueryCriteria) {
        String today = dateFormat.format(new Date());
        BatchPlanQueryCriteria batchPlanQueryCriteria = new BatchPlanQueryCriteria();
        batchPlanQueryCriteria.setStartDate(today);
        batchPlanQueryCriteria.setEndDate(today);
        if (boardQueryCriteria.getManufactureAddress() != null) {
            batchPlanQueryCriteria.setManufactureAddress(boardQueryCriteria.getManufactureAddress());
        }
        List<BatchPlan> batchPlanList = batchPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, batchPlanQueryCriteria, criteriaBuilder));

//        batchPlanList.stream().sorted(Comparator.comparing(BatchPlan::getId, Comparator.reverseOrder())).collect(Collectors.toList());

        List<ProductParameter> productParameterList1 = productParameterRepository.findAll();
        if (productParameterList1 == null || productParameterList1.size() == 0) {
            return null;
        }
        Map<String, List<ProductParameter>> productParameterMap = productParameterList1.stream().collect(Collectors.groupingBy(a -> {
            return a.getProductName() + "_" + a.getManufactureName();
        }));


        List<PlanBoardDto> planBoardDtoList = new ArrayList<>();
        for (BatchPlan batchPlan : batchPlanList) {
            DailyPlanQueryCriteria dailyPlanQueryCriteria = new DailyPlanQueryCriteria();
            dailyPlanQueryCriteria.setBatchPlanId(batchPlan.getId());
            List<DailyPlan> dailyPlanList = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, dailyPlanQueryCriteria, criteriaBuilder));

            if (dailyPlanList != null && dailyPlanList.size() > 0) {
                Map<String, List<DailyPlan>> groupByMap = dailyPlanList.stream().collect(Collectors.groupingBy(DailyPlan::getManufactureName));
                for (Map.Entry<String, List<DailyPlan>> entry : groupByMap.entrySet()) {
                    PlanBoardDto planBoardDto = new PlanBoardDto();
                    planBoardDto.setBatchPlanName(batchPlan.getBatchPlanName());
                    planBoardDto.setStartDate(batchPlan.getStartDate());
                    planBoardDto.setEndDate(batchPlan.getEndDate());
                    planBoardDto.setManufactureAddress(batchPlan.getManufactureAddress());
                    planBoardDto.setManufactureName(entry.getKey());

                    List<ProductParameter> productParameterList = productParameterMap.get(batchPlan.getProductName() + "_" + entry.getKey());
                    if (productParameterList == null || productParameterList.size() == 0) {
                        continue;
                    }
                    ProductParameter productParameter = productParameterList.get(0);
                    planBoardDto.setSerialNumber(productParameter.getSerialNumber() == null ? 1000 : productParameter.getSerialNumber());
                    planBoardDto.setPlanQuantity(batchPlan.getBatchPlanQuantity() * productParameter.getUnitsQuantity());
                    planBoardDto.setSecondaryType(productParameter.getTechniqueInfo().getCategory().getSecondaryType());
                    planBoardDto.setProcessSequence(productParameter.getTechniqueInfo().getCategory().getProcessSequence() == null ? 1000 : productParameter.getTechniqueInfo().getCategory().getProcessSequence());
                    Integer count = entry.getValue().stream().mapToInt(a -> {
                        return a.getCompletedQuantity();
                    }).sum();

                    planBoardDto.setCompletedQuantity(count);
                    planBoardDto.setRemainQuantity(planBoardDto.getPlanQuantity() - planBoardDto.getCompletedQuantity());

                    DailyPlanQueryCriteria dailyPlanQueryCriteria1 = new DailyPlanQueryCriteria();
                    dailyPlanQueryCriteria1.setBatchNumber(batchPlan.getBatchNumber());
                    dailyPlanQueryCriteria1.setManufactureName(entry.getKey());
                    dailyPlanQueryCriteria1.setStartDate(today);
                    List<DailyPlan> dailyPlanList1 = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, dailyPlanQueryCriteria1, criteriaBuilder));
                    if (dailyPlanList1 != null && dailyPlanList1.size() > 0) {
                        planBoardDto.setTodayPlan(dailyPlanList1.get(0).getDailyPlanQuantity());
                        planBoardDto.setTomorrowPlan(dailyPlanList1.get(0).getDailyPlanQuantity());
                    }
                    planBoardDtoList.add(planBoardDto);
                }
            }
        }

        if (boardQueryCriteria.getSecondaryType() != null) {
            planBoardDtoList = planBoardDtoList.stream().filter(a -> {
                return boardQueryCriteria.getSecondaryType().equals(a.getSecondaryType());
            }).collect(Collectors.toList());
        }
        if (boardQueryCriteria.getManufactureName() != null) {
            planBoardDtoList = planBoardDtoList.stream().filter(a -> {
                return boardQueryCriteria.getManufactureName().equals(a.getManufactureName());
            }).collect(Collectors.toList());
        }

        planBoardDtoList = planBoardDtoList.stream().sorted(Comparator.comparing(PlanBoardDto::getManufactureAddress, Comparator.reverseOrder()).thenComparing(PlanBoardDto::getProcessSequence).thenComparing(PlanBoardDto::getSerialNumber)).collect(Collectors.toList());

        return planBoardDtoList;
    }

    public String getYesterdayByCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date time = calendar.getTime();
        String yesterday = dateFormat.format(time);
        return yesterday;
    }

    @Override
    public List<UnfinishedReasonDto> getUnfinishedReasons(BoardQueryCriteria boardQueryCriteria) {

        String today = dateFormat.format(new Date());
        List<UnfinishedReasonDto> unfinishedReasonDtos = new ArrayList<>();
        DailyPlanQueryCriteria dailyPlanQueryCriteria = new DailyPlanQueryCriteria();
        if (boardQueryCriteria.getManufactureAddress() != null) {
            dailyPlanQueryCriteria.setManufactureAddress(boardQueryCriteria.getManufactureAddress());
        } else if (boardQueryCriteria.getManufactureName() != null) {
            dailyPlanQueryCriteria.setManufactureName(boardQueryCriteria.getManufactureName());
        }
        List<DailyPlan> dailyPlanList = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, dailyPlanQueryCriteria, criteriaBuilder));

        dailyPlanList = dailyPlanList.stream().filter(a -> {
            return a.getDailyPlanQuantity() > a.getCompletedQuantity();
        }).collect(Collectors.toList());

        Map<String, List<DailyPlan>> dailyPlanMap = dailyPlanList.stream().collect(Collectors.groupingBy(a -> {
            return a.getStartDate();
        }));

        List<DailyPlanGroupDto> dailyPlanGroupDtos = dailyPlanMap.entrySet().stream().map(e -> new DailyPlanGroupDto(e.getKey(), e.getValue())).collect(Collectors.toList());

        dailyPlanGroupDtos = dailyPlanGroupDtos.stream().sorted(Comparator.comparing(DailyPlanGroupDto::getStartDate, Comparator.reverseOrder())).collect(Collectors.toList());

        List<ProductParameter> productParameterList1 = productParameterRepository.findAll();
        if (productParameterList1 == null || productParameterList1.size() == 0) {
            return null;
        }
        Map<String, List<ProductParameter>> productParameterMap = productParameterList1.stream().collect(Collectors.groupingBy(a -> {
            return a.getProductName() + "_" + a.getManufactureName();
        }));

        List<Manufacture> manufactureList = manufactureRepository.findAll();
        manufactureList = manufactureList.stream().filter(a -> {
            return a.getPlanNumber() != null;
        }).collect(Collectors.toList());
        if (manufactureList == null || manufactureList.size() == 0) {
            return null;
        }
        Map<String, List<Manufacture>> manufactureMap = manufactureList.stream().collect(Collectors.groupingBy(a -> {
            return a.getPlanNumber();
        }));

        boolean flag = false;
        for (int i = 0; i < dailyPlanGroupDtos.size(); i++) {
            if (flag) {
                break;
            }
            List<DailyPlan> dailyPlanList1 = dailyPlanGroupDtos.get(i).getDailyPlanList();
            for (DailyPlan dailyPlan : dailyPlanList1) {
                UnfinishedReasonDto unfinishedReasonDto = new UnfinishedReasonDto();
                unfinishedReasonDto.setDailyPlanQuantity(dailyPlan.getDailyPlanQuantity());
                unfinishedReasonDto.setFillDate(dailyPlan.getStartDate());
                unfinishedReasonDto.setManufactureAddress(dailyPlan.getManufactureAddress());
                unfinishedReasonDto.setManufactureName(dailyPlan.getManufactureName());
                unfinishedReasonDto.setActualQuantity(dailyPlan.getCompletedQuantity());
                unfinishedReasonDto.setUnfinishedQuantity(unfinishedReasonDto.getDailyPlanQuantity() - unfinishedReasonDto.getActualQuantity());
                List<Manufacture> manufactures = manufactureMap.get(dailyPlan.getPlanNumber());
                if (manufactures == null || manufactures.size() == 0) {
                    continue;
                }

                Manufacture manufacture = manufactures.get(0);
                if ("无人报工".equals(manufacture.getIncompleteReasons()) && manufacture.getDailyOutput() == 0) {
                    continue;
                }
                unfinishedReasonDto.setIncompleteReasons(manufactures.get(0).getIncompleteReasons());
                List<ProductParameter> productParameterList = productParameterMap.get(dailyPlan.getBatchPlan().getProductName() + "_" + dailyPlan.getManufactureName());
                if (productParameterList != null && productParameterList.size() > 0) {
                    TechniqueInfo techniqueInfo = productParameterList.get(0).getTechniqueInfo();
                    unfinishedReasonDto.setSerialNumber(productParameterList.get(0).getSerialNumber() == null ? 10000 : productParameterList.get(0).getSerialNumber());
                    if (techniqueInfo != null && techniqueInfo.getCategory() != null) {
                        unfinishedReasonDto.setSecondaryType(techniqueInfo.getCategory().getSecondaryType());
                        unfinishedReasonDto.setProcessSequence(techniqueInfo.getCategory().getProcessSequence() == null ? 1000 : techniqueInfo.getCategory().getProcessSequence());
                    }
                }
                unfinishedReasonDtos.add(unfinishedReasonDto);

                if (i > 0 || today.compareTo(dailyPlanGroupDtos.get(i).getStartDate()) > 0) {
                    flag = true;
                }
            }
        }

//        unfinishedReasonDtos = unfinishedReasonDtos.stream().filter(a -> {
//            return !("无人报工".equals(a.getIncompleteReasons()) && a.getActualQuantity() == 0);
//        }).collect(Collectors.toList());

        if (boardQueryCriteria.getSecondaryType() != null) {
            unfinishedReasonDtos = unfinishedReasonDtos.stream().filter(a -> {
                return boardQueryCriteria.getSecondaryType().equals(a.getSecondaryType());
            }).collect(Collectors.toList());
        }

        unfinishedReasonDtos = unfinishedReasonDtos.stream().sorted(Comparator.comparing(UnfinishedReasonDto::getFillDate, Comparator.reverseOrder()).thenComparing(UnfinishedReasonDto::getManufactureAddress).thenComparing(UnfinishedReasonDto::getProcessSequence).thenComparing(UnfinishedReasonDto::getSerialNumber)).collect(Collectors.toList());

        return unfinishedReasonDtos;
    }
}
