package me.zhengjie.service.impl;

import cn.hutool.json.JSONObject;
import lombok.RequiredArgsConstructor;
import me.zhengjie.Utils.DataChangeUtil;
import me.zhengjie.domain.BatchPlan;
import me.zhengjie.domain.DailyPlan;
import me.zhengjie.domain.Manufacture;
import me.zhengjie.domain.ProductParameter;
import me.zhengjie.mapper.BatchPlanMapper;
import me.zhengjie.mapper.DailyPlanMapper;
import me.zhengjie.repository.BatchPlanRepository;
import me.zhengjie.repository.DailyPlanRepository;
import me.zhengjie.repository.ManufactureRepository;
import me.zhengjie.repository.ProductParameterRepository;
import me.zhengjie.service.ManufactureService;
import me.zhengjie.service.PlanService;
import me.zhengjie.service.dto.*;
import me.zhengjie.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


    private final ManufactureRepository manufactureRepository;


    private final ManufactureService manufactureService;

    private static final Logger log = LoggerFactory.getLogger(RedisUtils.class);


    final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmssS");
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    String str = "http://www.easybots.cn/api/holiday.php?d=20190913";

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
            map.put("生产基地", batchPlan.getManufactureAddress());
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
            map.put("生产基地", dailyPlan.getManufactureAddress());
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
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        String userAddress = (String) new JSONObject(new JSONObject(userDetails).get("user")).get("userAddress");
        DailyPlanQueryCriteria dailyPlanQueryCriteria = new DailyPlanQueryCriteria();
        if (userAddress != null && !"".equals(userAddress)) {
            dailyPlanQueryCriteria.setManufactureAddress(userAddress);
        }
        dailyPlanQueryCriteria.setStartDate(dateFormat.format(new Date()));
        List<DailyPlan> dailyPlanList = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, dailyPlanQueryCriteria, criteriaBuilder));

        List<DailyPlan> dailyPlans = dailyPlanList.stream().filter(dailyPlan -> {
            if (dailyPlan.getDailyPlanQuantity() > dailyPlan.getCompletedQuantity() && dailyPlan.getDailyPlanQuantity() != 0) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        List<DailyPlanDto> dailyPlanDtos = dailyPlanMapper.toDto(dailyPlans);

        Long userId = (Long) new JSONObject(new JSONObject(userDetails).get("user")).get("id");
        List<DailyPlanDto> dailyPlanDtoList = new ArrayList<>();
        ManufactureQueryCriteria manufactureQueryCriteria = new ManufactureQueryCriteria();
        for (DailyPlanDto dailyPlanDto : dailyPlanDtos) {
            manufactureQueryCriteria.setPlanNumber(dailyPlanDto.getPlanNumber());
            List<Manufacture> manufactures = manufactureRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, manufactureQueryCriteria, criteriaBuilder));
            if (manufactures != null && manufactures.size() > 0)
                continue;
            ProductParameterQueryCriteria productParameterQueryCriteria = new ProductParameterQueryCriteria();
            productParameterQueryCriteria.setProductName(dailyPlanDto.getBatchPlan().getProductName());
            productParameterQueryCriteria.setManufactureName(dailyPlanDto.getManufactureName());
            List<ProductParameter> productParameterList = productParameterRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, productParameterQueryCriteria, criteriaBuilder));
            if (productParameterList != null && productParameterList.size() > 0) {
                ProductParameter productParameter = productParameterList.get(0);
                if (productParameter.getPermissionUserIds() != null) {
                    String[] userIds = productParameter.getPermissionUserIds().split(",");
                    for (int i = 0; i < userIds.length; i++) {
                        if (userIds[i].equals(userId.toString())) {
                            setDailyPlan(dailyPlanDto, productParameter);
                            dailyPlanDtoList.add(dailyPlanDto);
                        }
                    }
                } else {
                    setDailyPlan(dailyPlanDto, productParameter);
                    dailyPlanDtoList.add(dailyPlanDto);
                }
            }
        }
        return dailyPlanDtoList;
    }

    private void setDailyPlan(DailyPlanDto dailyPlanDto, ProductParameter productParameter) {
        dailyPlanDto.setWorkerQuantity(productParameter.getWorkerQuantity());
        dailyPlanDto.setWorkHours(productParameter.getWorkHours());
        dailyPlanDto.setMaterial1Name(productParameter.getTechniqueInfo().getMaterial1Name());
        dailyPlanDto.setMaterial2Name(productParameter.getTechniqueInfo().getMaterial2Name());
        dailyPlanDto.setMaterial3Name(productParameter.getTechniqueInfo().getMaterial3Name());
        dailyPlanDto.setMaterial4Name(productParameter.getTechniqueInfo().getMaterial4Name());
        dailyPlanDto.setMaterial1Unit(productParameter.getTechniqueInfo().getMaterial1Unit());
        dailyPlanDto.setMaterial2Unit(productParameter.getTechniqueInfo().getMaterial2Unit());
        dailyPlanDto.setMaterial3Unit(productParameter.getTechniqueInfo().getMaterial3Unit());
        dailyPlanDto.setMaterial4Unit(productParameter.getTechniqueInfo().getMaterial4Unit());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BatchPlan createBatchPlan(BatchPlan resources) {
        resources.setBatchNumber(SDF.format(new Date()));
        BatchPlanQueryCriteria batchPlanQueryCriteria = new BatchPlanQueryCriteria();
        batchPlanQueryCriteria.setBatchPlanName(resources.getBatchPlanName());
        List<BatchPlan> batchPlans = batchPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, batchPlanQueryCriteria, criteriaBuilder));
        if (batchPlans != null && batchPlans.size() > 0)
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
    public List<RemainBatchQuantityDto> getRemainBatchQuantity(BatchPlanQueryCriteria criteria) {
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        String userAddress = (String) new JSONObject(new JSONObject(userDetails).get("user")).get("userAddress");
        if (userAddress != null && !"".equals(userAddress)) {
            criteria.setManufactureAddress(userAddress);
        }
        List<BatchPlan> batchPlans = batchPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));

        if (batchPlans == null || batchPlans.size() == 0)
            return null;
        List<RemainBatchQuantityDto> remainDailyQuantities = new ArrayList<>();
        List<ProductParameter> productParameterList = productParameterRepository.findAll();
        for (int i = 0; i < batchPlans.size(); i++) {
            List<RemainInfoDto> remainInfoDtos = new ArrayList<>();
            for (int j = 0; j < productParameterList.size(); j++) {
                RemainInfoDto remainInfoDto = new RemainInfoDto();
                if (batchPlans.get(i).getProductName().equals(productParameterList.get(j).getProductName())) {
                    DailyPlanQueryCriteria dailyPlanQueryCriteria = new DailyPlanQueryCriteria();
                    dailyPlanQueryCriteria.setBatchNumber(batchPlans.get(i).getBatchNumber());
                    dailyPlanQueryCriteria.setManufactureName(productParameterList.get(j).getManufactureName());
                    List<DailyPlan> dailyPlanList = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, dailyPlanQueryCriteria, criteriaBuilder));

                    Integer dailyPlanTotal = dailyPlanList.stream().mapToInt(dailyPlan -> {
                        return dailyPlan.getDailyPlanQuantity();
                    }).sum();

                    if (batchPlans.get(i).getBatchPlanQuantity() * productParameterList.get(j).getUnitsQuantity() > dailyPlanTotal) {
                        remainInfoDto.setManufactureName(productParameterList.get(j).getManufactureName());
                        remainInfoDto.setRemainDailyQuantity(batchPlans.get(i).getBatchPlanQuantity() * productParameterList.get(j).getUnitsQuantity() - dailyPlanTotal);
                        remainInfoDto.setUnitsQuantity(productParameterList.get(j).getUnitsQuantity());
                        remainInfoDto.setSerialNumber(productParameterList.get(j).getSerialNumber());
                        remainInfoDtos.add(remainInfoDto);
                    }
                }
            }
            if (remainInfoDtos != null && remainInfoDtos.size() > 0) {
                RemainBatchQuantityDto remainDailyQuantity = new RemainBatchQuantityDto();
                remainDailyQuantity.setBatchPlanId(batchPlans.get(i).getId());
                remainDailyQuantity.setBatchNumber(batchPlans.get(i).getBatchNumber());
                remainDailyQuantity.setProductName(batchPlans.get(i).getProductName());
                remainDailyQuantity.setBatchPlanName(batchPlans.get(i).getBatchPlanName());
                remainDailyQuantity.setManufactureAddress(batchPlans.get(i).getManufactureAddress());
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
        for (DailyPlan dailyPlan : dailyPlanList) {
            dailyPlan.setPlanNumber(planNumber.toString());
            dailyPlan.setBatchPlan(resources.getBatchPlan());
            dailyPlan.setStartDate(resources.getStartDate());
            dailyPlan.setUserId(userId);
            dailyPlan.setManufactureAddress(resources.getManufactureAddress());
            planNumber++;
        }
        return dailyPlanRepository.saveAll(dailyPlanList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDailyPlan() throws ParseException {

        String today = dateFormat.format(new Date());
        log.info("today:" + today);
        BatchPlanQueryCriteria batchPlanQueryCriteria = new BatchPlanQueryCriteria();
        batchPlanQueryCriteria.setStartDate(today);
        batchPlanQueryCriteria.setEndDate(today);
        List<BatchPlan> batchPlans = batchPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, batchPlanQueryCriteria, criteriaBuilder));
        if (batchPlans != null && batchPlans.size() > 0) {
            List<ProductParameter> productParameterList = productParameterRepository.findAll();
            List<DailyPlan> dailyPlans = new ArrayList<>();
            Long planNumber = Long.parseLong(SDF.format(new Date()));
            for (int i = 0; i < batchPlans.size(); i++) {
                int days = getDutyDays(today, batchPlans.get(i).getEndDate());
                log.info("day and endDate differ:" + days);

                for (int j = 0; j < productParameterList.size(); j++) {
                    DailyPlan dailyPlan = new DailyPlan();
                    if (batchPlans.get(i).getProductName().equals(productParameterList.get(j).getProductName())) {
                        DailyPlanQueryCriteria dailyPlanQueryCriteria = new DailyPlanQueryCriteria();
                        dailyPlanQueryCriteria.setBatchNumber(batchPlans.get(i).getBatchNumber());
                        dailyPlanQueryCriteria.setManufactureName(productParameterList.get(j).getManufactureName());
                        List<DailyPlan> dailyPlanList = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, dailyPlanQueryCriteria, criteriaBuilder));

                        Integer dailyCompletedTotal = dailyPlanList.stream().mapToInt(plan -> {
                            return plan.getCompletedQuantity();
                        }).sum();
                        log.info("batch plan:" + batchPlans.get(i).getBatchNumber() + "plan quantity:" + batchPlans.get(i).getBatchPlanQuantity() + ",completedQuantity:" + dailyCompletedTotal);

                        Integer remainQuantity = batchPlans.get(i).getBatchPlanQuantity() * productParameterList.get(j).getUnitsQuantity() - dailyCompletedTotal;

                        if (remainQuantity > 0) {
                            dailyPlan.setDailyPlanQuantity(Double.valueOf(Math.ceil(remainQuantity * 1.0 / days)).intValue());
                            dailyPlan.setBatchPlan(batchPlans.get(i));
                            dailyPlan.setPlanNumber(planNumber.toString());
                            dailyPlan.setStartDate(dateFormat.format(new Date()));
                            dailyPlan.setManufactureAddress(batchPlans.get(i).getManufactureAddress());
                            dailyPlan.setManufactureName(productParameterList.get(j).getManufactureName());
                            dailyPlan.setUserId(batchPlans.get(i).getUserId());
                            dailyPlan.setSerialNumber(productParameterList.get(j).getSerialNumber());
                            DailyPlanQueryCriteria dailyPlanQueryCriteria1 = new DailyPlanQueryCriteria();
                            dailyPlanQueryCriteria1.setBatchPlanId(batchPlans.get(i).getId());
                            dailyPlanQueryCriteria1.setManufactureName(dailyPlan.getManufactureName());
                            dailyPlanQueryCriteria1.setStartDate(dailyPlan.getStartDate());

                            List<DailyPlan> dailyPlanList1 = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, dailyPlanQueryCriteria1, criteriaBuilder));
                            if (dailyPlanList1 == null || dailyPlanList1.size() == 0) {
                                dailyPlans.add(dailyPlan);
                                planNumber++;
                            }
                        }
                    }
                }
            }
            dailyPlanRepository.saveAll(dailyPlans);
        }


    }

    @SuppressWarnings("deprecation")
    public int getDutyDays(String strStartDate, String strEndDate) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date startDate = null;
        Date endDate = null;

        Object dataList = DateUtil.getHolidayJson(format.format(df.parse(strStartDate)) + "," + format.format(df.parse(strEndDate))).get("data");
        List<String> list = DataChangeUtil.castList(dataList, String.class);

        try {
            startDate = df.parse(strStartDate);
            endDate = df.parse(strEndDate);
        } catch (ParseException e) {
            System.out.println("非法的日期格式,无法进行转换");
            e.printStackTrace();
        }
        int result = 0;
        while (startDate.compareTo(endDate) <= 0) {
            if (startDate.getDay() != 0)
                result++;
            startDate.setDate(startDate.getDate() + 1);
        }
        if (list != null && list.size() > 0) {
            result = result - list.size();
        }
        return result > 0 ? result : 1;
    }


}
