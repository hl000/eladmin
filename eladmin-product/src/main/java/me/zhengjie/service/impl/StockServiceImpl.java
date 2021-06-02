package me.zhengjie.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.*;
import me.zhengjie.mapper.BalanceMapper;
import me.zhengjie.repository.BalanceRepository;
import me.zhengjie.repository.MaterialRatioRepository;
import me.zhengjie.repository.ProcessRelationRepository;
import me.zhengjie.repository.ProductParameterRepository;
import me.zhengjie.service.StockService;
import me.zhengjie.service.dto.BalanceQueryCriteria;
import me.zhengjie.service.dto.MaterialRatioQueryCriteria;
import me.zhengjie.service.dto.ProcessRelationQueryCriteria;
import me.zhengjie.service.dto.ProductParameterQueryCriteria;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.QueryHelp;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HL
 * @create 2021/5/19 14:00
 */
@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final ProductParameterRepository productParameterRepository;

    private final ProcessRelationRepository processRelationRepository;

    private final BalanceRepository balanceRepository;

    private final MaterialRatioRepository materialRatioRepository;

    @Resource(name = "balanceMapperImpl")
    private BalanceMapper balanceMapper;

    @Override
    public List<Balance> queryBalance(BalanceQueryCriteria criteria) {

        List<Balance> balances = balanceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        balances = balances.stream().filter(a -> {
            return a.getRemainQuantity() > 0;
        }).collect(Collectors.toList());

        List<Balance> rst = balances.stream().sorted((a, b) -> {
            if (a.getCategory() == null || b.getCategory() == null || a.getProductParameter() == null || b.getProductParameter() == null ||
                    a.getCategory().getProcessSequence() == null || b.getCategory().getProcessSequence() == null || a.getProductParameter().getSerialNumber() == null || b.getProductParameter().getSerialNumber() == null) {
                return 0;
            }

            if (a.getCategory().getProcessSequence() - b.getCategory().getProcessSequence() == 0) {
                return a.getProductParameter().getSerialNumber() - b.getProductParameter().getSerialNumber();
            }
            return a.getCategory().getProcessSequence() - b.getCategory().getProcessSequence();
        }).collect(Collectors.toList());

        rst = rst.stream()
                .sorted(Comparator.comparing(Balance::getManufactureAddress))
                .collect(Collectors.toList());
        return rst;
    }

    @Override
    @Transactional
    public void updateBalance(Manufacture manufacture) {

        //查询报工名称，工序名称
        ProductParameterQueryCriteria productParameterQueryCriteria = new ProductParameterQueryCriteria();
        productParameterQueryCriteria.setManufactureName(manufacture.getManufactureName());
        List<ProductParameter> productParameterList = productParameterRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, productParameterQueryCriteria, criteriaBuilder));
        if (productParameterList == null || productParameterList.size() == 0)
            return;

        ProductParameter productParameter = productParameterList.get(0);


        BalanceQueryCriteria balanceQueryCriteria2 = new BalanceQueryCriteria();
        balanceQueryCriteria2.setManufactureName(manufacture.getManufactureName());
        balanceQueryCriteria2.setSecondaryType(productParameter.getTechniqueInfo().getCategory().getSecondaryType());
        balanceQueryCriteria2.setManufactureAddress(manufacture.getManufactureAddress());
        List<Balance> balances2 = balanceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, balanceQueryCriteria2, criteriaBuilder));
        if (balances2 != null && balances2.size() > 0) {
            Balance balance = balances2.get(0);
            balance.setRemainQuantity(balance.getRemainQuantity() + manufacture.getDailyOutput() - manufacture.getTransferQuantity());
            balanceRepository.save(balance);
        } else {
            Balance balance = new Balance();
            balance.setManufactureAddress(manufacture.getManufactureAddress());
            balance.setProductParameter(productParameter);
            balance.setCategory(productParameter.getTechniqueInfo().getCategory());
            balance.setRemainQuantity(manufacture.getDailyOutput() - manufacture.getTransferQuantity());
            balanceRepository.save(balance);
        }


        //找出报工的产品和工序
        ProcessRelationQueryCriteria processRelationQueryCriteria = new ProcessRelationQueryCriteria();
        processRelationQueryCriteria.setManufactureName(manufacture.getManufactureName());
        processRelationQueryCriteria.setSecondaryType(productParameter.getTechniqueInfo().getCategory().getSecondaryType());
        List<ProcessRelation> processRelationList = processRelationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, processRelationQueryCriteria, criteriaBuilder));


        if (processRelationList != null && processRelationList.size() > 0) {
            processRelationList.forEach(processRelation -> {

                //计算原材料消耗和产品库存
                if (processRelation.getProjectOrder() > 1) {
                    ProcessRelationQueryCriteria processRelationQueryCriteria1 = new ProcessRelationQueryCriteria();
                    processRelationQueryCriteria1.setProcess(processRelation.getProcess());
                    processRelationQueryCriteria1.setProjectOrder(processRelation.getProjectOrder() - 1);

                    List<ProcessRelation> processRelationList1 = processRelationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, processRelationQueryCriteria1, criteriaBuilder));
                    if (processRelationList1 != null && processRelationList1.size() > 0) {
                        ProcessRelation processRelation1 = processRelationList1.get(0);

                        MaterialRatioQueryCriteria materialRatioQueryCriteria = new MaterialRatioQueryCriteria();
                        materialRatioQueryCriteria.setManufactureName(manufacture.getManufactureName());
                        materialRatioQueryCriteria.setMaterialName(processRelation1.getProductParameter().getManufactureName());
                        List<MaterialRatio> materialRatios = materialRatioRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, materialRatioQueryCriteria, criteriaBuilder));

                        if (materialRatios != null && materialRatios.size() > 0) {
                            MaterialRatio materialRatio = materialRatios.get(0);
                            BalanceQueryCriteria balanceQueryCriteria = new BalanceQueryCriteria();
                            balanceQueryCriteria.setManufactureName(processRelation1.getProductParameter().getManufactureName());
                            balanceQueryCriteria.setSecondaryType(processRelation.getCategory().getSecondaryType());
                            balanceQueryCriteria.setManufactureAddress(manufacture.getManufactureAddress());
                            List<Balance> balances = balanceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, balanceQueryCriteria, criteriaBuilder));
                            if (balances != null && balances.size() > 0) {
                                Balance balance = balances.get(0);
                                balance.setRemainQuantity(balance.getRemainQuantity() - manufacture.getDailyOutput() * materialRatio.getConsumedQuantity());
                                balanceRepository.save(balance);
                            } else {
                                Balance balance3 = new Balance();
                                balance3.setManufactureAddress(manufacture.getManufactureAddress());
                                balance3.setCategory(processRelation.getCategory());
                                balance3.setProductParameter(processRelation1.getProductParameter());
                                balance3.setRemainQuantity(0 - manufacture.getDailyOutput() * materialRatio.getConsumedQuantity());
                                balanceRepository.save(balance3);
                            }
                        }
                    }
                }

                //更新移交
                ProcessRelationQueryCriteria processRelationQueryCriteria1 = new ProcessRelationQueryCriteria();
                processRelationQueryCriteria1.setProcess(processRelation.getProcess());
                processRelationQueryCriteria1.setProjectOrder(processRelation.getProjectOrder() + 1);

                List<ProcessRelation> processRelationList1 = processRelationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, processRelationQueryCriteria1, criteriaBuilder));
                if (processRelationList1 != null && processRelationList1.size() > 0) {
                    ProcessRelation processRelation1 = processRelationList1.get(0);

                    BalanceQueryCriteria balanceQueryCriteria = new BalanceQueryCriteria();
                    balanceQueryCriteria.setManufactureName(manufacture.getManufactureName());
                    balanceQueryCriteria.setSecondaryType(processRelation1.getCategory().getSecondaryType());
                    balanceQueryCriteria.setManufactureAddress(manufacture.getManufactureAddress());
                    List<Balance> balances = balanceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, balanceQueryCriteria, criteriaBuilder));

                    if (balances != null && balances.size() > 0) {
                        Balance balance = balances.get(0);
                        balance.setRemainQuantity(balance.getRemainQuantity() + manufacture.getTransferQuantity());
                        balanceRepository.save(balance);
                    } else {
                        Balance balance = new Balance();
                        balance.setManufactureAddress(manufacture.getManufactureAddress());
                        balance.setCategory(processRelation1.getCategory());
                        balance.setProductParameter(productParameter);
                        balance.setRemainQuantity(manufacture.getTransferQuantity());
                        balanceRepository.save(balance);
                    }
                }
            });


        }
    }

    @Override
    public void download(HttpServletResponse response, BalanceQueryCriteria criteria) {
        List<Balance> balances = balanceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        List<Balance> rst = balances.stream().sorted((a, b) -> {
            if (a.getCategory() == null || b.getCategory() == null || a.getProductParameter() == null || b.getProductParameter() == null ||
                    a.getCategory().getProcessSequence() == null || b.getCategory().getProcessSequence() == null || a.getProductParameter().getSerialNumber() == null || b.getProductParameter().getSerialNumber() == null) {
                return 0;
            }
            if (a.getCategory().getProcessSequence() - b.getCategory().getProcessSequence() == 0) {
                return a.getProductParameter().getSerialNumber() - b.getProductParameter().getSerialNumber();
            }
            return a.getCategory().getProcessSequence() - b.getCategory().getProcessSequence();
        }).collect(Collectors.toList());


        List<Map<String, Object>> list = new ArrayList<>();
        for (Balance balance : rst) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("生产基地", balance.getManufactureAddress());
            map.put("工序名称", balance.getCategory().getSecondaryType());
            map.put("工序半成品名称", balance.getProductParameter().getManufactureName());
            map.put("工序半成品数量", balance.getRemainQuantity());
            map.put("更新", balance.getUpdateTime());
            list.add(map);
        }
        try {
            FileUtil.downloadExcel(list, response);
        } catch (Exception e) {
        }
    }


}
