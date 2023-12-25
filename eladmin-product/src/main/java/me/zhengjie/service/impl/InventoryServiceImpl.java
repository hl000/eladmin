package me.zhengjie.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.InventoryErp;
import me.zhengjie.mapper.InventoryErpMapper;
import me.zhengjie.service.InventoryService;
import me.zhengjie.service.dto.InventoryQueryCriteria;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author HL
 * @create 2023/8/31 15:35
 */
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    @Resource
    private final InventoryErpMapper inventoryErpMapper;

    @Override
    public List<InventoryErp> getInventory(String code, String name, String specs) {
        return inventoryErpMapper.queryInventoryErp(code,name,specs);
    }
}
