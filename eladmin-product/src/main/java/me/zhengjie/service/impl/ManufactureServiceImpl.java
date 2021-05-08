package me.zhengjie.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.*;
import me.zhengjie.mapper.ManufactureMapper;
import me.zhengjie.mapper.ManufactureSummaryMapper;
import me.zhengjie.mapper.SummaryViewMapper;
import me.zhengjie.repository.*;
import me.zhengjie.service.ManufactureService;
import me.zhengjie.service.ProductParameterService;
import me.zhengjie.service.dto.*;
import me.zhengjie.utils.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author HL
 * @create 2021/4/13 11:48
 */
@Service
@RequiredArgsConstructor
public class ManufactureServiceImpl implements ManufactureService {

    @Resource(name = "manufactureMapperImpl")
    private ManufactureMapper manufactureMapper;

    private final ManufactureRepository manufactureRepository;

    private final DailyPlanRepository dailyPlanRepository;

    private final ProductParameterRepository productParameterRepository;

    @Resource(name = "manufactureSummaryMapperImpl")
    private ManufactureSummaryMapper manufactureSummaryMapper;

    private final ManufactureSummaryRepository manufactureSummaryRepository;

    private final ProductParameterService productParameterService;

    private final SummaryViewRepository summaryViewRepository;

    @Resource(name = "summaryViewMapperImpl")
    private SummaryViewMapper summaryViewMapper;

    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    //制造信息报工
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ManufactureDto create(Manufacture resources) {
        ManufactureQueryCriteria criteria = new ManufactureQueryCriteria();
        criteria.setPlanNumber(resources.getPlanNumber());
        criteria.setManufactureName(resources.getManufactureName());

        String dateTime = dateFormat.format(new Date());
        List<Manufacture> manufactures = manufactureRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        if (manufactures != null && manufactures.size() > 0) {
            for (Manufacture manufacture : manufactures) {
                if (dateFormat.format(manufacture.getFillDate()).equals(dateTime))
                    return null;
            }
        }

        Manufacture manufacture = manufactureRepository.save(resources);
        updateDailyCompletedQuantity(resources.getPlanNumber());
        summary(resources.getPlanNumber());
//        //新增后将对应的日计划effect改成true
//        DailyPlanQueryCriteria dailyPlanQueryCriteria = new DailyPlanQueryCriteria();
//        dailyPlanQueryCriteria.setPlanNumber(resources.getPlanNumber());
//        List<DailyPlan> dailyPlanList = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, dailyPlanQueryCriteria, criteriaBuilder));
//
//        if (dailyPlanList != null && dailyPlanList.size() > 0) {
//            dailyPlanList.forEach(dailyPlan -> {
//                dailyPlan.setEffect(true);
//                dailyPlanRepository.save(dailyPlan);
//            });
//        }
        return manufactureMapper.toDto(manufacture);
    }

    public void summary(String planNumber) {
        ManufactureQueryCriteria manufactureQueryCriteria = new ManufactureQueryCriteria();
        manufactureQueryCriteria.setPlanNumber(planNumber);

        //获取该日计划下的所有报工
        List<Manufacture> manufactureList = manufactureRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, manufactureQueryCriteria, criteriaBuilder));
        if (manufactureList == null || manufactureList.size() <= 0) { //如果没有报工，则不产生汇总数据
            return;
        }

        DailyPlanQueryCriteria dailyPlanQueryCriteria = new DailyPlanQueryCriteria();
        dailyPlanQueryCriteria.setPlanNumber(planNumber);
        List<DailyPlan> dailyPlanList = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, dailyPlanQueryCriteria, criteriaBuilder));
        if (dailyPlanList == null || dailyPlanList.size() <= 0) { //如果没有日计划，则不产生汇总数据
            return;
        }

        DailyPlan dailyPlan = dailyPlanList.get(0);
        List<ProductParameter> productParameterList = productParameterService.getProductParameterByDailyPlan(dailyPlan);
        if (productParameterList == null || productParameterList.size() <= 0) {
            return;
        }
        TechniqueInfo techniqueInfo = productParameterList.get(0).getTechniqueInfo();
        BatchPlan batchPlan = dailyPlan.getBatchPlan();

        //获取日计划下的报工汇总
        ManufactureSummaryQueryCriteria manufactureSummaryQueryCriteria = new ManufactureSummaryQueryCriteria();
        manufactureSummaryQueryCriteria.setPlanNumber(planNumber);
        List<ManufactureSummary> summaryList = manufactureSummaryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, manufactureSummaryQueryCriteria, criteriaBuilder));
        final ManufactureSummary manufactureSummary = new ManufactureSummary();
        if (summaryList != null && summaryList.size() > 0) {
            summaryList.forEach(a -> {
                manufactureSummaryRepository.delete(a);
            });
        }

        manufactureSummary.setPlanNumber(planNumber);
        manufactureSummary.setManufactureName(dailyPlan.getManufactureName());

        manufactureSummary.setMaterial1Name(techniqueInfo.getMaterial1Name());
        manufactureSummary.setMaterial2Name(techniqueInfo.getMaterial2Name());
        manufactureSummary.setMaterial3Name(techniqueInfo.getMaterial3Name());
        manufactureSummary.setMaterial4Name(techniqueInfo.getMaterial4Name());
        manufactureSummary.setMaterial1Unit(techniqueInfo.getMaterial1Unit());
        manufactureSummary.setMaterial2Unit(techniqueInfo.getMaterial2Unit());
        manufactureSummary.setMaterial3Unit(techniqueInfo.getMaterial3Unit());
        manufactureSummary.setMaterial4Unit(techniqueInfo.getMaterial4Unit());

        ProductParameter productParameter = productParameterList.get(0);

        //实际材料消耗
        manufactureSummary.setConsumeMaterial1(convertDouble(manufactureList.stream().mapToDouble(manufacture -> {
            return manufacture.getDailyOutput() * techniqueInfo.getMaterial1Quota() + manufacture.getUnexpectedMaterial1();
        }).sum()));
        manufactureSummary.setConsumeMaterial2(convertDouble(manufactureList.stream().mapToDouble(manufacture -> {
            return manufacture.getDailyOutput() * techniqueInfo.getMaterial2Quota() + manufacture.getUnexpectedMaterial2();
        }).sum()));
        manufactureSummary.setConsumeMaterial3(convertDouble(manufactureList.stream().mapToDouble(manufacture -> {
            return manufacture.getDailyOutput() * techniqueInfo.getMaterial3Quota() + manufacture.getUnexpectedMaterial3();
        }).sum()));
        manufactureSummary.setConsumeMaterial4(convertDouble(manufactureList.stream().mapToDouble(manufacture -> {
            return manufacture.getDailyOutput() * techniqueInfo.getMaterial4Quota() + manufacture.getUnexpectedMaterial4();
        }).sum()));

        //实际工时
        manufactureSummary.setActualHours(convertDouble(manufactureList.stream().mapToDouble(manufacture -> {
            return manufacture.getWorkerQuantity() * manufacture.getWorkingHours();
        }).sum()));
        //理论工时
        manufactureSummary.setTheoryHours(convertDouble(manufactureList.stream().mapToDouble(manufacture -> {
            return techniqueInfo.getHourNorm() * (manufacture.getDailyOutput() - manufacture.getRejectsQuantity());
        }).sum()));

        //实际工时定额达成率
        manufactureSummary.setActualHourQuota(convertDouble(manufactureList.stream().mapToDouble(manufacture -> {
            return manufactureSummary.getActualHours() == 0 ? 0.0 : manufactureSummary.getTheoryHours() / manufactureSummary.getActualHours();
        }).sum()));

        //材料定额达成率
        Double a = manufactureSummary.getConsumeMaterial1();
        Double b = manufactureList.stream().mapToDouble(manufacture -> {
            return manufacture.getDailyOutput() * techniqueInfo.getMaterial1Quota();
        }).sum();
        manufactureSummary.setActualMaterial1Quota(convertDouble(b == 0 ? 0 : a / b));

        a = manufactureSummary.getConsumeMaterial2();
        b = manufactureList.stream().mapToDouble(manufacture -> {
            return manufacture.getDailyOutput() * techniqueInfo.getMaterial2Quota();
        }).sum();
        manufactureSummary.setActualMaterial2Quota(convertDouble(b == 0 ? 0 : a / b));

        a = manufactureSummary.getConsumeMaterial3();
        b = manufactureList.stream().mapToDouble(manufacture -> {
            return manufacture.getDailyOutput() * techniqueInfo.getMaterial3Quota();
        }).sum();
        manufactureSummary.setActualMaterial3Quota(convertDouble(b == 0 ? 0 : a / b));

        a = manufactureSummary.getConsumeMaterial4();
        b = manufactureList.stream().mapToDouble(manufacture -> {
            return manufacture.getDailyOutput() * techniqueInfo.getMaterial4Quota();
        }).sum();
        manufactureSummary.setActualMaterial4Quota(convertDouble(b == 0 ? 0 : a / b));


        //日计划完成率
        a = manufactureList.stream().mapToDouble(manufacture -> {
            return manufacture.getDailyOutput() - manufacture.getRejectsQuantity();
        }).sum();
        b = dailyPlan.getDailyPlanQuantity() * 1.0;
        manufactureSummary.setDailyCompletionRate(convertDouble(b == 0 ? 0 : a / b));

        //日不良率
        a = manufactureList.stream().mapToDouble(manufacture -> {
            return manufacture.getRejectsQuantity();
        }).sum();
        b = manufactureList.stream().mapToDouble(manufacture -> {
            return manufacture.getDailyOutput();
        }).sum();
        manufactureSummary.setDailyRejectRate(convertDouble(b == 0 ? 0 : a / b));


        manufactureSummary.setHour8Capacity(convertDouble(techniqueInfo.getCurrentMaxCapacity() * manufactureSummary.getActualHourQuota() * 7.5));
        manufactureSummary.setHour10Capacity(convertDouble(techniqueInfo.getCurrentMaxCapacity() * manufactureSummary.getActualHourQuota() * 9.5));
        manufactureSummary.setWeeklyCapacity(manufactureSummary.getHour10Capacity() * 6);
        manufactureSummary.setMonthlyCapacity(manufactureSummary.getHour10Capacity() * 26);
        manufactureSummary.setAnnualCapacity(manufactureSummary.getHour10Capacity() * 300);
        manufactureSummary.setMaxAnnualCapacity(convertDouble(techniqueInfo.getCurrentMaxCapacity() * manufactureSummary.getActualHourQuota() * 24 * 300));

        if (techniqueInfo.getCategory().getPrimaryType().equals("电堆") || techniqueInfo.getCategory().getPrimaryType().equals("系统")) {
            manufactureSummary.setStack260Quantity((int) Math.floor(manufactureSummary.getAnnualCapacity()));
            manufactureSummary.setStack340Quantity((int) Math.floor(manufactureSummary.getAnnualCapacity()));
            manufactureSummary.setStack450Quantity((int) Math.floor(manufactureSummary.getAnnualCapacity()));
        } else if (dailyPlan.getManufactureName().contains("密封-假电池") || dailyPlan.getManufactureName().contains("密封-单电池")) {
            manufactureSummary.setStack260Quantity((int) Math.floor(manufactureSummary.getAnnualCapacity() / 2));
            manufactureSummary.setStack340Quantity((int) Math.floor(manufactureSummary.getAnnualCapacity() / 2));
            manufactureSummary.setStack450Quantity((int) Math.floor(manufactureSummary.getAnnualCapacity() / 2));
        } else {
            manufactureSummary.setStack260Quantity((int) Math.floor(manufactureSummary.getAnnualCapacity() / 260));
            manufactureSummary.setStack340Quantity((int) Math.floor(manufactureSummary.getAnnualCapacity() / 340));
            manufactureSummary.setStack450Quantity((int) Math.floor(manufactureSummary.getAnnualCapacity() / 450));
        }


        manufactureSummary.setCapacityUtilization(convertDouble(techniqueInfo.getEquipmentMaxCapacity() == 0 ? 0.0 : techniqueInfo.getCurrentMaxCapacity() / techniqueInfo.getEquipmentMaxCapacity()));
//        manufactureSummary.setBatchPlanQuantity(batchPlan.getBatchPlanQuantity());

        //获得日计划对应批计划下的该工序所有日计划和总报工
        DailyPlanQueryCriteria dailyPlanQueryCriteria1 = new DailyPlanQueryCriteria();
        dailyPlanQueryCriteria1.setBatchPlanId(batchPlan.getId());
        dailyPlanQueryCriteria1.setManufactureName(dailyPlan.getManufactureName());
        List<DailyPlan> allDailyPlan = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, dailyPlanQueryCriteria1, criteriaBuilder));
        List<Manufacture> allManufacture = new ArrayList<>();
        allDailyPlan.forEach(d -> {
            ManufactureQueryCriteria manufactureQueryCriteria2 = new ManufactureQueryCriteria();
            manufactureQueryCriteria2.setPlanNumber(d.getPlanNumber());
            List<Manufacture> manufactures = manufactureRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, manufactureQueryCriteria2, criteriaBuilder));
            if (manufactures != null && manufactures.size() > 0) {
                allManufacture.addAll(manufactures);
            }
        });
        a = allManufacture.stream().mapToDouble(manufacture -> {
            return manufacture.getRejectsQuantity();
        }).sum();
        b = productParameter.getUnitsQuantity() == 0 ? 0.0 : batchPlan.getBatchPlanQuantity() * productParameter.getUnitsQuantity();
        //批次不良数需重新计算
        manufactureSummary.setRejectsTotal(a);
        //总计划制造量
        manufactureSummary.setTotalPlanQuantity(b.intValue()); //allDailyPlan.stream().mapToInt(d ->{return d.getDailyPlanQuantity();}).sum()
        manufactureSummary.setBatchRejectRate(convertDouble(b == 0 ? 0 : a / b));

        //批次计划累计完成百分比
        a = allManufacture.stream().mapToDouble(manufacture -> {
            return manufacture.getDailyOutput() - manufacture.getRejectsQuantity();
        }).sum();
        b = productParameter.getUnitsQuantity() == 0 ? 0.0 : batchPlan.getBatchPlanQuantity() * productParameter.getUnitsQuantity();
        manufactureSummary.setBatchPlanQuantity(b.intValue());
        //批次累计完成数量（合格）
        manufactureSummary.setBatchCompletedQuantity(a.intValue());
        manufactureSummary.setAccumulativeTotalPercentage(convertDouble(b == 0 ? 0 : a / b));

        //日计划产量
        manufactureSummary.setDailyPlanQuantity(dailyPlan.getDailyPlanQuantity());


        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        String endDate = dateFormat.format(c.getTime());
        Timestamp end = null;
        try {
            end = new Timestamp(dateFormat.parse(endDate).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        c.add(Calendar.YEAR, -1);
        String startDate = dateFormat.format(c.getTime());
        Timestamp start = null;
        try {
            start = new Timestamp(dateFormat.parse(startDate).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Timestamp> timestampList = new ArrayList<>();
        timestampList.add(start);
        timestampList.add(end);
        ManufactureQueryCriteria manufactureQueryCriteria1 = new ManufactureQueryCriteria();
        manufactureQueryCriteria1.setManufactureName(dailyPlan.getManufactureName());
        manufactureQueryCriteria1.setFillDate(timestampList);

        //获取该工序一年的报工
        List<Manufacture> manufactureList1 = manufactureRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, manufactureQueryCriteria1, criteriaBuilder));
        if (manufactureList == null || manufactureList.size() <= 0) { //如果没有报工，则不产生汇总数据
            return;
        }

        //年度不良品总数
        a = manufactureList1.stream().mapToDouble(manufacture -> {
            return manufacture.getRejectsQuantity();
        }).sum();
        manufactureSummary.setAnnualRejectTotal(a.intValue());

        //年度生产累计总量(合格)
        b = manufactureList1.stream().mapToDouble(manufacture -> {
            return manufacture.getDailyOutput() - manufacture.getRejectsQuantity();
        }).sum();
        manufactureSummary.setAnnualOutputTotal(b.intValue());

        //年度生产累计总量
        b = manufactureList1.stream().mapToDouble(manufacture -> {
            return manufacture.getDailyOutput();
        }).sum();

        //年平均不良率
        manufactureSummary.setAnnualRejectRate(convertDouble(b == 0 ? 0 : a / b));

        manufactureSummaryRepository.save(manufactureSummary);

//        a = manufactureList.stream().mapToDouble(manufacture -> {
//            return manufacture.getDailyOutput() - manufacture.getRejectsQuantity();
//        }).sum();
//
//        dailyPlan.setCompletedQuantity(a.intValue());
//        dailyPlanRepository.save(dailyPlan);
    }

    private void updateDailyCompletedQuantity(String planNumber) {
        ManufactureQueryCriteria manufactureQueryCriteria = new ManufactureQueryCriteria();
        manufactureQueryCriteria.setPlanNumber(planNumber);

        //获取该日计划下的所有报工
        List<Manufacture> manufactureList = manufactureRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, manufactureQueryCriteria, criteriaBuilder));
        if (manufactureList != null && manufactureList.size() > 0) { //如果没有报工，则不产生汇总数据

            Double a = manufactureList.stream().mapToDouble(manufacture -> {
                return manufacture.getDailyOutput() - manufacture.getRejectsQuantity();
            }).sum();

            DailyPlanQueryCriteria dailyPlanQueryCriteria = new DailyPlanQueryCriteria();
            dailyPlanQueryCriteria.setPlanNumber(planNumber);
            List<DailyPlan> dailyPlanList = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, dailyPlanQueryCriteria, criteriaBuilder));
            if (dailyPlanList != null && dailyPlanList.size() > 0) { //如果没有日计划，则不产生汇总数据
                DailyPlan dailyPlan = dailyPlanList.get(0);
                dailyPlan.setCompletedQuantity(a.intValue());
                dailyPlanRepository.save(dailyPlan);
            }
        }

    }

    private Double convertDouble(Double source) {
        DecimalFormat decimalFormat = new DecimalFormat("#########.####");
        return Double.valueOf(decimalFormat.format(source));
    }

    @Override
    public void queryManufacture(HttpServletResponse response, ManufactureQueryCriteria criteria) {
        List<Manufacture> manufactures = manufactureRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));

        List<Map<String, Object>> list = new ArrayList<>();
        for (Manufacture manufacture : manufactures) {
            Map<String, Object> map = new LinkedHashMap<>();


            map.put("生产计划编号", manufacture.getPlanNumber());
            map.put("生产基地", manufacture.getManufactureAddress());
            map.put("报工名称", manufacture.getManufactureName());
            map.put("工序结存数", manufacture.getInventoryBalance());

            map.put("材料1意外消耗", "");
            map.put("材料2意外消耗", "");
            map.put("材料3意外消耗", "");
            DailyPlanQueryCriteria dailyPlanQueryCriteria = new DailyPlanQueryCriteria();
            dailyPlanQueryCriteria.setPlanNumber(manufacture.getPlanNumber());
            dailyPlanQueryCriteria.setManufactureName(manufacture.getManufactureName());
            List<DailyPlan> dailyPlanList = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, dailyPlanQueryCriteria, criteriaBuilder));
            if (dailyPlanList != null && dailyPlanList.size() > 0) {
                DailyPlan dailyPlan = dailyPlanList.get(0);
                ProductParameterQueryCriteria productParameterQueryCriteria = new ProductParameterQueryCriteria();
                productParameterQueryCriteria.setProductName(dailyPlan.getBatchPlan().getProductName());
                productParameterQueryCriteria.setManufactureName(manufacture.getManufactureName());
                List<ProductParameter> productParameterList = productParameterRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, productParameterQueryCriteria, criteriaBuilder));
                if (productParameterList != null && productParameterList.size() > 0) {
                    ProductParameter productParameter = productParameterList.get(0);
                    map.put("材料1意外消耗", productParameter.getTechniqueInfo().getMaterial1Name() + manufacture.getUnexpectedMaterial1() + productParameter.getTechniqueInfo().getMaterial1Unit());
                    map.put("材料2意外消耗", productParameter.getTechniqueInfo().getMaterial2Name() + manufacture.getUnexpectedMaterial2() + productParameter.getTechniqueInfo().getMaterial2Unit());
                    map.put("材料3意外消耗", productParameter.getTechniqueInfo().getMaterial3Name() + manufacture.getUnexpectedMaterial3() + productParameter.getTechniqueInfo().getMaterial3Unit());
                }
            }

            map.put("班组人员数", manufacture.getWorkerQuantity());
            map.put("工时（含加班）", manufacture.getWorkingHours());
            map.put("日实际产量(含不良）", manufacture.getDailyOutput());
            map.put("不良品数量", manufacture.getRejectsQuantity());
            map.put("废品率异常原因说明", manufacture.getRejectReasons());
            map.put("日产能不达标原因", manufacture.getIncompleteReasons());
            map.put("填报日期", manufacture.getFillDate());
            list.add(map);
        }
        try {
            FileUtil.downloadExcel(list, response);
        } catch (Exception e) {
        }
    }

    @Override
    public void queryManufactureSummary(HttpServletResponse response, ManufactureQueryCriteria criteria) {
        List<ManufactureSummary> manufactureSummaryList = manufactureSummaryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));

        List<Map<String, Object>> list = new ArrayList<>();
        for (ManufactureSummary manufactureSummary : manufactureSummaryList) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("报工名称", manufactureSummary.getManufactureName());
            map.put("生产批次号", manufactureSummary.getPlanNumber());
            map.put("材料1消耗", manufactureSummary.getMaterial1Name()+manufactureSummary.getConsumeMaterial1()+manufactureSummary.getMaterial1Unit());
            map.put("材料2消耗", manufactureSummary.getMaterial2Name()+manufactureSummary.getConsumeMaterial2()+manufactureSummary.getMaterial2Unit());
            map.put("材料3消耗", manufactureSummary.getMaterial3Name()+manufactureSummary.getConsumeMaterial3()+manufactureSummary.getMaterial3Unit());
            map.put("材料4消耗", manufactureSummary.getMaterial4Name()+manufactureSummary.getConsumeMaterial4()+manufactureSummary.getMaterial4Unit());

            map.put("材料1定额达成率", manufactureSummary.getMaterial1Name() + manufactureSummary.getActualMaterial1Quota() * 100 + "%");
            map.put("材料2定额达成率", manufactureSummary.getMaterial2Name() + manufactureSummary.getActualMaterial2Quota() * 100 + "%");
            map.put("材料3定额达成率", manufactureSummary.getMaterial3Name() + manufactureSummary.getActualMaterial3Quota() * 100 + "%");
            map.put("材料4定额达成率", manufactureSummary.getMaterial4Name() + manufactureSummary.getActualMaterial4Quota() * 100 + "%");
            map.put("实际人工时", manufactureSummary.getActualHours());
            map.put("理论工时", manufactureSummary.getTheoryHours());
            map.put("日计划完成率", manufactureSummary.getDailyCompletionRate());
            map.put("批次不良总数", manufactureSummary.getRejectsTotal());
            map.put("日不良率%", manufactureSummary.getDailyCompletionRate());
            map.put("批次平均不良率%", manufactureSummary.getBatchRejectRate());
            map.put("年度平均不良率%", manufactureSummary.getAnnualRejectRate());
            map.put("年度不良品总数", manufactureSummary.getAnnualRejectTotal());
            map.put("年度生产累计总量", manufactureSummary.getAnnualOutputTotal());
            map.put("批次计划累计完成百分比", manufactureSummary.getAccumulativeTotalPercentage());
            map.put("日计划产量", manufactureSummary.getDailyPlanQuantity());
            map.put("总计划制造量", manufactureSummary.getTotalPlanQuantity());
            map.put("批次累计完成数量(合格品）", manufactureSummary.getBatchCompletedQuantity());
            map.put("产能利用率%", manufactureSummary.getCapacityUtilization());
            map.put("8小时工作产能（7.5小时）", manufactureSummary.getHour8Capacity());
            map.put("10小时工作产能（9.）", manufactureSummary.getHour10Capacity());
            map.put("当前状态最大周产量预测", manufactureSummary.getWeeklyCapacity());
            map.put("当前状态最大月产量预测", manufactureSummary.getMonthlyCapacity());
            map.put("当前状态年产能预测", manufactureSummary.getAnnualCapacity());
            map.put("设备最大年产能", manufactureSummary.getMaxAnnualCapacity());
            map.put("260节电堆可制造数量 /年", manufactureSummary.getStack260Quantity());
            map.put("340节电堆可制造数量 /年", manufactureSummary.getStack340Quantity());
            map.put("450节电堆可制造数量 /年", manufactureSummary.getStack450Quantity());
            list.add(map);
        }
        try {
            FileUtil.downloadExcel(list, response);
        } catch (Exception e) {
        }
    }

    //制造信息修改
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Manufacture resources) {
        Manufacture manufacture = manufactureRepository.findById(resources.getId()).orElseGet(Manufacture::new);
        ValidationUtil.isNull(manufacture.getId(), "manufacture", "id", resources.getId());

        manufacture.copy(resources);
        manufactureRepository.save(manufacture);
        updateDailyCompletedQuantity(manufacture.getPlanNumber());
        summary(manufacture.getPlanNumber());
    }

    @Override
    public Map<String, Object> queryManufacture(ManufactureQueryCriteria criteria, Pageable pageable) {
        Page<Manufacture> page = manufactureRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);

        Page<ManufactureDto> rst = page.map(manufactureMapper::toDto);

        rst.forEach(manufactureDto -> {
            DailyPlanQueryCriteria dailyPlanQueryCriteria = new DailyPlanQueryCriteria();
            dailyPlanQueryCriteria.setPlanNumber(manufactureDto.getPlanNumber());
            dailyPlanQueryCriteria.setManufactureName(manufactureDto.getManufactureName());
            List<DailyPlan> dailyPlanList = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, dailyPlanQueryCriteria, criteriaBuilder));
            if (dailyPlanList != null && dailyPlanList.size() > 0) {
                DailyPlan dailyPlan = dailyPlanList.get(0);
                ProductParameterQueryCriteria productParameterQueryCriteria = new ProductParameterQueryCriteria();
                productParameterQueryCriteria.setProductName(dailyPlan.getBatchPlan().getProductName());
                productParameterQueryCriteria.setManufactureName(manufactureDto.getManufactureName());
                List<ProductParameter> productParameterList = productParameterRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, productParameterQueryCriteria, criteriaBuilder));
                if (productParameterList != null && productParameterList.size() > 0) {
                    ProductParameter productParameter = productParameterList.get(0);
                    manufactureDto.setMaterial1Name(productParameter.getTechniqueInfo().getMaterial1Name());
                    manufactureDto.setMaterial2Name(productParameter.getTechniqueInfo().getMaterial2Name());
                    manufactureDto.setMaterial3Name(productParameter.getTechniqueInfo().getMaterial3Name());
                    manufactureDto.setMaterial4Name(productParameter.getTechniqueInfo().getMaterial4Name());
                    manufactureDto.setMaterial1Unit(productParameter.getTechniqueInfo().getMaterial1Unit());
                    manufactureDto.setMaterial2Unit(productParameter.getTechniqueInfo().getMaterial2Unit());
                    manufactureDto.setMaterial3Unit(productParameter.getTechniqueInfo().getMaterial3Unit());
                    manufactureDto.setMaterial4Unit(productParameter.getTechniqueInfo().getMaterial4Unit());
                }
            }
        });

        return PageUtil.toPage(rst);
    }

    @Override
    public Map<String, Object> queryManufactureSummary(ManufactureSummaryQueryCriteria criteria, Pageable pageable) {
        Page<ManufactureSummary> page = manufactureSummaryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(manufactureSummaryMapper::toDto));
    }
}
