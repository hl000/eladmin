package me.zhengjie.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.ElectricPipeActivation;
import me.zhengjie.domain.ManufactureOrder;
import me.zhengjie.domain.WorkGroup;
import me.zhengjie.service.ManufactureOrderService;
import me.zhengjie.service.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/createActive")
    @Log("添加电堆活化测试")
    @ApiOperation("添加电堆活化测试")
    public ElectricPipeActivationDto createActive(@Validated @RequestBody ElectricPipeActivationDto electricPipeActivationDto) {
        return manufactureOrderService.createActive(electricPipeActivationDto);
    }

    @PostMapping("/updateActive")
    @Log("更新电堆活化测试")
    @ApiOperation("更新电堆活化测试")
    public ElectricPipeActivationDto updateActive(@Validated @RequestBody ElectricPipeActivationDto electricPipeActivationDto) {
        return manufactureOrderService.updateActive(electricPipeActivationDto);
    }

    @GetMapping("/queryElectricActivations")
    @Log("查询电堆活化")
    @ApiOperation("查询电堆活化")
    public ResponseEntity<Object> queryElectricActivations(ElectricPipeActivationQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(manufactureOrderService.queryElectricActivation(criteria, pageable), HttpStatus.OK);
    }

    @GetMapping("/getElectricActivationById")
    @Log("根据活化id查询电堆活化详情")
    @ApiOperation("根据活化id查询电堆活化详情")
    public ElectricPipeActivationDto getElectricActivationById(Integer id) {
        return manufactureOrderService.getElectricActivationById(id);
    }

    @GetMapping("/queryWorkDevice")
    @Log("查询设备")
    @ApiOperation("查询设备")
    public ResponseEntity<Object> queryWorkDevice() {
        return new ResponseEntity<>(manufactureOrderService.queryWorkDevice(), HttpStatus.OK);
    }

    @GetMapping("/getManufactureOrderActive")
    @Log("查询生产跟踪单激活次数")
    @ApiOperation("查询生产跟踪单激活次数")
    public ManufactureOrderActiveDto getManufactureOrderActive(String stackNumber) {
        return manufactureOrderService.getManufactureOrderActive(stackNumber);
    }

    @GetMapping("/queryWorkGroup")
    @Log("查询组装活化人员")
    @ApiOperation("查询组装活化人员")
    public List<WorkGroup> queryWorkGroup(WorkGroupQueryCriteria criteria) {
        return manufactureOrderService.queryWorkGroup(criteria);
    }

    @GetMapping("/findAllStackNumber")
    @Log("查询所有电堆编号")
    @ApiOperation("查询所有电堆编号")
    public List<String> findAllStackNumber(StackNumberQueryCriteria criteria) {
        return manufactureOrderService.findAllStackNumber(criteria);
    }
}
