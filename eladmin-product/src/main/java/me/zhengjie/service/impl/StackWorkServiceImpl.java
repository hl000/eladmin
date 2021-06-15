package me.zhengjie.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.Utils.DownExcelUtil;
import me.zhengjie.domain.ProductNum;
import me.zhengjie.domain.StackRemainView;
import me.zhengjie.domain.StackWorkView;
import me.zhengjie.mapper.StackRemainMapper;
import me.zhengjie.mapper.StackWorkMapper;
import me.zhengjie.mapper.SysLocalBatisMapper;
import me.zhengjie.repository.StackRemainViewRepository;
import me.zhengjie.repository.StackWorkViewRepository;
import me.zhengjie.service.StackWorkService;
import me.zhengjie.service.dto.StackSummary;
import me.zhengjie.service.dto.StackWorkQueryCriteria;
import me.zhengjie.service.dto.SysLocalDto;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.QueryHelp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HL
 * @create 2021/6/7 10:02
 */
@Service
@RequiredArgsConstructor
public class StackWorkServiceImpl implements StackWorkService {

    private final StackWorkViewRepository stackWorkViewRepository;
    private final StackRemainViewRepository stackRemainViewRepository;

    @Resource(name = "stackRemainMapperImpl")
    private StackRemainMapper stackRemainMapper;
    @Resource(name = "stackWorkMapperImpl")
    private StackWorkMapper stackWorkMapper;
    @Resource
    private SysLocalBatisMapper sysLocalBatisMapper;

    @Override
    public List<Map<String, Object>> getStackWork(StackWorkQueryCriteria criteria) {

        List<SysLocalDto> addressList = sysLocalBatisMapper.getLocal();

        List<StackRemainView> stackRemainViews = stackRemainViewRepository.findStackRemainViews();
        List<StackSummary> stackSummaries = stackRemainMapper.toDto(stackRemainViews);

        List<StackWorkView> stackWorkViewList = stackWorkViewRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        List<StackSummary> stackSummaries1 = stackWorkMapper.toDto(stackWorkViewList);

        stackSummaries.addAll(stackSummaries1);


        List<Map<String, Object>> list = new ArrayList<>();

        for (SysLocalDto sysLocalDto : addressList) {
            Map<String, Object> product = new HashMap<>();
            product.put("address", sysLocalDto.getLocalName());
            product.put("fDate", criteria.getFillDate());
            List<StackSummary> stackGroupByAddress = stackSummaries.stream().filter(a -> {
                return a.getManufactureAddress().equals(sysLocalDto.getLocalName());
            }).collect(Collectors.toList());
            product.put("productInfoList", getProducts(stackGroupByAddress));
            list.add(product);
        }
        Map<String, Object> summary = new HashMap<>();
        summary.put("address", "合计");
        summary.put("fDate", criteria.getFillDate());
        summary.put("productInfoList", getProducts(stackSummaries));
        list.add(summary);
        return list;
    }


    public List<ProductNum> getProducts(List<StackSummary> stackSummaries) {
        Map<String, List<StackSummary>> groupByMap = stackSummaries.stream().collect(Collectors.groupingBy(stack -> {
            return stack.getComponent();
        }));

        List<ProductNum> productNums = new ArrayList<>();
        for (Map.Entry<String, List<StackSummary>> entry : groupByMap.entrySet()) {
            List<StackSummary> stackSummaries2 = entry.getValue();
            ProductNum productNum = new ProductNum();
            productNum.setModelName(stackSummaries2.get(0).getComponent());
            for (StackSummary stackSummary : stackSummaries2) {
                if (stackSummary.getManufactureName().contains("活化") && stackSummary.getDailyOutput() != null) {
                    productNum.setActivationNum(productNum.getActivationNum() + stackSummary.getDailyOutput());
                } else if (stackSummary.getManufactureName().contains("活化") && stackSummary.getRemainQuantity() != null) {
                    productNum.setActivationBalance(productNum.getActivationBalance() + stackSummary.getRemainQuantity());
                } else if (stackSummary.getManufactureName().contains("电堆组装") && stackSummary.getDailyOutput() != null) {
                    productNum.setAssemblyNum(productNum.getAssemblyNum() + stackSummary.getDailyOutput());
                } else if (stackSummary.getManufactureName().contains("电堆组装") && stackSummary.getRemainQuantity() != null) {
                    productNum.setAssemblyBalance(productNum.getAssemblyNum() + stackSummary.getRemainQuantity());
                }
            }
            productNums.add(productNum);
        }
        productNums.stream().sorted(Comparator.comparing(ProductNum::getModelName));
        return productNums;
    }

    @Override
    public void stackWorkDownload(StackWorkQueryCriteria criteria, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> stacks = getStackWork(criteria);
        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, Object>> layoutList = new ArrayList<>();
        List<Integer> cellList = new ArrayList<>();
        int size = 0;
        List<Integer> integerList = new ArrayList<>();
        for (Map<String, Object> product : stacks) {
            List<ProductNum> productInfoList = (List<ProductNum>) product.get("productInfoList");
            if (productInfoList == null || productInfoList.size() == 0) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("日期", product.get("fDate"));
                map.put("生产基地", product.get("address"));
                map.put("产品型号", null);
                map.put("今日组装", null);
                map.put("组装结存", null);
                map.put("今日活化", null);
                map.put("活化结存", null);
                list.add(map);
                integerList.add(size + 1);
                size = size + 1;
                integerList.add(size);
                continue;
            }
            integerList.add(size + 1);
            size = size + productInfoList.size();
            integerList.add(size);
            for (ProductNum productInfo : productInfoList) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("日期", product.get("fDate"));
                map.put("生产基地", product.get("address"));
                map.put("产品型号", productInfo.getModelName());
                map.put("今日组装", productInfo.getAssemblyNum());
                map.put("组装结存", productInfo.getAssemblyBalance());
                map.put("今日活化", productInfo.getActivationNum());
                map.put("活化结存", productInfo.getActivationBalance());
                list.add(map);
            }
        }
        Map<String, Object> layoutMap = new LinkedHashMap<>();
        layoutMap.put("firstRow", 1);
        layoutMap.put("lastRow", size);
        layoutMap.put("firstColumn", 0);
        layoutMap.put("lastColumn", 0);
        layoutList.add(layoutMap);
        for (int i = 0; i < integerList.size() - 1; i++) {
            if (integerList.get(i + 1) > integerList.get(i)) {
                Map<String, Object> layoutMap2 = new LinkedHashMap<>();
                layoutMap2.put("firstRow", integerList.get(i));
                layoutMap2.put("lastRow", integerList.get(i + 1));
                layoutMap2.put("firstColumn", 1);
                layoutMap2.put("lastColumn", 1);
                layoutList.add(layoutMap2);
            }
            i++;

        }
        DownExcelUtil.downloadExcelByNum(list, layoutList, cellList, response);
    }
}
