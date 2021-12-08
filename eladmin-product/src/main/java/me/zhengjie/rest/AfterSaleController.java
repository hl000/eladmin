package me.zhengjie.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.base.MergeResult;
import me.zhengjie.domain.Maintenance4CarItem;
import me.zhengjie.domain.VehicleInfo;
import me.zhengjie.service.AfterSaleService;
import me.zhengjie.service.dto.Maintenance4CarItemQueryCriteria;
import me.zhengjie.service.dto.VehicleInfoQueryCriteria;
import me.zhengjie.utils.PageUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author HL
 * @create 2021/9/7 23:01
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "售后维修")
@RequestMapping("/api/afterSale")
public class AfterSaleController {

    private final AfterSaleService afterSaleService;

    @GetMapping("/getMaintenance4CarItem")
    @Log("getMaintenance4CarItem")
    @ApiOperation("getMaintenance4CarItem")
    public Object getMaintenance4CarItem(Maintenance4CarItemQueryCriteria criteria, Pageable pageable) {
        List<Maintenance4CarItem> result = afterSaleService.queryAll(criteria);
        MergeResult mergeResult = new MergeResult();
        mergeResult.totalElements = result.size();
        mergeResult.totalPages = result.size() % pageable.getPageSize() == 0 ? result.size() / pageable.getPageSize() : result.size() / pageable.getPageSize() + 1;
        mergeResult.currentPage = pageable.getPageNumber();
        mergeResult.size = pageable.getPageSize();
        mergeResult.content = PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), result);
        return mergeResult;
    }

    @GetMapping("/getMaintenance4CarItem/download")
    @Log(" 下载Maintenance4CarItem")
    @ApiOperation("下载Maintenance4CarItem")
    public void downloadMaintenance4CarItem(HttpServletResponse response, Maintenance4CarItemQueryCriteria criteria) {
        List<Maintenance4CarItem> result = afterSaleService.queryAll(criteria);
        afterSaleService.downloadMaintenance4CarItem(response,result);
    }

    @GetMapping("/getVehicleInfo/download")
    @Log(" 下载VehicleInfo")
    @ApiOperation("下载VehicleInfo")
    public void downloadVehicleInfo(HttpServletResponse response,VehicleInfoQueryCriteria criteria) {
        List<VehicleInfo> result = afterSaleService.queryAllVehicleInfo(criteria);
        afterSaleService.downloadVehicleInfo(response,result);
    }


    @GetMapping("/getVehicleInfo")
    @Log("getVehicleInfo")
    @ApiOperation("getVehicleInfo")
    public Object getVehicleInfo(VehicleInfoQueryCriteria criteria, Pageable pageable) {
        List<VehicleInfo> result = afterSaleService.queryAllVehicleInfo(criteria);
        MergeResult mergeResult = new MergeResult();
        mergeResult.totalElements = result.size();
        mergeResult.totalPages = result.size() % pageable.getPageSize() == 0 ? result.size() / pageable.getPageSize() : result.size() / pageable.getPageSize() + 1;
        mergeResult.currentPage = pageable.getPageNumber();
        mergeResult.size = pageable.getPageSize();
        mergeResult.content = PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), result);
        return mergeResult;
    }
}
