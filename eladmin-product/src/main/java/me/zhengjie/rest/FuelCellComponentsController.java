package me.zhengjie.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.FuelCellComponents;
import me.zhengjie.service.FuelCellComponentsService;
import me.zhengjie.service.dto.FuelCellComponentsQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author HL
 * @create 2022/7/5 14:11
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "系统配件编号统计")
@RequestMapping("/api/partNumber")
public class FuelCellComponentsController {

    private final FuelCellComponentsService fuelCellComponentsService;

    @PostMapping("/add")
    @Log("新增配件")
    @ApiOperation("新增配件")
    public Object create(@Validated @RequestBody FuelCellComponents resources) {
        return fuelCellComponentsService.create(resources);
    }

    @PutMapping("/edit")
    @Log("修改配件")
    @ApiOperation("修改配件")
    public Object update(@Validated @RequestBody FuelCellComponents resources) {
        return fuelCellComponentsService.update(resources);
    }

    @GetMapping("/getFuelCellComponents")
    @Log("getFuelCellComponents")
    @ApiOperation("getFuelCellComponents")
    public Object getFuelCellComponents(FuelCellComponentsQueryCriteria fuelCellComponentsQueryCriteria, Pageable pageable) {
        return fuelCellComponentsService.queryAll(fuelCellComponentsQueryCriteria, pageable);
    }

    @GetMapping("/getFuelCellComponents/download")
    @Log("下载系统配件编号表")
    @ApiOperation("下载系统配件编号表")
    public void downloadFuelCellComponents(HttpServletResponse response, FuelCellComponentsQueryCriteria criteria) {
        fuelCellComponentsService.downloadPartNumber(response, criteria);
    }
}
