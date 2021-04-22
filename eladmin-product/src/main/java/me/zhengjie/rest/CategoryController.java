package me.zhengjie.rest;

import cn.hutool.json.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.Category;
import me.zhengjie.domain.Manufacture;
import me.zhengjie.service.CategoryService;
import me.zhengjie.service.TechniqueInfoService;
import me.zhengjie.service.dto.CategoryQueryCriteria;
import me.zhengjie.service.dto.TechniqueInfoQueryCriteria;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author HL
 * @create 2021/4/13 14:56
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "分类管理")
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/getTechniqueInfo")
    @Log("查询techniqueInfo")
    @ApiOperation("查询techniqueInfo")
    public ResponseEntity<Object> query(CategoryQueryCriteria criteria) {
        return new ResponseEntity<>(categoryService.queryAll(criteria), HttpStatus.OK);
    }

    @PostMapping("/add")
    @Log("新增category")
    @ApiOperation("新增category")
    public ResponseEntity<Object> create(@Validated @RequestBody Category resources){
        return new ResponseEntity<>(categoryService.create(resources), HttpStatus.CREATED);
    }

}
