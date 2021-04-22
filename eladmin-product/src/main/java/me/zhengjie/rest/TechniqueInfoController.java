package me.zhengjie.rest;

import cn.hutool.json.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.TechniqueInfo;
import me.zhengjie.service.TechniqueInfoService;
import me.zhengjie.service.dto.TechniqueInfoQueryCriteria;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author HL
 * @date 2021-4-13
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "固定项目填报管理")
@RequestMapping("/api/techniqueInfo")
public class TechniqueInfoController {

    private final TechniqueInfoService techniqueInfoService;

    @GetMapping("/getTechniqueInfo")
    @Log("查询固定项目填报")
    @ApiOperation("查询固定项目填报")
    public ResponseEntity<Object> query(TechniqueInfoQueryCriteria criteria, Pageable pageable){
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        Long userId = (Long) new JSONObject(new JSONObject(userDetails).get("user")).get("id");
        criteria.setUserId(userId);
        return new ResponseEntity<>(techniqueInfoService.queryAll(criteria,pageable), HttpStatus.OK);
    }

    @GetMapping("/getTechniqueInfo/download")
    @Log("导出固定项目填报")
    @ApiOperation("导出固定项目填报")
    public void download(HttpServletResponse response,TechniqueInfoQueryCriteria criteria){
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        Long userId = (Long) new JSONObject(new JSONObject(userDetails).get("user")).get("id");
        criteria.setUserId(userId);
        techniqueInfoService.download(response,criteria);
    }

    @PostMapping("/add")
    @Log("新增固定项目填报")
    @ApiOperation("新增固定项目填报")
    public Object create(@Validated @RequestBody TechniqueInfo resources){
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        Long userId = (Long) new JSONObject(new JSONObject(userDetails).get("user")).get("id");
        resources.setUserId(userId);
        return techniqueInfoService.create(resources) !=null;
    }

    @PutMapping("/edit")
    @Log("修改固定项目填报")
    @ApiOperation("修改固定项目填报")
    public ResponseEntity<Object> update(@Validated @RequestBody TechniqueInfo resources){
        techniqueInfoService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/getTechniqueInfoByUser")
    @Log("查询固定项目填报ByUser")
    @ApiOperation("查询固定项目填报ByUser")
    public ResponseEntity<Object> getTechniqueInfoByUser(TechniqueInfoQueryCriteria criteria, Pageable pageable){
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        JSONObject deptObject = (JSONObject) new JSONObject(new JSONObject(userDetails).get("user")).get("dept");
        String deptId = deptObject.get("id", String.class);
        return new ResponseEntity<>(techniqueInfoService.getTechniqueInfoByUser(deptId),HttpStatus.OK);
    }
}
