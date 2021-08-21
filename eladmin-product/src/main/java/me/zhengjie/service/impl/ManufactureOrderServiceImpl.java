package me.zhengjie.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.ManufactureOrder;
import me.zhengjie.repository.ManufactureOrderRepository;
import me.zhengjie.service.ManufactureOrderService;
import me.zhengjie.service.dto.ManufactureOrderQueryCriteria;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author HL
 * @create 2021/7/14 23:45
 */
@Service
@RequiredArgsConstructor
public class ManufactureOrderServiceImpl implements ManufactureOrderService {

    private final ManufactureOrderRepository manufactureOrderRepository;

    @Override
    public Map<String, Object> queryAll(ManufactureOrderQueryCriteria criteria, Pageable pageable) {
        Page<ManufactureOrder> page = manufactureOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page);
    }

    @Override
    public List<ManufactureOrder> queryAll(ManufactureOrderQueryCriteria criteria) {
        return manufactureOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
    }

    @Override
    public ManufactureOrder create(ManufactureOrder resources) {
        return manufactureOrderRepository.save(resources);
    }

    @Override
    public ManufactureOrder update(ManufactureOrder resources) {
        ManufactureOrder manufactureOrder = manufactureOrderRepository.findById(resources.getId()).orElseGet(ManufactureOrder::new);
        ValidationUtil.isNull(manufactureOrder.getId(), "manufactureOrder", "id", resources.getId());
        manufactureOrder.copy(resources);
        return manufactureOrderRepository.save(manufactureOrder);
    }
}
