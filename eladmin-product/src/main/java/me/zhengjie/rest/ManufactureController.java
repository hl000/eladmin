package me.zhengjie.rest;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.base.MergeResult;
import me.zhengjie.domain.Manufacture;
import me.zhengjie.service.ManufactureService;
import me.zhengjie.service.dto.*;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;

/**
 * @author HL
 * @create 2021/4/13 18:09
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "制造信息汇总管理")
@RequestMapping("/api/manufacture")
public class ManufactureController {

    private final ManufactureService manufactureService;

    @PostMapping("/add")
    @Log("新增manufacture")
    @ApiOperation("新增manufacture")
    public Object create(@Validated @RequestBody Manufacture resources) throws ParseException {
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        Long userId = (Long) new JSONObject(new JSONObject(userDetails).get("user")).get("id");
        resources.setUserId(userId);
        return manufactureService.create(resources) != null;
    }

    @PostMapping("/addUnplanned")
    @Log("新增计划外报工")
    @ApiOperation("新增计划外报工")
    public Object addUnplanned(@Validated @RequestBody UnPlannedManufactureDto resources) {
        return manufactureService.unplannedManufacture(resources) != null;
    }

    @GetMapping("/getManufacture")
    @Log("查询getManufacture")
    @ApiOperation("查询getManufacture")
    public Object queryManufacture(ManufactureQueryCriteria criteria, Pageable pageable, Boolean isPlan) {
        return getObject(criteria, pageable, isPlan);
    }

    @GetMapping("/getManufacture/download")
    @Log("导出")
    @ApiOperation("导出")
    public void downloadManufacture(HttpServletResponse response, ManufactureQueryCriteria criteria, Boolean isPlan) {
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        List<JSONObject> roles = (List) new JSONObject(new JSONObject(userDetails).get("user")).get("roles");
        if (roles != null && roles.size() > 0) {
            int flag = 0;
            for (int i = 0; i < roles.size(); i++) {
                Role role = JSONUtil.toBean(roles.get(0), Role.class);
                if ("超级管理员".equals(role.getName()) || "测试人员".equals(role.getName()) || "部门主管".equals(role.getName())) {
                    flag++;
                    break;
                }
            }
            if (flag == 0) {
                Long userId = (Long) new JSONObject(new JSONObject(userDetails).get("user")).get("id");
                criteria.setUserId(userId);
            }
        }
        manufactureService.queryManufacture(response, criteria, isPlan);
    }

    @GetMapping("/getManufactureByUser")
    @Log("查询getManufactureByUser")
    @ApiOperation("查询getManufactureByUser")
    public Object queryManufactureByUser(ManufactureQueryCriteria criteria, Pageable pageable, Boolean isPlan) {
        return getObject(criteria, pageable, isPlan);
    }

    private Object getObject(ManufactureQueryCriteria criteria, Pageable pageable, Boolean isPlan) {
        List<ManufactureDto> manufactureDtoList=manufactureService.queryManufacture(criteria, isPlan);
        MergeResult mergeResult = new MergeResult();
        mergeResult.totalElements = manufactureDtoList.size();
        mergeResult.totalPages = manufactureDtoList.size() % pageable.getPageSize() == 0 ? manufactureDtoList.size() / pageable.getPageSize() : manufactureDtoList.size() / pageable.getPageSize() + 1;
        mergeResult.currentPage = pageable.getPageNumber();
        mergeResult.size = pageable.getPageSize();
        mergeResult.content = PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), manufactureDtoList);
        return mergeResult;
    }

    @PutMapping("/edit")
    @Log("修改manufacture")
    @ApiOperation("修改manufacture")
    public ResponseEntity<Object> update(@Validated @RequestBody Manufacture resources) {
        manufactureService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/getManufactureSummary")
    @Log("查询getManufactureSummary")
    @ApiOperation("查询getManufactureSummary")
    public ResponseEntity<Object> queryManufacture(ManufactureSummaryQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(manufactureService.queryManufactureSummary(criteria, pageable), HttpStatus.OK);
    }


    @GetMapping("/getManufactureSummary/download")
    @Log("导出汇总")
    @ApiOperation("导出汇总")
    public void downloadManufactureSummary(HttpServletResponse response, ManufactureQueryCriteria criteria) {
        manufactureService.queryManufactureSummary(response, criteria);
    }

    @GetMapping("/autoManufacture")
    @Log("自动生成报工")
    @ApiOperation("自动生成报工")
    public void autoManufacture(@RequestParam String date) {
        manufactureService.createManufacture(date);
    }
}
