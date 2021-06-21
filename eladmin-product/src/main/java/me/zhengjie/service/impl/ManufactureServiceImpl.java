package me.zhengjie.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.*;
import me.zhengjie.mapper.ManufactureMapper;
import me.zhengjie.mapper.ManufactureSummaryMapper;
import me.zhengjie.repository.*;
import me.zhengjie.service.ManufactureService;
import me.zhengjie.service.ProductParameterService;
import me.zhengjie.service.StockService;
import me.zhengjie.service.dto.*;
import me.zhengjie.utils.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.DecimalFormat;
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

    private final StockService stockService;


    private final SysUserRepository sysUserRepository;

    private final ReasonRepository reasonRepository;

        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    //制造信息报工
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ManufactureDto create(Manufacture resources) {
        ManufactureQueryCriteria criteria = new ManufactureQueryCriteria();
        criteria.setPlanNumber(resources.getPlanNumber());
        criteria.setManufactureName(resources.getManufactureName());

        List<Manufacture> manufactures = manufactureRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        if (manufactures != null && manufactures.size() > 0) {
            return null;
        }

        Manufacture manufacture = manufactureRepository.save(resources);
        updateDailyCompletedQuantity(resources.getPlanNumber());
        summary(resources.getPlanNumber());
        stockService.updateBalance(manufacture, "CREATE", manufacture);
        return manufactureMapper.toDto(manufacture);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Manufacture> unplannedManufacture(UnPlannedManufactureDto unPlannedManufactureDto) {
        List<Manufacture> manufactureList = unPlannedManufactureDto.getManufactureList();
        List<Manufacture> manufactureLists = new ArrayList<>();

        for (Manufacture manufacture : manufactureList) {
            manufacture.setFillDate(unPlannedManufactureDto.getFDate());
            manufacture.setManufactureAddress(unPlannedManufactureDto.getFAddress());
            manufacture.setUserId(unPlannedManufactureDto.getFUseId().longValue());


            ProductParameterQueryCriteria productParameterQueryCriteria = new ProductParameterQueryCriteria();
            productParameterQueryCriteria.setManufactureName(manufacture.getManufactureName());
            List<ProductParameter> productParameterList = productParameterRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, productParameterQueryCriteria, criteriaBuilder));
            if (productParameterList != null && productParameterList.size() > 0) {
                ProductParameter productParameter = productParameterList.get(0);
                manufacture.setSerialNumber(productParameter.getSerialNumber());

                ManufactureQueryCriteria criteria = new ManufactureQueryCriteria();
                criteria.setManufactureName(manufacture.getManufactureName());
                criteria.setManufactureAddress(manufacture.getManufactureAddress());
                criteria.setFillDate(dateFormat.format(manufacture.getFillDate()));
                List<Manufacture> manufactureList1 = manufactureRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
                if (manufactureList1 == null || manufactureList1.size() == 0) {
                    Manufacture manufacture2 = manufactureRepository.save(manufacture);
                    stockService.updateBalance(manufacture2, "CREATE", manufacture2);
                    manufactureLists.add(manufacture2);
                } else {
                    int i = 0;
                    for (Manufacture manufacture1 : manufactureList1) {
                        if (manufacture1.getPlanNumber() == null || "".equals(manufacture1.getPlanNumber())) {
                            if ((manufacture.getDailyOutput() != null && manufacture.getDailyOutput() != manufacture1.getDailyOutput()) || (manufacture.getRejectsQuantity() != null && manufacture.getRejectsQuantity() != manufacture1.getRejectsQuantity()) || (manufacture.getTransferQuantity() != null && manufacture.getTransferQuantity() != manufacture1.getTransferQuantity())) {
                                Manufacture manufacture3 = new Manufacture();
                                manufacture3.copy(manufacture1);
                                manufacture3.setDailyOutput(manufacture.getDailyOutput() - manufacture1.getDailyOutput());
                                manufacture3.setRejectsQuantity(manufacture.getRejectsQuantity() - manufacture1.getRejectsQuantity());
                                manufacture3.setTransferQuantity(manufacture.getTransferQuantity() - manufacture1.getTransferQuantity());

                                manufacture1.copy(manufacture);

                                Manufacture manufacture2 = manufactureRepository.save(manufacture1);
                                stockService.updateBalance(manufacture3, "UPDATE", manufacture1);
                                manufactureLists.add(manufacture2);
                            }
                            i++;
                            break;
                        }
                    }
                    if (i == 0) {
                        Manufacture manufacture2 = manufactureRepository.save(manufacture);
                        stockService.updateBalance(manufacture2, "CREATE", manufacture2);
                        manufactureLists.add(manufacture2);
                    }

                }
            }
        }
        return manufactureLists;
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
//        a = manufactureList.stream().mapToDouble(manufacture -> {
//            return manufacture.getDailyOutput() - manufacture.getRejectsQuantity();
//        }).sum();
        a = manufactureList.stream().mapToDouble(manufacture -> {
            return manufacture.getDailyOutput();
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
        ManufactureDtoQueryCriteria manufactureDtoQueryCriteria = new ManufactureDtoQueryCriteria();
        manufactureDtoQueryCriteria.setManufactureName(dailyPlan.getManufactureName());

        //获取该工序一年的报工
        List<Manufacture> manufactureList1 = manufactureRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, manufactureDtoQueryCriteria, criteriaBuilder));
        if (manufactureList == null || manufactureList.size() <= 0) { //如果没有报工，则不产生汇总数据
            return;
        }

        Timestamp finalStart = start;
        Timestamp finalEnd = end;
        manufactureList1 = manufactureList1.stream().filter(aa -> {
            return aa.getFillDate().getTime() > finalStart.getTime() && aa.getFillDate().getTime() < finalEnd.getTime();
        }).collect(Collectors.toList());

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
    public void queryManufacture(HttpServletResponse response, ManufactureQueryCriteria criteria, Boolean isPlan) {
        List<Manufacture> manufactures = manufactureRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        if (isPlan) {
            manufactures = manufactures.stream().filter(a -> {
                return a.getPlanNumber() != null && !"".equals(a.getPlanNumber());
            }).collect(Collectors.toList());
        } else {
            manufactures = manufactures.stream().filter(a -> {
                return a.getPlanNumber() == null || "".equals(a.getPlanNumber());
            }).collect(Collectors.toList());
        }

        manufactures = manufactures.stream().sorted(Comparator.comparing(Manufacture::getFillDate, Comparator.reverseOrder()).thenComparing(Manufacture::getManufactureAddress, Comparator.reverseOrder()).thenComparing(Manufacture::getSerialNumber)).collect(Collectors.toList());

        List<Map<String, Object>> list = new ArrayList<>();
        for (Manufacture manufacture : manufactures) {
            Map<String, Object> map = new LinkedHashMap<>();

            map.put("生产基地", manufacture.getManufactureAddress());
            map.put("报工名称", manufacture.getManufactureName());
            if (isPlan) {
                map.put("生产计划编号", manufacture.getPlanNumber());

            }
            map.put("日实际产量(含不良）", manufacture.getDailyOutput());
            map.put("不良品数量", manufacture.getRejectsQuantity());
            map.put("移交数量", manufacture.getTransferQuantity());
            if (isPlan) {
                DailyPlanQueryCriteria dailyPlanQueryCriteria = new DailyPlanQueryCriteria();
                dailyPlanQueryCriteria.setPlanNumber(manufacture.getPlanNumber());
                dailyPlanQueryCriteria.setManufactureName(manufacture.getManufactureName());
                List<DailyPlan> dailyPlanList = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, dailyPlanQueryCriteria, criteriaBuilder));
                if (dailyPlanList != null && dailyPlanList.size() > 0) {
                    DailyPlan dailyPlan = dailyPlanList.get(0);
                    map.put("日计划产量", dailyPlan.getDailyPlanQuantity());
                    map.put("日计划达成率", convertDouble(dailyPlan.getDailyPlanQuantity() == 0 ? 1 : manufacture.getDailyOutput() * 1.0 / dailyPlan.getDailyPlanQuantity()));
                    map.put("日产能不达标原因", manufacture.getIncompleteReasons());
                    map.put("班组人员数", manufacture.getWorkerQuantity());
                    map.put("工时（含加班）", manufacture.getWorkingHours());
                    map.put("废品率异常原因说明", manufacture.getRejectReasons());

                    ProductParameterQueryCriteria productParameterQueryCriteria = new ProductParameterQueryCriteria();
                    productParameterQueryCriteria.setProductName(dailyPlan.getBatchPlan().getProductName());
                    productParameterQueryCriteria.setManufactureName(manufacture.getManufactureName());
                    List<ProductParameter> productParameterList = productParameterRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, productParameterQueryCriteria, criteriaBuilder));
                    if (productParameterList != null && productParameterList.size() > 0) {
                        ProductParameter productParameter = productParameterList.get(0);
                        TechniqueInfo techniqueInfo = productParameter.getTechniqueInfo();
                        map.put("材料1意外消耗", (techniqueInfo.getMaterial1Name() == null || "".equals(techniqueInfo.getMaterial1Name())) ? "" : techniqueInfo.getMaterial1Name() + manufacture.getUnexpectedMaterial1() + (techniqueInfo.getMaterial1Unit() == null || "".equals(techniqueInfo.getMaterial1Unit()) ? "" : techniqueInfo.getMaterial1Unit()));
                        map.put("材料2意外消耗", (techniqueInfo.getMaterial2Name() == null || "".equals(techniqueInfo.getMaterial2Name())) ? "" : techniqueInfo.getMaterial2Name() + manufacture.getUnexpectedMaterial2() + (techniqueInfo.getMaterial2Unit() == null || "".equals(techniqueInfo.getMaterial2Unit()) ? "" : techniqueInfo.getMaterial2Unit()));
                        map.put("材料3意外消耗", (techniqueInfo.getMaterial3Name() == null || "".equals(techniqueInfo.getMaterial3Name())) ? "" : techniqueInfo.getMaterial3Name() + manufacture.getUnexpectedMaterial3() + (techniqueInfo.getMaterial3Unit() == null || "".equals(techniqueInfo.getMaterial3Unit()) ? "" : techniqueInfo.getMaterial3Unit()));
                        map.put("材料4意外消耗", (techniqueInfo.getMaterial4Name() == null || "".equals(techniqueInfo.getMaterial4Name())) ? "" : techniqueInfo.getMaterial4Name() + manufacture.getUnexpectedMaterial4() + (techniqueInfo.getMaterial4Unit() == null || "".equals(techniqueInfo.getMaterial4Unit()) ? "" : techniqueInfo.getMaterial4Unit()));
                    }
                }
            }
            map.put("报工人", "");
            if (manufacture.getUserId() != null && !"".equals(manufacture.getUserId())) {
                SysUser sysUser = sysUserRepository.findById(manufacture.getUserId()).orElseGet(SysUser::new);
                map.put("报工人", sysUser.getUsername());
            }

            map.put("填报日期", dateFormat.format(manufacture.getFillDate()));
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
            map.put("材料1消耗", manufactureSummary.getMaterial1Name() == null || "".equals(manufactureSummary.getMaterial1Name()) ? "" : manufactureSummary.getMaterial1Name() + manufactureSummary.getConsumeMaterial1() + manufactureSummary.getMaterial1Unit());
            map.put("材料2消耗", manufactureSummary.getMaterial2Name() == null || "".equals(manufactureSummary.getMaterial2Name()) ? "" : manufactureSummary.getMaterial2Name() + manufactureSummary.getConsumeMaterial2() + manufactureSummary.getMaterial2Unit());
            map.put("材料3消耗", manufactureSummary.getMaterial3Name() == null || "".equals(manufactureSummary.getMaterial3Name()) ? "" : manufactureSummary.getMaterial3Name() + manufactureSummary.getConsumeMaterial3() + manufactureSummary.getMaterial3Unit());
            map.put("材料4消耗", manufactureSummary.getMaterial4Name() == null || "".equals(manufactureSummary.getMaterial4Name()) ? "" : manufactureSummary.getMaterial4Name() + manufactureSummary.getConsumeMaterial4() + manufactureSummary.getMaterial4Unit());

            map.put("材料1定额达成率", manufactureSummary.getMaterial1Name() == null || "".equals(manufactureSummary.getMaterial1Name()) ? "" : manufactureSummary.getMaterial1Name() + manufactureSummary.getActualMaterial1Quota() * 100 + "%");
            map.put("材料2定额达成率", manufactureSummary.getMaterial2Name() == null || "".equals(manufactureSummary.getMaterial2Name()) ? "" : manufactureSummary.getMaterial2Name() + manufactureSummary.getActualMaterial2Quota() * 100 + "%");
            map.put("材料3定额达成率", manufactureSummary.getMaterial3Name() == null || "".equals(manufactureSummary.getMaterial3Name()) ? "" : manufactureSummary.getMaterial3Name() + manufactureSummary.getActualMaterial3Quota() * 100 + "%");
            map.put("材料4定额达成率", manufactureSummary.getMaterial4Name() == null || "".equals(manufactureSummary.getMaterial4Name()) ? "" : manufactureSummary.getMaterial4Name() + manufactureSummary.getActualMaterial4Quota() * 100 + "%");
            map.put("实际人工时", manufactureSummary.getActualHours());
            map.put("理论工时", manufactureSummary.getTheoryHours());
            map.put("日计划产量", manufactureSummary.getDailyPlanQuantity());
            map.put("日计划完成率", manufactureSummary.getDailyCompletionRate());
            map.put("日不良率%", manufactureSummary.getDailyCompletionRate());
            map.put("批次累计完成数量(合格品）", manufactureSummary.getBatchCompletedQuantity());
            map.put("批次计划累计完成百分比", manufactureSummary.getAccumulativeTotalPercentage());
            map.put("批次不良总数", manufactureSummary.getRejectsTotal());
            map.put("批次平均不良率%", manufactureSummary.getBatchRejectRate());
            map.put("年度平均不良率%", manufactureSummary.getAnnualRejectRate());
            map.put("年度不良品总数", manufactureSummary.getAnnualRejectTotal());
            map.put("年度生产累计总量", manufactureSummary.getAnnualOutputTotal());
            map.put("总计划制造量", manufactureSummary.getTotalPlanQuantity());
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

    @Override
    @Transactional
    public void createManufacture(String today) {
//        String today = dateFormat.format(new Date());
        DailyPlanQueryCriteria dailyPlanQueryCriteria = new DailyPlanQueryCriteria();
        dailyPlanQueryCriteria.setStartDate(today);
        List<DailyPlan> dailyPlanList = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, dailyPlanQueryCriteria, criteriaBuilder));

        if (dailyPlanList != null && dailyPlanList.size() > 0) {

            List<Manufacture> manufactures = new ArrayList<>();
            dailyPlanList.forEach(dailyPlan -> {
                ManufactureQueryCriteria manufactureQueryCriteria = new ManufactureQueryCriteria();
                manufactureQueryCriteria.setPlanNumber(dailyPlan.getPlanNumber());
                manufactureQueryCriteria.setManufactureAddress(dailyPlan.getManufactureAddress());
                manufactureQueryCriteria.setManufactureName(dailyPlan.getManufactureName());
                List<Manufacture> manufactureList = manufactureRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, manufactureQueryCriteria, criteriaBuilder));


                if (manufactureList == null || manufactureList.size() == 0) {
                    int flag = 0;
                    Manufacture manufacture = new Manufacture();
                    manufacture.setPlanNumber(dailyPlan.getPlanNumber());
                    manufacture.setManufactureName(dailyPlan.getManufactureName());
                    manufacture.setManufactureAddress(dailyPlan.getManufactureAddress());
                    try {
                        manufacture.setFillDate(new Timestamp(dateFormat.parse(today).getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    ProductParameterQueryCriteria productParameterQueryCriteria = new ProductParameterQueryCriteria();
                    productParameterQueryCriteria.setProductName(dailyPlan.getBatchPlan().getProductName());
                    productParameterQueryCriteria.setManufactureName(dailyPlan.getManufactureName());
                    List<ProductParameter> productParameterList = productParameterRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, productParameterQueryCriteria, criteriaBuilder));
                    if (productParameterList != null && productParameterList.size() > 0) {
                        ProductParameter productParameter = productParameterList.get(0);
                        manufacture.setSerialNumber(productParameter.getSerialNumber());
                        String[] userIds = productParameter.getPermissionUserIds().split(",");

                        for (int i = 0; i < userIds.length; i++) {
                            SysUser user = sysUserRepository.findById(Long.valueOf(userIds[i])).orElseGet(SysUser::new);
                            if (dailyPlan.getManufactureAddress().equals(user.getUserAddress())) {
                                manufacture.setUserId(user.getUserId());
                                flag++;
                                break;
                            }
                        }
                    }
                    if (flag > 0) {
                        manufacture.setIncompleteReasons("无人报工");
                        manufactures.add(manufacture);
                    }
                }
            });

            if (manufactures != null && manufactures.size() > 0) {
                manufactureRepository.saveAll(manufactures);
            }
        }


    }

    //制造信息修改
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Manufacture resources) {
        Manufacture manufacture = manufactureRepository.findById(resources.getId()).orElseGet(Manufacture::new);
        ValidationUtil.isNull(manufacture.getId(), "manufacture", "id", resources.getId());

        Manufacture manufacture1 = new Manufacture();
        manufacture1.copy(manufacture);

        manufacture.copy(resources);
        manufactureRepository.save(manufacture);
        if (resources.getPlanNumber() != null) {
            updateDailyCompletedQuantity(manufacture.getPlanNumber());
            summary(manufacture.getPlanNumber());
        }

        if ((resources.getDailyOutput() != null && resources.getDailyOutput() != manufacture1.getDailyOutput()) || (resources.getRejectsQuantity() != null && resources.getRejectsQuantity() != manufacture1.getRejectsQuantity()) || (resources.getTransferQuantity() != null && resources.getTransferQuantity() != manufacture1.getTransferQuantity())) {
            manufacture1.setDailyOutput(resources.getDailyOutput() - manufacture1.getDailyOutput());
            manufacture1.setRejectsQuantity(resources.getRejectsQuantity() - manufacture1.getRejectsQuantity());
            manufacture1.setTransferQuantity(resources.getTransferQuantity() - manufacture1.getTransferQuantity());
            stockService.updateBalance(manufacture1, "UPDATE", manufacture);
        }
    }

    @Override
    public List<ManufactureDto> queryManufacture(ManufactureQueryCriteria criteria, Boolean isPlan) {

        UserDetails userDetails = SecurityUtils.getCurrentUser();
        List<JSONObject> roles = (List) new JSONObject(new JSONObject(userDetails).get("user")).get("roles");

        if (roles == null || roles.size() == 0)
            return null;
        int flag = 0;
        Boolean isAdmin = false;
        for (int i = 0; i < roles.size(); i++) {
            Role role = JSONUtil.toBean(roles.get(0), Role.class);
            if ("超级管理员".equals(role.getName())) {
                flag++;
                isAdmin = true;
                break;
            } else if ("测试人员".equals(role.getName()) || "部门主管".equals(role.getName())) {
                flag++;
            }
        }

        Long userId = (Long) new JSONObject(new JSONObject(userDetails).get("user")).get("id");
        if (flag == 0) {
            criteria.setUserId(userId);
        }
        List<Manufacture> manufactureList = manufactureRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));


        List<ManufactureDto> manufactureDtoList = manufactureMapper.toDto(manufactureList);


        Boolean finalIsAdmin = isAdmin;
        manufactureDtoList.forEach(manufactureDto -> {
            Long c = new Date().getTime() - manufactureDto.getFillDate().getTime();
            long d = c / 1000 / 60 / 60 / 24;

            if ((manufactureDto.getUserId() == userId && d < 2) || finalIsAdmin) {
                manufactureDto.setIsSame(true);
            } else {
                manufactureDto.setIsSame(false);
            }

            if (manufactureDto.getUserId() != null && !"".equals(manufactureDto.getUserId())) {
                SysUser sysUser = sysUserRepository.findById(manufactureDto.getUserId()).orElseGet(SysUser::new);
                manufactureDto.setUserName(sysUser.getUsername());
            }

        });


        if (isPlan) {
            manufactureDtoList = manufactureDtoList.stream().filter(a -> {
                return a.getPlanNumber() != null && !"".equals(a.getPlanNumber());
            }).collect(Collectors.toList());


            manufactureDtoList = manufactureDtoList.stream().sorted(Comparator.comparing(ManufactureDto::getFillDate, Comparator.reverseOrder()).thenComparing(ManufactureDto::getManufactureAddress, Comparator.reverseOrder()).thenComparing(ManufactureDto::getSerialNumber)).collect(Collectors.toList());


            manufactureDtoList.forEach(manufactureDto -> {

                DailyPlanQueryCriteria dailyPlanQueryCriteria = new DailyPlanQueryCriteria();
                dailyPlanQueryCriteria.setPlanNumber(manufactureDto.getPlanNumber());
                dailyPlanQueryCriteria.setManufactureName(manufactureDto.getManufactureName());
                List<DailyPlan> dailyPlanList = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, dailyPlanQueryCriteria, criteriaBuilder));
                if (dailyPlanList != null && dailyPlanList.size() > 0) {
                    DailyPlan dailyPlan = dailyPlanList.get(0);
                    manufactureDto.setDailyPlanQuantity(dailyPlan.getDailyPlanQuantity());
                    manufactureDto.setDailyCompletionRate(convertDouble(dailyPlan.getDailyPlanQuantity() == 0 ? 1 : manufactureDto.getDailyOutput() * 1.0 / dailyPlan.getDailyPlanQuantity()));
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
        } else {
            manufactureDtoList = manufactureDtoList.stream().filter(a -> {
                return a.getPlanNumber() == null || "".equals(a.getPlanNumber());
            }).collect(Collectors.toList());


            manufactureDtoList = manufactureDtoList.stream().sorted(Comparator.comparing(ManufactureDto::getFillDate, Comparator.reverseOrder()).thenComparing(ManufactureDto::getManufactureAddress, Comparator.reverseOrder()).thenComparing(ManufactureDto::getSerialNumber)).collect(Collectors.toList());

        }
        return manufactureDtoList;
    }

    @Override
    public Map<String, Object> queryManufactureSummary(ManufactureSummaryQueryCriteria criteria, Pageable pageable) {
        Page<ManufactureSummary> page = manufactureSummaryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(manufactureSummaryMapper::toDto));
    }

    @Override
    public List<Reasons> getReasons() {
        return reasonRepository.findAll();
    }

}
