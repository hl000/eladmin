package me.zhengjie.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.base.MergeResult;
import me.zhengjie.service.MileageService;
import me.zhengjie.service.dto.VehicleMileageStackDto;
import me.zhengjie.utils.PageUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author HL
 * @create 2022/6/26 21:12
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "车辆里程统计")
@RequestMapping("/api/mileage")
public class MileageController {

    private final MileageService mileageService;

    @GetMapping("/getVehicleMileage")
    @Log("getVehicleMileage")
    @ApiOperation("getVehicleMileage")
    public Object getVehicleMileage(@RequestParam(name = "startTs") String startTs, @RequestParam(name = "endTs") String endTs, String label, boolean updateStackFlag, Pageable pageable) {
        List<VehicleMileageStackDto> vehicleMileageStackDtos = mileageService.getVehicleMileage(startTs, endTs, label, updateStackFlag);
        MergeResult mergeResult = new MergeResult();
        mergeResult.totalElements = vehicleMileageStackDtos.size();
        mergeResult.totalPages = vehicleMileageStackDtos.size() % pageable.getPageSize() == 0 ? vehicleMileageStackDtos.size() / pageable.getPageSize() : vehicleMileageStackDtos.size() / pageable.getPageSize() + 1;
        mergeResult.currentPage = pageable.getPageNumber() + 1;
        mergeResult.size = pageable.getPageSize();
        mergeResult.content = PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), vehicleMileageStackDtos);
        return mergeResult;
    }

    @GetMapping("/getVehicleList")
    @Log("getVehicleList")
    @ApiOperation("getVehicleList")
    public Object getVehicleList() {
        return mileageService.getVehicleList();
    }

    @GetMapping("/getVehicleMileage/download")
    @Log("getVehicleMileage/download")
    @ApiOperation("getVehicleMileage/download")
    public void vehicleMileageDownload(HttpServletResponse response, @RequestParam(name = "startTs") String startTs, @RequestParam(name = "endTs") String endTs, String label, boolean updateStackFlag) {
        mileageService.vehicleMileageDownload(response,startTs, endTs, label, updateStackFlag);
    }
}
