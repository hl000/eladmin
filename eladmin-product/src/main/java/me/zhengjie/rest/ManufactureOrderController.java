package me.zhengjie.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.ManufactureOrder;
import me.zhengjie.service.ManufactureOrderService;
import me.zhengjie.service.dto.ManufactureOrderQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author HL
 * @create 2021/7/14 22:59
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "电堆生产跟踪单")
@RequestMapping("/api/manufactureOrder")
public class ManufactureOrderController {

    private final ManufactureOrderService manufactureOrderService;

    @PostMapping("/create")
    @Log("新增电堆生产跟踪单")
    @ApiOperation("新增电堆生产跟踪单")
    public ManufactureOrder create(@Validated @RequestBody ManufactureOrder manufactureOrder) {
        return manufactureOrderService.create(manufactureOrder);
    }

    @GetMapping("/queryManufactureOrder")
    @Log("查询电堆生产跟踪单")
    @ApiOperation("查询电堆生产跟踪单")
    public ResponseEntity<Object> queryManufactureOrder(ManufactureOrderQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(manufactureOrderService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PutMapping(value = "/update")
    @Log("更新电堆生产跟踪单")
    @ApiOperation("更新电堆生产跟踪单")
    public ManufactureOrder update(@Validated @RequestBody ManufactureOrder manufactureOrder) {
        return manufactureOrderService.update(manufactureOrder);
    }

}
