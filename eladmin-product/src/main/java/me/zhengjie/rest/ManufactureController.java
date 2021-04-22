package me.zhengjie.rest;

import cn.hutool.json.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.Manufacture;
import me.zhengjie.domain.ManufactureSummary;
import me.zhengjie.domain.TechniqueInfo;
import me.zhengjie.service.ManufactureService;
import me.zhengjie.service.dto.BatchPlanQueryCriteria;
import me.zhengjie.service.dto.DailyPlanQueryCriteria;
import me.zhengjie.service.dto.ManufactureQueryCriteria;
import me.zhengjie.service.dto.ManufactureSummaryQueryCriteria;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

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
    public ResponseEntity<Object> create(@Validated @RequestBody Manufacture resources){
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        Long userId = (Long) new JSONObject(new JSONObject(userDetails).get("user")).get("id");
        resources.setUserId(userId);
        return new ResponseEntity<>(manufactureService.create(resources), HttpStatus.CREATED);
    }

    @GetMapping("/getManufacture")
    @Log("查询getManufacture")
    @ApiOperation("查询getManufacture")
    public ResponseEntity<Object> queryManufacture(ManufactureQueryCriteria criteria, Pageable pageable){

        UserDetails userDetails = SecurityUtils.getCurrentUser();
        Long userId = (Long) new JSONObject(new JSONObject(userDetails).get("user")).get("id");
        criteria.setUserId(userId);
        return new ResponseEntity<>(manufactureService.queryManufacture(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping("/getManufacture/download")
    @Log("导出")
    @ApiOperation("导出")
    public void downloadManufacture(HttpServletResponse response, ManufactureQueryCriteria criteria){

        UserDetails userDetails = SecurityUtils.getCurrentUser();
        Long userId = (Long) new JSONObject(new JSONObject(userDetails).get("user")).get("id");
        criteria.setUserId(userId);
        manufactureService.queryManufacture(response,criteria);
    }

    @GetMapping("/getManufactureByUser")
    @Log("查询getManufactureByUser")
    @ApiOperation("查询getManufactureByUser")
    public ResponseEntity<Object> queryManufactureByUser(ManufactureQueryCriteria criteria, Pageable pageable){
          return new ResponseEntity<>(manufactureService.queryManufacture(criteria,pageable),HttpStatus.OK);
    }

    @PutMapping("/edit")
    @Log("修改manufacture")
    @ApiOperation("修改manufacture")
    public ResponseEntity<Object> update(@Validated @RequestBody Manufacture resources){
        manufactureService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/getManufactureSummary")
    @Log("查询getManufactureSummary")
    @ApiOperation("查询getManufactureSummary")
    public ResponseEntity<Object> queryManufacture(ManufactureSummaryQueryCriteria criteria, Pageable pageable){
//        Timestamp timestamp = criteria.getUpdateTime();
//        if(timestamp!=null){
//
//        }
        return new ResponseEntity<>(manufactureService.queryManufactureSummary(criteria,pageable),HttpStatus.OK);
    }


    @GetMapping("/getManufactureSummary/download")
    @Log("导出汇总")
    @ApiOperation("导出汇总")
    public void downloadManufactureSummary(HttpServletResponse response, ManufactureQueryCriteria criteria){
        manufactureService.queryManufactureSummary(response,criteria);
    }




}
