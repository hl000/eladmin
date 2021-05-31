package me.zhengjie.rest;

import cn.hutool.json.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.BatchPlan;
import me.zhengjie.domain.ProductParameter;
import me.zhengjie.service.PlanService;
import me.zhengjie.service.ProductParameterService;
import me.zhengjie.service.dto.BatchPlanQueryCriteria;
import me.zhengjie.service.dto.ProductParameterQueryCriteria;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author HL
 * @create 2021/4/23 10:52
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "产品参数信息管理")
@RequestMapping("/api/productParameter")
public class ProductParameterController {

    private final ProductParameterService productParameterService;

    private final PlanService planService;

    @PostMapping("/productParameterAdd")
    @Log("新增产品参数信息")
    @ApiOperation("新增产品参数信息")
    public Object createProductParameter(@Validated @RequestBody ProductParameter resources) {
        return productParameterService.create(resources) != null;
    }

    @PostMapping("/productParameterEdit")
    @Log("更新产品参数信息")
    @ApiOperation("更新产品参数信息")
    public ResponseEntity<Object> updateProductParameter(@Validated @RequestBody ProductParameter resources) {
        productParameterService.updateProductParameter(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/getProductParameter")
    @Log("查询产品参数信息")
    @ApiOperation("查询产品参数信息")
    public ResponseEntity<Object> getProductParameter(ProductParameterQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(productParameterService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @GetMapping("/productNameSelector")
    @Log("获取产品名称下拉框")
    @ApiOperation("查询产品名称")
    public Map<String,Object> getProductName(ProductParameterQueryCriteria criteria) {
        Map<String,Object> map = new HashMap<>();
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        String userAddress = (String) new JSONObject(new JSONObject(userDetails).get("user")).get("userAddress");
        map.put("userAddress",userAddress);

        List<ProductParameter> productParameterList = productParameterService.queryAll(criteria);
//        Map <String,List < ProductParameter >> collect = productParameterList.stream().collect(Collectors.groupingBy(ProductParameter::getProductName));
//        return collet;

        Map<String, String> stringStringMap = new HashMap<>();
        productParameterList.stream().forEach(productParameter -> {
            stringStringMap.put(productParameter.getProductName(), productParameter.getProductName());
        });
        List<String> productNames = stringStringMap.entrySet().stream().map(e -> e.getKey()).collect(Collectors.toList());
        map.put("productNames",productNames);
        return map;
    }

    @GetMapping("/productParameterSelector")
    @Log("获取产品下拉框")
    @ApiOperation("查询产品参数信息")
    public ResponseEntity<Object> getProductParameter(ProductParameterQueryCriteria criteria) {
        return new ResponseEntity<>(productParameterService.queryAll(criteria), HttpStatus.OK);
    }

    @GetMapping("/getRemainBatchQuantity")
    @Log("获取剩余日计划最大值")
    @ApiOperation("获取剩余日计划最大值")
    public ResponseEntity<Object> getRemainBatchQuantity(BatchPlanQueryCriteria criteria) {
        return new ResponseEntity<>(planService.getRemainBatchQuantity(criteria), HttpStatus.OK);
    }

    @GetMapping("/getManufactureName")
    @Log("获取计划外报工名称")
    @ApiOperation("获取计划外报工名称")
    public ResponseEntity<Object> getManufactureName() {
        return new ResponseEntity<>(productParameterService.getManufactureName(), HttpStatus.OK);
    }
}
