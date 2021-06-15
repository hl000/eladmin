package me.zhengjie.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.*;
import me.zhengjie.repository.BatchPlanRepository;
import me.zhengjie.repository.DailyPlanRepository;
import me.zhengjie.repository.ManufactureRepository;
import me.zhengjie.repository.ProductParameterRepository;
import me.zhengjie.service.BoardService;
import me.zhengjie.service.dto.BatchPlanQueryCriteria;
import me.zhengjie.service.dto.DailyPlanQueryCriteria;
import me.zhengjie.service.dto.ManufactureQueryCriteria;
import me.zhengjie.service.dto.ProductParameterQueryCriteria;
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
    public List<PlanBoardDto> getPlanBoard() {
        String today = dateFormat.format(new Date());
        BatchPlanQueryCriteria batchPlanQueryCriteria = new BatchPlanQueryCriteria();
        batchPlanQueryCriteria.setStartDate(today);
        batchPlanQueryCriteria.setEndDate(today);
        List<BatchPlan> batchPlanList = batchPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, batchPlanQueryCriteria, criteriaBuilder));

        batchPlanList.stream().sorted(Comparator.comparing(BatchPlan::getId, Comparator.reverseOrder())).collect(Collectors.toList());
        List<PlanBoardDto> planBoardDtoList = new ArrayList<>();
        for (BatchPlan batchPlan : batchPlanList) {
            DailyPlanQueryCriteria dailyPlanQueryCriteria = new DailyPlanQueryCriteria();
            dailyPlanQueryCriteria.setBatchPlanId(batchPlan.getId());
            List<DailyPlan> dailyPlanList = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, dailyPlanQueryCriteria, criteriaBuilder));

            ProductParameterQueryCriteria productParameterQueryCriteria = new ProductParameterQueryCriteria();
            productParameterQueryCriteria.setProductName(batchPlan.getProductName());
            List<ProductParameter> productParameterList = productParameterRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, productParameterQueryCriteria, criteriaBuilder));
            if (productParameterList == null || productParameterList.size() == 0) {
                return null;
            }

            if (dailyPlanList != null && dailyPlanList.size() > 0) {
                Map<String, List<DailyPlan>> groupByMap = dailyPlanList.stream().collect(Collectors.groupingBy(DailyPlan::getManufactureName));
                for (Map.Entry<String, List<DailyPlan>> entry : groupByMap.entrySet()) {
                    PlanBoardDto planBoardDto = new PlanBoardDto();
                    planBoardDto.setBatchPlanName(batchPlan.getBatchPlanName());
                    planBoardDto.setStartDate(batchPlan.getStartDate());
                    planBoardDto.setEndDate(batchPlan.getEndDate());
                    planBoardDto.setManufactureAddress(batchPlan.getManufactureAddress());
                    planBoardDto.setManufactureName(entry.getKey());
                    ProductParameter productParameter = productParameterList.stream().filter(a -> a.getManufactureName().equals(entry.getKey())).collect(Collectors.toList()).get(0);
                    planBoardDto.setPlanQuantity(batchPlan.getBatchPlanQuantity() * productParameter.getUnitsQuantity());


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
        return planBoardDtoList;
    }

    @Override
    public List<UnfinishedReasonDto> getUnfinishedReasons() {

        List<UnfinishedReasonDto> unfinishedReasonDtos = new ArrayList<>();
        List<DailyPlan> dailyPlanList = dailyPlanRepository.findAll();
        dailyPlanList.stream().filter(a -> {
            return a.getDailyPlanQuantity() > a.getCompletedQuantity();
        }).collect(Collectors.toList());

        dailyPlanList.stream().sorted(Comparator.comparing(DailyPlan::getId, Comparator.reverseOrder())).collect(Collectors.toList());
        for (DailyPlan dailyPlan : dailyPlanList) {
            UnfinishedReasonDto unfinishedReasonDto = new UnfinishedReasonDto();
            ManufactureQueryCriteria manufactureQueryCriteria = new ManufactureQueryCriteria();
            manufactureQueryCriteria.setPlanNumber(dailyPlan.getPlanNumber());

            unfinishedReasonDto.setDailyPlanQuantity(dailyPlan.getDailyPlanQuantity());
            unfinishedReasonDto.setFillDate(dailyPlan.getStartDate());
            unfinishedReasonDto.setManufactureAddress(dailyPlan.getManufactureAddress());
            unfinishedReasonDto.setManufactureName(dailyPlan.getManufactureName());
            unfinishedReasonDto.setActualQuantity(dailyPlan.getCompletedQuantity());

            List<Manufacture> manufactures = manufactureRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, manufactureQueryCriteria, criteriaBuilder));
            if (manufactures != null && manufactures.size() > 0) {
                unfinishedReasonDto.setIncompleteReasons(manufactures.get(0).getIncompleteReasons());
            }

            unfinishedReasonDtos.add(unfinishedReasonDto);
        }

        return unfinishedReasonDtos;
    }
}
