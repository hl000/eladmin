package me.zhengjie.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.service.WorkPlanService;
import me.zhengjie.service.dto.WorkPlanDetailQueryCriteria;
import me.zhengjie.service.dto.WorkPlanGroupDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author HL
 * @create 2021/12/13 10:07
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "工作计划管理")
@RequestMapping("/api/workPlan")
public class WorkPlanController {

    private final WorkPlanService workPlanService;

    @GetMapping("/queryAllWorkPlan")
    @Log("查询工作计划")
    @ApiOperation("查询工作计划")
    public ResponseEntity<Object> queryAllWorkPlan() {
        return new ResponseEntity<>(workPlanService.queryAllWorkPlan(), HttpStatus.OK);
    }


    @GetMapping("/queryAllWorkPlanType")
    @Log("计划类型下拉框")
    @ApiOperation("计划类型下拉框")
    public ResponseEntity<Object> queryAllWorkPlanType() {
        return new ResponseEntity<>(workPlanService.queryAllWorkPlanType(), HttpStatus.OK);
    }


    @PostMapping("/createWorkPlan")
    @Log("新增工作计划")
    @ApiOperation("新增工作计划")
    public ResponseEntity<Object> createWorkPlan(@Validated @RequestBody WorkPlanGroupDto resources) {
        return new ResponseEntity<>(workPlanService.createWorkPlan(resources), HttpStatus.OK);
    }

    @PostMapping("/updateWorkPlan")
    @Log("更新工作计划")
    @ApiOperation("更新工作计划")
    public ResponseEntity<Object> updateWorkPlan(@Validated @RequestBody WorkPlanGroupDto resources) {
        return new ResponseEntity<>(workPlanService.updateWorkPlan(resources), HttpStatus.OK);
    }

    @GetMapping("/getWorkPlanDetail")
    @Log("查询工作计划详情")
    @ApiOperation("查询工作计划详情")
    public Object getWorkPlanDetail(WorkPlanDetailQueryCriteria criteria, Pageable pageable) {
        return workPlanService.findWorkPlanDetails(criteria, pageable);
    }

    @GetMapping("/getWorkPlanDetailHistory")
    @Log("查询工作计划历史记录")
    @ApiOperation("查询工作计划历史记录")
    public ResponseEntity<Object> getWorkPlanDetailHistory(Integer id) {
        return new ResponseEntity<>(workPlanService.getWorkPlanDetailHistory(id), HttpStatus.OK);
    }

    @GetMapping("/queryAllOutPut")
    @Log("查询工作计划结果输出")
    @ApiOperation("查询工作计划结果输出")
    public ResponseEntity<Object> queryAllOutPut() {
        return new ResponseEntity<>(workPlanService.queryAllOutPut(), HttpStatus.OK);
    }

    @GetMapping("/queryDutyPerson")
    @Log("查询负责人下拉列表")
    @ApiOperation("查询负责人下拉列表")
    public ResponseEntity<Object> queryDutyPerson() {
        return new ResponseEntity<>(workPlanService.queryDutyPerson(), HttpStatus.OK);
    }

    @DeleteMapping("/deleteWorkPlanDetail")
    @Log("删除工作计划详情")
    @ApiOperation("删除工作计划详情")
    public ResponseEntity<Object> deleteWorkPlanDetail(@RequestBody Integer id) {
        return new ResponseEntity<>(workPlanService.deleteWorkPlanDetail(id), HttpStatus.OK);
    }

    @DeleteMapping("/deleteWorkPlan")
    @Log("删除工作计划")
    @ApiOperation("删除工作计划")
    public ResponseEntity<Object> deleteWorkPlan(@RequestBody Integer id) {
        return new ResponseEntity<>(workPlanService.deleteWorkPlan(id), HttpStatus.OK);
    }

    @GetMapping("/getWorkPlanDetail/download")
    @Log("下载工作计划详情")
    @ApiOperation("下载工作计划详情")
    public void downloadWorkPlanDetail(HttpServletResponse response, WorkPlanDetailQueryCriteria criteria) throws IOException {
        workPlanService.downloadWorkPlanDetails(response, criteria);
    }
}

