package me.zhengjie.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.*;
import me.zhengjie.mapper.StockMapper;
import me.zhengjie.repository.CategoryRepository;
import me.zhengjie.repository.DailyPlanRepository;
import me.zhengjie.repository.ProductParameterRepository;
import me.zhengjie.repository.StockRepository;
import me.zhengjie.service.StockService;
import me.zhengjie.service.dto.*;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HL
 * @create 2021/5/19 14:00
 */
@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    private final ProductParameterRepository productParameterRepository;

    private final DailyPlanRepository dailyPlanRepository;

    private final CategoryRepository categoryRepository;

    @Resource(name = "stockMapperImpl")
    private StockMapper stockMapper;

    @Override
    public Map<String, Object> queryAll(StockQueryCriteria criteria, Pageable pageable) {
        Page<Stock> page = stockRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(stockMapper::toDto));
    }

    @Override
    public List<StockDto> queryAll(StockQueryCriteria criteria) {
        return stockMapper.toDto(stockRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public void updateStock(Manufacture manufacture) {
        DailyPlanQueryCriteria dailyPlanQueryCriteria = new DailyPlanQueryCriteria();
        dailyPlanQueryCriteria.setPlanNumber(manufacture.getPlanNumber());
        List<DailyPlan> dailyPlanList = dailyPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, dailyPlanQueryCriteria, criteriaBuilder));
        if (dailyPlanList != null && dailyPlanList.size() > 0) {
            DailyPlan dailyPlan = dailyPlanList.get(0);

            ProductParameterQueryCriteria productParameterQueryCriteria = new ProductParameterQueryCriteria();
            productParameterQueryCriteria.setProductName(dailyPlan.getBatchPlan().getProductName());
            productParameterQueryCriteria.setManufactureName(dailyPlan.getManufactureName());
            List<ProductParameter> productParameterList = productParameterRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, productParameterQueryCriteria, criteriaBuilder));
            if (productParameterList != null && productParameterList.size() > 0) {
                ProductParameter productParameter = productParameterList.get(0);

                //查找当前报工工序的库存
                StockQueryCriteria stockQueryCriteria = new StockQueryCriteria();
                stockQueryCriteria.setProcessName(productParameter.getTechniqueInfo().getCategory().getSecondaryType());
                stockQueryCriteria.setManufactureAddress(dailyPlan.getManufactureAddress());
                stockQueryCriteria.setManufactureName(dailyPlan.getManufactureName());
                List<Stock> stocks = stockRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, stockQueryCriteria, criteriaBuilder));
                Stock stock = new Stock();
                if (stocks != null && stocks.size() > 0) {
                    stock = stocks.get(0);
                } else {
                    stock.setProcessName(productParameter.getTechniqueInfo().getCategory().getSecondaryType());
                    stock.setManufactureAddress(dailyPlan.getManufactureAddress());
                    stock.setManufactureName(dailyPlan.getManufactureName());
                    stock.setSerialNumber(productParameter.getSerialNumber());
                    stock.setProcessNumber(productParameter.getTechniqueInfo().getCategory().getId());
                }
                Integer dailyTotal = manufacture.getDailyOutput() - manufacture.getRejectsQuantity() - manufacture.getTransferQuantity();
                stock.setQuantity(stock.getQuantity() == null ? dailyTotal : stock.getQuantity() + dailyTotal);
                stockRepository.save(stock);

                //查询下一道工序报工名称
                if (manufacture.getManufactureName().contains("密封-胶线") || manufacture.getTransferQuantity() == 0)
                    return;

                if (productParameter.getTechniqueInfo().getCategory().getProcessSequence() != null && !"".equals(productParameter.getTechniqueInfo().getCategory().getProcessSequence())) {
                    //查询下一道工序存库
                    Category category = categoryRepository.findById(productParameter.getTechniqueInfo().getCategory().getProcessSequence()).orElseGet(Category::new);
                    StockQueryCriteria stockQueryCriteria1 = new StockQueryCriteria();
                    stockQueryCriteria1.setProcessName(category.getSecondaryType());
                    stockQueryCriteria1.setManufactureAddress(manufacture.getManufactureAddress());
                    stockQueryCriteria1.setManufactureName(manufacture.getManufactureName());
                    List<Stock> stocks1 = stockRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, stockQueryCriteria1, criteriaBuilder));
                    Stock stock2 = new Stock();
                    if (stocks1 != null && stocks1.size() > 0) {
                        //查询下一道工序该原材料库存
                        stock2 = stocks1.get(0);
                        stock2.setQuantity(stock2.getQuantity() + manufacture.getTransferQuantity());
                    } else {
                        stock2.setProcessName(category.getSecondaryType());
                        stock2.setManufactureAddress(dailyPlan.getManufactureAddress());
                        stock2.setManufactureName(manufacture.getManufactureName());
                        stock2.setQuantity(manufacture.getTransferQuantity());
                        stock.setSerialNumber(productParameter.getSerialNumber());
                        stock2.setProcessNumber(category.getId());
                    }
                    stockRepository.save(stock2);
                }
            }
        }

    }

    @Override
    public void download(HttpServletResponse response, StockQueryCriteria criteria) {
        List<Stock> stocks = stockRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        List<Map<String, Object>> list = new ArrayList<>();

        for (Stock stock : stocks) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("生产基地", stock.getManufactureAddress());
            map.put("工序名称", stock.getProcessName());
            map.put("工序半成品名称", stock.getManufactureName());
            map.put("工序半成品数量", stock.getQuantity());
            map.put("创建时间", stock.getCreateTime());
            map.put("更新时间", stock.getUpdateTime());
            list.add(map);
        }
        try {
            FileUtil.downloadExcel(list, response);
        } catch (Exception e) {
        }
    }


}
