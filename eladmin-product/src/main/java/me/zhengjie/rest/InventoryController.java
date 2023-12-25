package me.zhengjie.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.base.MergeResult;
import me.zhengjie.domain.InventoryErp;
import me.zhengjie.service.InventoryService;
import me.zhengjie.service.dto.InventoryQueryCriteria;
import me.zhengjie.utils.PageUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author HL
 * @create 2023/8/31 10:32
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "库存详情")
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/getInventory")
    @Log("查询库存")
    @ApiOperation("查询库存")
    public Object getInventory(String code, String name, String specs, Pageable pageable) {
        List<InventoryErp> erpList = inventoryService.getInventory(code,name,specs);
        MergeResult mergeResult = new MergeResult();
        mergeResult.totalElements = erpList.size();
        mergeResult.totalPages = erpList.size() % pageable.getPageSize() == 0 ? erpList.size() / pageable.getPageSize() : erpList.size() / pageable.getPageSize() + 1;
        mergeResult.currentPage = pageable.getPageNumber() + 1;
        mergeResult.size = pageable.getPageSize();
        mergeResult.content = PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), erpList);
        return mergeResult;
    }
}
