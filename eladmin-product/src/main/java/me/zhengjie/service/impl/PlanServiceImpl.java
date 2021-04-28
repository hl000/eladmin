package me.zhengjie.service.impl;

import cn.hutool.json.JSONObject;
import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.BatchPlan;
import me.zhengjie.domain.DailyPlan;
import me.zhengjie.domain.ProductParameter;
import me.zhengjie.domain.TechniqueInfo;
import me.zhengjie.mapper.BatchPlanMapper;
import me.zhengjie.mapper.DailyPlanMapper;
import me.zhengjie.repository.BatchPlanRepository;
import me.zhengjie.repository.DailyPlanRepository;
import me.zhengjie.repository.ProductParameterRepository;
import me.zhengjie.repository.TechniqueInfoRepository;
import me.zhengjie.service.ManufactureService;
import me.zhengjie.service.PlanService;
import me.zhengjie.service.dto.*;
import me.zhengjie.utils.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
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

    private final ProductParameterRepository productParameterRepository;

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
            map.put("产品名称", batchPlan.getProductName());
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
            map.put("部件名称", dailyPlan.getManufactureName());
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
    public List<DailyPlanDto> getDailyPlanSelector() {
        List<DailyPlan> dailyPlanList = dailyPlanRepository.findAll();
        List<DailyPlan> dailyPlans = dailyPlanList.stream().filter(dailyPlan -> {
            if (dailyPlan.getDailyPlanQuantity() > dailyPlan.getCompletedQuantity() && dailyPlan.getDailyPlanQuantity()!=0) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        List<DailyPlanDto> dailyPlanDtos = dailyPlanMapper.toDto(dailyPlans);

        UserDetails userDetails = SecurityUtils.getCurrentUser();
        Long userId = (Long)new JSONObject(new JSONObject(userDetails).get("user")).get("id");
        List<DailyPlanDto>  dailyPlanDtoList = new ArrayList<>();
         for(DailyPlanDto dailyPlanDto : dailyPlanDtos ){
             ProductParameterQueryCriteria productParameterQueryCriteria = new ProductParameterQueryCriteria();
             productParameterQueryCriteria.setProductName(dailyPlanDto.getBatchPlan().getProductName());
             productParameterQueryCriteria.setManufactureName(dailyPlanDto.getManufactureName());
             List<ProductParameter>  productParameterList = productParameterRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, productParameterQueryCriteria, criteriaBuilder));
            if(productParameterList !=null && productParameterList.size()>0){
                ProductParameter productParameter = productParameterList.get(0);
                if(productParameter.getPermissionUserIds()!=null){
                   String[] userIds = productParameter.getPermissionUserIds().split(",");
                   for(int i=0;i<userIds.length;i++){
                       if(userIds[i].equals(userId.toString())){
                           dailyPlanDto.setWorkerQuantity(productParameter.getWorkerQuantity());
                           dailyPlanDto.setWorkHours(productParameter.getWorkHours());
                           dailyPlanDtoList.add(dailyPlanDto);
                       }
                   }
                }else{
                    dailyPlanDto.setWorkerQuantity(productParameter.getWorkerQuantity());
                    dailyPlanDto.setWorkHours(productParameter.getWorkHours());
                    dailyPlanDtoList.add(dailyPlanDto);
                }
            }
        }
        return dailyPlanDtoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BatchPlan createBatchPlan(BatchPlan resources) {
        resources.setBatchNumber(SDF.format(new Date()));
        BatchPlanQueryCriteria batchPlanQueryCriteria = new BatchPlanQueryCriteria();
        batchPlanQueryCriteria.setBatchPlanName(resources.getBatchPlanName());
        List<BatchPlan> batchPlans = batchPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, batchPlanQueryCriteria, criteriaBuilder));
        if(batchPlans!=null && batchPlans.size()>0)
            return null;
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
                try {
                    manufactureService.summary(d.getPlanNumber());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
        } else {
            batchPlan.copy(resources);
            batchPlanRepository.save(batchPlan);
        }

//        batchPlan.copy(resources);
//        batchPlanRepository.save(batchPlan);
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
        dailyPlanQueryCriteria.setManufactureName(resources.getManufactureName());
        List<DailyPlan> dailyPlanList = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, dailyPlanQueryCriteria, criteriaBuilder));
        if (dailyPlanList != null && dailyPlanList.size() > 0)
            return null;

        resources.setPlanNumber(SDF.format(new Date()));
        return dailyPlanRepository.save(resources);
    }

    @Override
    public Map<String, Object> queryDailyPlan(DailyPlanQueryCriteria criteria, Pageable pageable) {
        Page<DailyPlan> page = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(dailyPlanMapper::toDto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDailyPlan(DailyPlan resources) throws ParseException {
        DailyPlan dailyPlan = dailyPlanRepository.findById(resources.getId()).orElseGet(DailyPlan::new);
        ValidationUtil.isNull(dailyPlan.getId(), "batchPlan", "id", resources.getId());

        if (resources.getDailyPlanQuantity() != null && !resources.getDailyPlanQuantity().equals(dailyPlan.getDailyPlanQuantity())) {
            dailyPlan.copy(resources);
            dailyPlanRepository.save(dailyPlan);
            manufactureService.summary(dailyPlan.getPlanNumber());
        } else {
            dailyPlan.copy(resources);
            dailyPlanRepository.save(dailyPlan);
        }

//        dailyPlan.copy(resources);
//        dailyPlanRepository.save(dailyPlan);

    }


    @Override
    public List<RemainBatchQuantityDto> getRemainBatchQuantity(ProductParameterQueryCriteria criteria) {
        List<BatchPlan> batchPlans = batchPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));

        if(batchPlans==null || batchPlans.size()==0)
            return null;
        List<RemainBatchQuantityDto> remainDailyQuantities = new ArrayList<>();
        List<ProductParameter>  productParameterList = productParameterRepository.findAll();
        for(int i=0;i<batchPlans.size();i++){
            List<RemainInfoDto> remainInfoDtos = new ArrayList<>();
            for(int j=0;j<productParameterList.size();j++){
                RemainInfoDto remainInfoDto = new RemainInfoDto();
                if(batchPlans.get(i).getProductName().equals(productParameterList.get(j).getProductName())){
                    DailyPlanQueryCriteria dailyPlanQueryCriteria = new DailyPlanQueryCriteria();
                    dailyPlanQueryCriteria.setBatchNumber(batchPlans.get(i).getBatchNumber());
                    dailyPlanQueryCriteria.setManufactureName(productParameterList.get(j).getManufactureName());
                    List<DailyPlan> dailyPlanList = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, dailyPlanQueryCriteria, criteriaBuilder));

                   Integer dailyPlanTotal=  dailyPlanList.stream().mapToInt(dailyPlan -> {
                        return dailyPlan.getDailyPlanQuantity();
                    }).sum();

                    if(batchPlans.get(i).getBatchPlanQuantity() * productParameterList.get(j).getUnitsQuantity() > dailyPlanTotal){
                        remainInfoDto.setManufactureName(productParameterList.get(j).getManufactureName());
                        remainInfoDto.setRemainDailyQuantity(batchPlans.get(i).getBatchPlanQuantity()*productParameterList.get(j).getUnitsQuantity() - dailyPlanTotal);
                        remainInfoDto.setUnitsQuantity(productParameterList.get(j).getUnitsQuantity());
                        remainInfoDtos.add(remainInfoDto);
                    }
                }
            }
            if(remainInfoDtos!=null && remainInfoDtos.size()>0){
                RemainBatchQuantityDto remainDailyQuantity = new RemainBatchQuantityDto();
                remainDailyQuantity.setBatchPlanId(batchPlans.get(i).getId());
                remainDailyQuantity.setBatchNumber(batchPlans.get(i).getBatchNumber());
                remainDailyQuantity.setProductName(batchPlans.get(i).getProductName());
                remainDailyQuantity.setBatchPlanName(batchPlans.get(i).getBatchPlanName());
                remainDailyQuantity.setRemainInfo(remainInfoDtos);
                remainDailyQuantities.add(remainDailyQuantity);
            }
        }

        return remainDailyQuantities;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<DailyPlan> createDailyPlanBatch(DailyPlanBatchDto resources) {
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        Long userId = (Long) new JSONObject(new JSONObject(userDetails).get("user")).get("id");

        List<DailyPlan> dailyPlanList = resources.getDailyPlanList();
        Long planNumber = Long.parseLong(SDF.format(new Date()));
        for(DailyPlan dailyPlan : dailyPlanList){
            dailyPlan.setPlanNumber(planNumber.toString());
            dailyPlan.setBatchPlan(resources.getBatchPlan());
            dailyPlan.setStartDate(resources.getStartDate());
            dailyPlan.setUserId(userId);
            planNumber++;
        }
       return dailyPlanRepository.saveAll(dailyPlanList);
    }

}
