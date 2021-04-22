package me.zhengjie.rest;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.BatchPlan;
import me.zhengjie.domain.DailyPlan;
import me.zhengjie.repository.DailyPlanRepository;
import me.zhengjie.service.PlanService;
import me.zhengjie.service.dto.BatchPlanQueryCriteria;
import me.zhengjie.service.dto.DailyPlanQueryCriteria;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author HL
 * @create 2021/4/14 14:53
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "制造计划管理")
@RequestMapping("/api/plan")
public class PlanController {

    private final PlanService planService;

    private final DailyPlanRepository dailyPlanRepository;

    @PostMapping("/batchPlanAdd")
    @Log("新增batchPlan")
    @ApiOperation("新增batchPlan")
    public Object createBatchPlan(@Validated @RequestBody BatchPlan resources){
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        Long userId = (Long) new JSONObject(new JSONObject(userDetails).get("user")).get("id");
        resources.setUserId(userId);

        return planService.createBatchPlan(resources)!=null;
//        return new ResponseEntity<>(planService.createBatchPlan(resources), HttpStatus.CREATED);
    }

    @GetMapping("/findBatchPlan")
    @Log("查询findBatchPlan")
    @ApiOperation("查询findBatchPlan")
    public ResponseEntity<Object> findBatchPlan(BatchPlanQueryCriteria criteria){
        return new ResponseEntity<>(planService.findBatchPlan(criteria),HttpStatus.OK);
    }

    @GetMapping("/getBatchPlan/download")
    @Log("导出BatchPlan")
    @ApiOperation("导出BatchPlan")
    public void download(HttpServletResponse response,BatchPlanQueryCriteria criteria){
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        Long userId = (Long) new JSONObject(new JSONObject(userDetails).get("user")).get("id");
        criteria.setUserId(userId);
        planService.download(response,criteria);
    }

    @GetMapping("/getDailyPlan/download")
    @Log("导出DailyPlan")
    @ApiOperation("导出DailyPlan")
    public void downloadDailyPlan(HttpServletResponse response,DailyPlanQueryCriteria criteria){
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        Long userId = (Long) new JSONObject(new JSONObject(userDetails).get("user")).get("id");
        criteria.setUserId(userId);
        planService.downloadDailyPlan(response,criteria);
    }

    @GetMapping("/getBatchPlan")
    @Log("查询getBatchPlan")
    @ApiOperation("查询getBatchPlan")
    public ResponseEntity<Object> queryBatchPlan(BatchPlanQueryCriteria criteria, Pageable pageable){
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        Long userId = (Long) new JSONObject(new JSONObject(userDetails).get("user")).get("id");
        criteria.setUserId(userId);
        return new ResponseEntity<>(planService.queryBatchPlan(criteria,pageable),HttpStatus.OK);
    }

    @PutMapping("/batchPlanEdit")
    @Log("修改batchPlan")
    @ApiOperation("修改batchPlan")
    public ResponseEntity<Object> updateBatch(@Validated @RequestBody BatchPlan resources){
        planService.updateBatchPlan(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/dailyPlanAdd")
    @Log("新增dailyPlan")
    @ApiOperation("新增dailyPlan")
    public Object createDailyPlan(@Validated @RequestBody DailyPlan resources){
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        Long userId = (Long) new JSONObject(new JSONObject(userDetails).get("user")).get("id");
        resources.setUserId(userId);

        return planService.createDailyPlan(resources)!=null;
    }

    @PutMapping("/dailyPlanEdit")
    @Log("修改dailyPlan")
    @ApiOperation("修改dailyPlan")
    public ResponseEntity<Object> updateDailyPlan(@Validated @RequestBody DailyPlan resources){
        planService.updateDailyPlan(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/getDailyPlan")
    @Log("查询getDailyPlan")
    @ApiOperation("查询getDailyPlan")
    public ResponseEntity<Object> queryDailyPlan(DailyPlanQueryCriteria criteria, Pageable pageable){
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        Long userId = (Long) new JSONObject(new JSONObject(userDetails).get("user")).get("id");
        criteria.setUserId(userId);
        return new ResponseEntity<>(planService.queryDailyPlan(criteria,pageable),HttpStatus.OK);
    }


    @GetMapping("/findDailyPlan")
    @Log("查询findDailyPlan")
    @ApiOperation("查询findDailyPlan")
    public ResponseEntity<Object> findDailyPlan(DailyPlanQueryCriteria criteria){
        return new ResponseEntity<>(planService.findDailyPlan(criteria),HttpStatus.OK);
    }

    @GetMapping("/getDailyPlanByUser")
    @Log("getDailyPlanByUser")
    @ApiOperation("getDailyPlanUser")
    public ResponseEntity<Object> getDailyPlanaByUser(){

        return new ResponseEntity<>(planService.getDailyPlanSelector(),HttpStatus.OK);
    }

    @GetMapping("/getBatchPlanByUser")
    @Log("getBatchPlanByUser")
    @ApiOperation("getBatchPlanByUser")
    public ResponseEntity<Object> getBatchPlanaByUser(){
        return new ResponseEntity<>(planService.getBatchPlanSelector(),HttpStatus.OK);
    }
}
