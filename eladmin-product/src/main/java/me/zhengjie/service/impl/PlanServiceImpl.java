package me.zhengjie.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.BatchPlan;
import me.zhengjie.domain.DailyPlan;
import me.zhengjie.mapper.BatchPlanMapper;
import me.zhengjie.mapper.DailyPlanMapper;
import me.zhengjie.repository.BatchPlanRepository;
import me.zhengjie.repository.DailyPlanRepository;
import me.zhengjie.service.ManufactureService;
import me.zhengjie.service.PlanService;
import me.zhengjie.service.dto.BatchPlanDto;
import me.zhengjie.service.dto.BatchPlanQueryCriteria;
import me.zhengjie.service.dto.DailyPlanDto;
import me.zhengjie.service.dto.DailyPlanQueryCriteria;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HL
 * @create 2021/4/13 11:48
 */
@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    @Resource(name = "batchPlanMapperImpl")
    private BatchPlanMapper batchPlanMapper;

    @Resource(name = "dailyPlanMapperImpl")
    private DailyPlanMapper dailyPlanMapper;

    private final BatchPlanRepository batchPlanRepository;

    private final DailyPlanRepository dailyPlanRepository;

    private final ManufactureService manufactureService;

    final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmssS");
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<BatchPlanDto> findBatchPlan(BatchPlanQueryCriteria criteria) {
        return batchPlanMapper.toDto(batchPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public Map<String, Object> queryBatchPlan(BatchPlanQueryCriteria criteria, Pageable pageable) {
        Page<BatchPlan> page = batchPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(batchPlanMapper::toDto));
    }

    @Override
    public void download(HttpServletResponse response, BatchPlanQueryCriteria criteria) {
        List<BatchPlan> batchPlans = batchPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));

        List<Map<String, Object>> list = new ArrayList<>();
        for (BatchPlan batchPlan : batchPlans) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("生产批次号", batchPlan.getBatchNumber());
            map.put("产品名称", batchPlan.getTechniqueInfo().getProductCode());
            map.put("生产计划开始日期", batchPlan.getStartDate());
            map.put("生产计划结束日期", batchPlan.getEndDate());
            map.put("计划产量", batchPlan.getBatchPlanQuantity());
            list.add(map);
        }
        try {
            FileUtil.downloadExcel(list, response);
        } catch (Exception e) {
        }
    }

    @Override
    public void downloadDailyPlan(HttpServletResponse response, DailyPlanQueryCriteria criteria) {
        List<DailyPlan> dailyPlans = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));

        List<Map<String, Object>> list = new ArrayList<>();
        for (DailyPlan dailyPlan : dailyPlans) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("生产批次号", dailyPlan.getBatchPlan().getBatchNumber());
            map.put("生产计划编号", dailyPlan.getPlanNumber());
            map.put("产品名称", dailyPlan.getBatchPlan().getTechniqueInfo().getProductCode());
            map.put("计划生产日期", dailyPlan.getStartDate());
            map.put("日计划产量", dailyPlan.getDailyPlanQuantity());
            list.add(map);
        }
        try {
            FileUtil.downloadExcel(list, response);
        } catch (Exception e) {
        }
    }

    @Override
    public List<DailyPlan> getDailyPlanSelector() {
        List<DailyPlan> dailyPlanList = dailyPlanRepository.findAll();
        List<DailyPlan> dailyPlans = dailyPlanList.stream().filter(dailyPlan -> {
            if (dailyPlan.getDailyPlanQuantity() > dailyPlan.getCompletedQuantity()) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return dailyPlans;
    }

    @Override
    public List<BatchPlan> getBatchPlanSelector() {
//        String date = dateFormat.format(new Date());
//        BatchPlanQueryCriteria criteria = new BatchPlanQueryCriteria();
//        criteria.setStartDate(date);
//        criteria.setEndDate(date);
//        List<BatchPlan> batchPlanList = batchPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
//        batchPlanList.stream().forEach(batchPlan -> {
//            DailyPlanQueryCriteria dailyPlanQueryCriteria = new DailyPlanQueryCriteria();
//            dailyPlanQueryCriteria.setBatchPlanId();
//            List<DailyPlan> dailyPlanList =  dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
//        });
        List<BatchPlan> batchPlanList = batchPlanRepository.findAll();
        List<BatchPlan> batchPlans = batchPlanList.stream().filter(batchPlan -> {
            if (batchPlan.getBatchPlanQuantity() > batchPlan.getCompletedQuantity() && batchPlan.getDailyPlanRemain() > 0) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return batchPlans;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public BatchPlan createBatchPlan(BatchPlan resources) {
        resources.setBatchNumber(SDF.format(new Date()));
        resources.setDailyPlanRemain(resources.getBatchPlanQuantity());
        return batchPlanRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBatchPlan(BatchPlan resources) {
        BatchPlan batchPlan = batchPlanRepository.findById(resources.getId()).orElseGet(BatchPlan::new);
        ValidationUtil.isNull(batchPlan.getId(), "batchPlan", "id", resources.getId());

        if (resources.getBatchPlanQuantity() != null && !resources.getBatchPlanQuantity().equals(batchPlan.getBatchPlanQuantity())) {
            batchPlan.copy(resources);
            batchPlanRepository.save(batchPlan);
            final DailyPlanQueryCriteria dailyPlanQueryCriteria1 = new DailyPlanQueryCriteria();
            dailyPlanQueryCriteria1.setBatchPlanId(batchPlan.getId());
            List<DailyPlan> allDailyPlan = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, dailyPlanQueryCriteria1, criteriaBuilder));
            allDailyPlan.forEach(d -> {
                manufactureService.summary(d.getPlanNumber());
            });

            updateDailyPlanRemain(resources.getId());
        } else {
            batchPlan.copy(resources);
            batchPlanRepository.save(batchPlan);
        }


        //TODO

//        manufactureService.summary(batchPlan.get);

 /*       if(resources.getBatchPlanQuantity() == batchPlan.getBatchPlanQuantity()){
            batchPlan.copy(resources);
            batchPlanRepository.save(batchPlan);
        } else if(resources.getBatchPlanQuantity() > batchPlan.getBatchPlanQuantity()){
            resources.setBatchPlanQuantity(resources.getBatchPlanQuantity()- batchPlan.getBatchPlanQuantity());
            resources.setId(null);
            batchPlanRepository.save(resources);
        }else{
            //TODO 重新计算自动汇总的批量完成率


            batchPlan.copy(resources);
            batchPlanRepository.save(batchPlan);
        }*/
    }

    @Override
    public List<DailyPlanDto> findDailyPlan(DailyPlanQueryCriteria criteria) {
        return dailyPlanMapper.toDto(dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DailyPlan createDailyPlan(DailyPlan resources) {
        DailyPlanQueryCriteria dailyPlanQueryCriteria = new DailyPlanQueryCriteria();
        dailyPlanQueryCriteria.setStartDate(resources.getStartDate());
        dailyPlanQueryCriteria.setBatchPlanId(resources.getBatchPlan().getId());
        List<DailyPlan> dailyPlanList = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, dailyPlanQueryCriteria, criteriaBuilder));
        if (dailyPlanList != null && dailyPlanList.size() > 0)
            return null;

        resources.setPlanNumber(SDF.format(new Date()));
        DailyPlan dailyPlan = dailyPlanRepository.save(resources);

        updateDailyPlanRemain(resources.getBatchPlan().getId());

        return dailyPlan;
    }


    private void updateDailyPlanRemain(Integer batchId) {
        BatchPlan batchPlan = batchPlanRepository.getOne(batchId);
        DailyPlanQueryCriteria criteria = new DailyPlanQueryCriteria();
        criteria.setBatchPlanId(batchId);
        List<DailyPlan> dailyPlans = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        Double quantity = dailyPlans.stream().mapToDouble(manufacture -> {
            return manufacture.getDailyPlanQuantity();
        }).sum();

        batchPlan.setDailyPlanRemain(batchPlan.getBatchPlanQuantity() - quantity.intValue());
        batchPlanRepository.save(batchPlan);
    }

    @Override
    public Map<String, Object> queryDailyPlan(DailyPlanQueryCriteria criteria, Pageable pageable) {
        Page<DailyPlan> page = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(dailyPlanMapper::toDto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDailyPlan(DailyPlan resources) {
        DailyPlan dailyPlan = dailyPlanRepository.findById(resources.getId()).orElseGet(DailyPlan::new);
        ValidationUtil.isNull(dailyPlan.getId(), "batchPlan", "id", resources.getId());

        if (resources.getDailyPlanQuantity() != null && !resources.getDailyPlanQuantity().equals(dailyPlan.getDailyPlanQuantity())) {
            dailyPlan.copy(resources);
            dailyPlanRepository.save(dailyPlan);
            manufactureService.summary(resources.getPlanNumber());
            updateDailyPlanRemain(resources.getBatchPlan().getId());
        } else {
            dailyPlan.copy(resources);
            dailyPlanRepository.save(dailyPlan);
        }

    }

}
