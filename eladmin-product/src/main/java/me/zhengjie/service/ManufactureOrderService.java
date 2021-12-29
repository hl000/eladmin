package me.zhengjie.service;

import me.zhengjie.domain.ManufactureOrder;
import me.zhengjie.service.dto.ManufactureOrderQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author HL
 * @create 2021/7/14 23:45
 */
public interface ManufactureOrderService {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(ManufactureOrderQueryCriteria criteria, Pageable pageable);

    List<ManufactureOrder> queryAll(ManufactureOrderQueryCriteria criteria);


    /**
     * 创建
     *
     * @param resources /
     * @return ManufactureOrder
     */
    ManufactureOrder create(ManufactureOrder resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    ManufactureOrder update(ManufactureOrder resources);
}