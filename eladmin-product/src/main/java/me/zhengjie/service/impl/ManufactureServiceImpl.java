package me.zhengjie.service.impl;

import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.*;
import me.zhengjie.mapper.ManufactureMapper;
import me.zhengjie.mapper.ManufactureSummaryMapper;
import me.zhengjie.repository.DailyPlanRepository;
import me.zhengjie.repository.ManufactureRepository;
import me.zhengjie.repository.ManufactureSummaryRepository;
import me.zhengjie.service.ManufactureService;
import me.zhengjie.service.dto.DailyPlanQueryCriteria;
import me.zhengjie.service.dto.ManufactureDto;
import me.zhengjie.service.dto.ManufactureQueryCriteria;
import me.zhengjie.service.dto.ManufactureSummaryQueryCriteria;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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


    @Resource(name = "manufactureSummaryMapperImpl")
    private ManufactureSummaryMapper manufactureSummaryMapper;

    private final ManufactureSummaryRepository manufactureSummaryRepository;

    //制造信息报工
    @Override
    public ManufactureDto create(Manufacture resources) {
        Manufacture manufacture = manufactureRepository.save(resources);
        summary(resources.getPlanNumber());

        //新增后将对应的日计划effect改成true
        DailyPlanQueryCriteria dailyPlanQueryCriteria = new DailyPlanQueryCriteria();
        dailyPlanQueryCriteria.setPlanNumber(resources.getPlanNumber());
        List<DailyPlan> dailyPlanList = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, dailyPlanQueryCriteria, criteriaBuilder));

        if (dailyPlanList != null && dailyPlanList.size() > 0) { //如果没有日计划，则不产生汇总数据;
            dailyPlanList.forEach(dailyPlan -> {
                dailyPlan.setEffect(true);
                dailyPlanRepository.save(dailyPlan);
            });
        }
        return manufactureMapper.toDto(manufacture);
    }

    public void summary(String planNumber) {
        ManufactureQueryCriteria manufactureQueryCriteria = new ManufactureQueryCriteria();
        manufactureQueryCriteria.setPlanNumber(planNumber);
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
        TechniqueInfo techniqueInfo = dailyPlan.getBatchPlan().getTechniqueInfo();
        BatchPlan batchPlan = dailyPlan.getBatchPlan();

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
        manufactureSummary.setConsumeMaterial1(manufactureList.stream().mapToDouble(manufacture -> {
            return manufacture.getDailyOutput() * techniqueInfo.getMaterial1Quota() + manufacture.getUnexpectedMaterial1();
        }).sum());
        manufactureSummary.setConsumeMaterial2(manufactureList.stream().mapToDouble(manufacture -> {
            return manufacture.getDailyOutput() * techniqueInfo.getMaterial2Quota() + manufacture.getUnexpectedMaterial2();
        }).sum());
        manufactureSummary.setConsumeMaterial3(manufactureList.stream().mapToDouble(manufacture -> {
            return manufacture.getDailyOutput() * techniqueInfo.getMaterial3Quota() + manufacture.getUnexpectedMaterial3();
        }).sum());

        manufactureSummary.setActualHours(manufactureList.stream().mapToDouble(manufacture -> {
            return manufacture.getWorkerQuantity() * manufacture.getWorkingHours();
        }).sum());
        manufactureSummary.setTheoryHours(manufactureList.stream().mapToDouble(manufacture -> {
            return techniqueInfo.getHourNorm() * (manufacture.getDailyOutput() - manufacture.getRejectsQuantity());
        }).sum());
        manufactureSummary.setActualHourQuota(convertDouble(manufactureList.stream().mapToDouble(manufacture -> {
            return manufactureSummary.getTheoryHours() == 0 ? 0.0 : manufactureSummary.getActualHours() / manufactureSummary.getTheoryHours();
        }).sum()));

        Double a = techniqueInfo.getMaterial1Quota() > 0 ? manufactureList.stream().mapToDouble(manufacture -> {
            return manufactureSummary.getConsumeMaterial1();
        }).sum() : 0;
        Double b = manufactureList.stream().mapToDouble(manufacture -> {
            return manufacture.getDailyOutput() * techniqueInfo.getMaterial1Quota();
        }).sum();
        manufactureSummary.setActualMaterial1Quota(convertDouble(b == 0 ? 0 : a / b));

        a = techniqueInfo.getMaterial1Quota() > 0 ? manufactureList.stream().mapToDouble(manufacture -> {
            return manufactureSummary.getConsumeMaterial2();
        }).sum() : 0;
        b = manufactureList.stream().mapToDouble(manufacture -> {
            return manufacture.getDailyOutput() * techniqueInfo.getMaterial2Quota();
        }).sum();
        manufactureSummary.setActualMaterial2Quota(convertDouble(b == 0 ? 0 : a / b));

        a = techniqueInfo.getMaterial1Quota() > 0 ? manufactureList.stream().mapToDouble(manufacture -> {
            return manufactureSummary.getConsumeMaterial3();
        }).sum() : 0;
        b = manufactureList.stream().mapToDouble(manufacture -> {
            return manufacture.getDailyOutput() * techniqueInfo.getMaterial3Quota();
        }).sum();
        manufactureSummary.setActualMaterial3Quota(convertDouble(b == 0 ? 0 : a / b));

        a = manufactureList.stream().mapToDouble(manufacture -> {
            return manufacture.getDailyOutput() - manufacture.getRejectsQuantity();
        }).sum();
        b = dailyPlan.getDailyPlanQuantity() * 1.0;
        manufactureSummary.setDailyCompletionRate(convertDouble(b == 0 ? 0 : a / b));

        manufactureSummary.setRejectsTotal(manufactureList.stream().mapToDouble(manufacture -> {
            return manufacture.getRejectsQuantity();
        }).sum());

        a = manufactureList.stream().mapToDouble(manufacture -> {
            return manufacture.getRejectsQuantity();
        }).sum();
        b = manufactureList.stream().mapToDouble(manufacture -> {
            return manufacture.getDailyOutput();
        }).sum();
        manufactureSummary.setDailyRejectRate(convertDouble(b == 0 ? 0 : a / b));


        manufactureSummary.setHour8Capacity(techniqueInfo.getEquipmentMaxCapacity() * 7.5);
        manufactureSummary.setHour10Capacity(techniqueInfo.getEquipmentMaxCapacity() * 9.5);

        manufactureSummary.setWeeklyCapacity(techniqueInfo.getEquipmentMaxCapacity() * 9.5 * 6);
        manufactureSummary.setMonthlyCapacity(techniqueInfo.getEquipmentMaxCapacity() * 9.5 * 26);
        manufactureSummary.setAnnualCapacity(techniqueInfo.getEquipmentMaxCapacity() * 9.5 * 300);
        manufactureSummary.setMaxAnnualCapacity(techniqueInfo.getCurrentMaxCapacity() * 9.5 * 300);

        manufactureSummary.setStack260Quantity((int) Math.floor(manufactureSummary.getAnnualCapacity() / 260));
        manufactureSummary.setStack340Quantity((int) Math.floor(manufactureSummary.getAnnualCapacity() / 340));
        manufactureSummary.setStack450Quantity((int) Math.floor(manufactureSummary.getAnnualCapacity() / 450));

        manufactureSummary.setCapacityUtilization(convertDouble(techniqueInfo.getEquipmentMaxCapacity() == 0 ? 0.0 : techniqueInfo.getCurrentMaxCapacity() / techniqueInfo.getEquipmentMaxCapacity()));
//        manufactureSummary.setBatchPlanQuantity(batchPlan.getBatchPlanQuantity());

        DailyPlanQueryCriteria dailyPlanQueryCriteria1 = new DailyPlanQueryCriteria();
        dailyPlanQueryCriteria1.setBatchPlanId(batchPlan.getId());
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
        b = batchPlan.getBatchPlanQuantity() * 1.0;
        manufactureSummary.setBatchRejectRate(convertDouble(b == 0 ? 0 : a / b));

        //TODO
        //年度平均不良率
        manufactureSummary.setAnnualRejectRate(0.0);

        //批次计划累计完成百分比
        a = allManufacture.stream().mapToDouble(manufacture -> {
            return manufacture.getDailyOutput() - manufacture.getRejectsQuantity();
        }).sum();
        b = batchPlan.getBatchPlanQuantity() * 1.0;
        manufactureSummary.setBatchPlanQuantity(a.intValue());
        //批次累计完成数量（合格）
        manufactureSummary.setBatchCompletedQuantity(a.intValue());
        manufactureSummary.setAccumulativeTotalPercentage(convertDouble(b == 0 ? 0 : a / b));

        //日计划产量
        manufactureSummary.setDailyPlanQuantity(dailyPlan.getDailyPlanQuantity());
        //总计划制造量
        manufactureSummary.setTotalPlanQuantity(batchPlan.getBatchPlanQuantity()); //allDailyPlan.stream().mapToInt(d ->{return d.getDailyPlanQuantity();}).sum()

//        年度不良品总数
        manufactureSummary.setAnnualRejectTotal(0);

//        年度生产累计总量
        manufactureSummary.setAnnualOutputTotal(0);

        manufactureSummaryRepository.save(manufactureSummary);
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

            DailyPlanQueryCriteria dailyPlanQueryCriteria = new DailyPlanQueryCriteria();
            dailyPlanQueryCriteria.setPlanNumber(manufacture.getPlanNumber());
            List<DailyPlan> dailyPlanList = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, dailyPlanQueryCriteria, criteriaBuilder));
            DailyPlan dailyPlan = new DailyPlan();
            if (dailyPlanList != null && dailyPlanList.size() > 0) {
                dailyPlan = dailyPlanList.get(0);
            }
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("生产计划编号", manufacture.getPlanNumber());
            map.put("产品名称", dailyPlan.getProductName());
            map.put("工序结存数", manufacture.getInventoryBalance());
            map.put("材料1意外消耗", manufacture.getUnexpectedMaterial1());
            map.put("材料2意外消耗", manufacture.getUnexpectedMaterial2());
            map.put("材料3意外消耗", manufacture.getUnexpectedMaterial3());
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
        for (ManufactureSummary manufacture : manufactureSummaryList) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("材料 1定额达成率", manufacture.getActualMaterial1Quota());
            map.put("材料 2定额达成率", manufacture.getActualMaterial2Quota());
            map.put("材料 3定额达成率", manufacture.getActualMaterial3Quota());
            map.put("实际人工时", manufacture.getActualHours());
            map.put("理论工时", manufacture.getTheoryHours());
            map.put("日计划完成率", manufacture.getDailyCompletionRate());
            map.put("批次不良总数", manufacture.getRejectsTotal());
            map.put("日不良率%", manufacture.getDailyCompletionRate());
            map.put("批次平均不良率%", manufacture.getBatchRejectRate());
//            map.put("年度平均不良率%", manufacture.getRejectsQuantity());
            map.put("批次计划累计完成百分比", manufacture.getAccumulativeTotalPercentage());
            map.put("日计划产量", manufacture.getDailyPlanQuantity());
            map.put("总计划制造量", manufacture.getTotalPlanQuantity());
            map.put("批次累计完成数量(合格品）", manufacture.getBatchCompletedQuantity());
            map.put("产能利用率%", manufacture.getCapacityUtilization());
            map.put("8小时工作产能（7.5小时）", manufacture.getHour8Capacity());
            map.put("10小时工作产能（9.）", manufacture.getHour10Capacity());
            map.put("当前状态最大周产量预测", manufacture.getWeeklyCapacity());
            map.put("当前状态最大月产量预测", manufacture.getMonthlyCapacity());
            map.put("当前状态年产能预测", manufacture.getAnnualCapacity());
            map.put("设备最大年产能", manufacture.getMaxAnnualCapacity());
            map.put("260节电堆可制造数量 /年", manufacture.getStack260Quantity());
            map.put("340节电堆可制造数量 /年", manufacture.getStack340Quantity());
            map.put("450节电堆可制造数量 /年", manufacture.getStack450Quantity());
            list.add(map);
        }
        try {
            FileUtil.downloadExcel(list, response);
        } catch (Exception e) {
        }

    }


    private void summary(Manufacture manufacture) {
        DailyPlanQueryCriteria dailyPlanQueryCriteria = new DailyPlanQueryCriteria();
        dailyPlanQueryCriteria.setPlanNumber(manufacture.getPlanNumber());
        DailyPlan dailyPlan = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, dailyPlanQueryCriteria, criteriaBuilder)).get(0);
        TechniqueInfo techniqueInfo = dailyPlan.getBatchPlan().getTechniqueInfo();

        ManufactureSummaryQueryCriteria manufactureSummaryQueryCriteria = new ManufactureSummaryQueryCriteria();
        manufactureSummaryQueryCriteria.setPlanNumber(manufacture.getPlanNumber());
        List<ManufactureSummary> summaryList = manufactureSummaryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, manufactureSummaryQueryCriteria, criteriaBuilder));
        ManufactureSummary lastSummary = new ManufactureSummary();
        if (summaryList != null)
            lastSummary = summaryList.get(0);

        ManufactureSummary manufactureSummary = new ManufactureSummary();
        manufactureSummary.setPlanNumber(manufacture.getPlanNumber());

        manufactureSummary.setConsumeMaterial1(manufacture.getDailyOutput() * techniqueInfo.getMaterial1Quota() + manufacture.getUnexpectedMaterial1());
        manufactureSummary.setConsumeMaterial2(manufacture.getDailyOutput() * techniqueInfo.getMaterial1Quota() + manufacture.getUnexpectedMaterial2());
        manufactureSummary.setConsumeMaterial3(manufacture.getDailyOutput() * techniqueInfo.getMaterial1Quota() + manufacture.getUnexpectedMaterial3());

        manufactureSummary.setActualHours(manufacture.getWorkingHours() * manufacture.getWorkingHours());
        manufactureSummary.setTheoryHours(techniqueInfo.getHourNorm() * (manufacture.getDailyOutput() - manufacture.getRejectsQuantity()));
        manufactureSummary.setActualHourQuota(manufactureSummary.getActualHours() / manufactureSummary.getTheoryHours());

        manufactureSummary.setActualMaterial1Quota(techniqueInfo.getMaterial2Quota() > 0 ? manufactureSummary.getConsumeMaterial1() / (manufacture.getDailyOutput() * techniqueInfo.getMaterial1Quota()) : 0);
        manufactureSummary.setActualMaterial2Quota(techniqueInfo.getMaterial2Quota() > 0 ? manufactureSummary.getConsumeMaterial2() / (manufacture.getDailyOutput() * techniqueInfo.getMaterial2Quota()) : 0);
        manufactureSummary.setActualMaterial3Quota(techniqueInfo.getMaterial3Quota() > 0 ? manufactureSummary.getConsumeMaterial3() / (manufacture.getDailyOutput() * techniqueInfo.getMaterial3Quota()) : 0);

        manufactureSummary.setDailyCompletionRate((double) (manufacture.getDailyOutput() - manufacture.getRejectsQuantity()) / dailyPlan.getDailyPlanQuantity());

        //存在问题
        manufactureSummary.setRejectsTotal(lastSummary.getRejectsTotal() + manufacture.getRejectsQuantity());

        manufactureSummary.setDailyRejectRate((double) manufacture.getRejectsQuantity() / manufacture.getDailyOutput());
        manufactureSummary.setHour8Capacity(techniqueInfo.getEquipmentMaxCapacity() * (float) 7.5);
        manufactureSummary.setHour10Capacity(techniqueInfo.getEquipmentMaxCapacity() * (float) 9.5);
        manufactureSummary.setWeeklyCapacity(manufactureSummary.getHour10Capacity() * 6);
        manufactureSummary.setMonthlyCapacity(manufactureSummary.getHour10Capacity() * 26);
        manufactureSummary.setAnnualCapacity(manufactureSummary.getHour10Capacity() * 300);

        manufactureSummary.setStack260Quantity((int) Math.floor(manufactureSummary.getAnnualCapacity() / (float) 260));
        manufactureSummary.setStack340Quantity((int) Math.floor(manufactureSummary.getAnnualCapacity() / (float) 340));
        manufactureSummary.setStack450Quantity((int) Math.floor(manufactureSummary.getAnnualCapacity() / (float) 450));

        manufactureSummary.setCapacityUtilization(techniqueInfo.getCurrentMaxCapacity() / techniqueInfo.getEquipmentMaxCapacity());
        manufactureSummary.setBatchPlanQuantity(manufactureSummary.getBatchPlanQuantity() + manufacture.getDailyOutput() - manufacture.getRejectsQuantity());
        manufactureSummary.setBatchRejectRate(manufactureSummary.getRejectsTotal() / (manufactureSummary.getRejectsTotal() + manufactureSummary.getBatchPlanQuantity()));
    }


    //制造信息修改
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Manufacture resources) {
        Manufacture manufacture = manufactureRepository.findById(resources.getId()).orElseGet(Manufacture::new);
        ValidationUtil.isNull(manufacture.getId(), "manufacture", "id", resources.getId());

        manufacture.copy(resources);
        manufactureRepository.save(manufacture);
        summary(resources.getPlanNumber());
    }

    @Override
    public Map<String, Object> queryManufacture(ManufactureQueryCriteria criteria, Pageable pageable) {


        Page<Manufacture> page = manufactureRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(manufactureMapper::toDto));

    }

    @Override
    public Map<String, Object> queryManufactureSummary(ManufactureSummaryQueryCriteria criteria, Pageable pageable) {

        Page<ManufactureSummary> page = manufactureSummaryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(manufactureSummaryMapper::toDto));

    }


}
