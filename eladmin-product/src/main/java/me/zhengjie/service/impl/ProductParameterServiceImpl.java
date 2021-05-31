package me.zhengjie.service.impl;

import cn.hutool.json.JSONObject;
import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.*;
import me.zhengjie.mapper.DailyPlanMapper;
import me.zhengjie.mapper.ProductParameterMapper;
import me.zhengjie.repository.BatchPlanRepository;
import me.zhengjie.repository.ManufactureRepository;
import me.zhengjie.repository.ProductParameterRepository;
import me.zhengjie.service.ProductParameterService;
import me.zhengjie.service.dto.BatchPlanQueryCriteria;
import me.zhengjie.service.dto.ManufactureQueryCriteria;
import me.zhengjie.service.dto.ProductParameterQueryCriteria;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Parameter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author HL
 * @create 2021/4/23 11:01
 */
@Service
@RequiredArgsConstructor
public class ProductParameterServiceImpl implements ProductParameterService {

    @Resource(name = "productParameterMapperImpl")
    private ProductParameterMapper productParameterMapper;

    private final ProductParameterRepository productParameterRepository;

    private final ManufactureRepository manufactureRepository;


    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductParameter create(ProductParameter resources) {
        return productParameterRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProductParameter(ProductParameter resources) {
        ProductParameter productParameter = productParameterRepository.findById(resources.getId()).orElseGet(ProductParameter::new);
        ValidationUtil.isNull(productParameter.getId(), "productParameter", "id", resources.getId());

        productParameter.copy(resources);
        productParameterRepository.save(productParameter);
    }

    @Override
    public Map<String, Object> queryAll(ProductParameterQueryCriteria criteria, Pageable pageable) {
        Page<ProductParameter> page = productParameterRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(productParameterMapper::toDto));

    }

    @Override
    public List<ProductParameter> queryAll(ProductParameterQueryCriteria criteria) {
        return productParameterRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
    }


    @Override
    public List<ProductParameter> getProductParameterByDailyPlan(DailyPlan dailyPlan) {
        ProductParameterQueryCriteria productParameterQueryCriteria = new ProductParameterQueryCriteria();
        productParameterQueryCriteria.setManufactureName(dailyPlan.getManufactureName());
        productParameterQueryCriteria.setProductName(dailyPlan.getBatchPlan().getProductName());
        return productParameterRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, productParameterQueryCriteria, criteriaBuilder));
    }

    @Override
    public Object getManufactureName() {
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        Long userId = (Long) new JSONObject(new JSONObject(userDetails).get("user")).get("id");

        List<ProductParameter> productParameterList = productParameterRepository.findAll();

        Set set = new HashSet<>();
        for (ProductParameter productParameter : productParameterList) {
            String[] userIds = productParameter.getPermissionUserIds().split(",");
            for (String id : userIds) {
                if (Long.valueOf(id) == userId) {
                    set.add(productParameter.getManufactureName());
                }
            }
        }

        return set;
    }

}
