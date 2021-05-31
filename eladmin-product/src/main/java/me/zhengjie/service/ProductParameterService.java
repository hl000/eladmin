package me.zhengjie.service;

import me.zhengjie.domain.DailyPlan;
import me.zhengjie.domain.ProductParameter;
import me.zhengjie.service.dto.ProductParameterQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author HL
 * @create 2021/4/23 11:01
 */
public interface ProductParameterService {
    ProductParameter create(ProductParameter resources);

    void updateProductParameter(ProductParameter resources);

    Map<String,Object> queryAll(ProductParameterQueryCriteria criteria, Pageable pageable);

    List<ProductParameter> queryAll(ProductParameterQueryCriteria criteria);


    List<ProductParameter> getProductParameterByDailyPlan(DailyPlan dailyPlan);

    Object getManufactureName();
}
