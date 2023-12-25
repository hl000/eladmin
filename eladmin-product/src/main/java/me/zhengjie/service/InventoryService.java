package me.zhengjie.service;

import me.zhengjie.domain.InventoryErp;
import me.zhengjie.service.dto.InventoryQueryCriteria;

import java.util.List;

/**
 * @author HL
 * @create 2023/8/31 15:31
 */
public interface InventoryService {
    List<InventoryErp> getInventory(String code, String name, String specs);
}
