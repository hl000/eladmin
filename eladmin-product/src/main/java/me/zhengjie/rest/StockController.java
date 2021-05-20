package me.zhengjie.rest;

import cn.hutool.json.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.service.StockService;
import me.zhengjie.service.TechniqueInfoService;
import me.zhengjie.service.dto.StockQueryCriteria;
import me.zhengjie.service.dto.TechniqueInfoQueryCriteria;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author HL
 * @create 2021/5/19 17:45
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "制造计划管理")
@RequestMapping("/api/stock")
public class StockController {
    private final StockService stockService;

    @GetMapping("/getStock")
    @Log("查看库存信息")
    @ApiOperation("查看库存信息")
    public ResponseEntity<Object> query(StockQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(stockService.queryAll(criteria,pageable), HttpStatus.OK);
    }

    @GetMapping("/getStock/download")
    @Log("导出库存信息")
    @ApiOperation("导出库存信息")
    public void download(HttpServletResponse response, StockQueryCriteria criteria){
        stockService.download(response,criteria);
    }


}