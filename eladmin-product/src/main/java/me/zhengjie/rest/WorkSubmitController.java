package me.zhengjie.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.WorkCompletQtySubmit;
import me.zhengjie.service.WorkSubmitService;
import me.zhengjie.service.dto.WorkCompletQtySubmitQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author HL
 * @create 2021/12/29 15:13
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "生产管理")
@RequestMapping("/api/workSubmit")
public class WorkSubmitController {

    private final WorkSubmitService workSubmitService;

    /**
     * 报工
     */
    @PostMapping("/createWorkSubmit")
    @Log("新增报工")
    @ApiOperation("新增报工")
    public ResponseEntity<Object> createWorkSubmit(@Validated @RequestBody WorkCompletQtySubmit resources) {
        return new ResponseEntity<>(workSubmitService.createWorkSubmit(resources), HttpStatus.OK);
    }

    /**
     * 报工
     */
    @GetMapping("/getAllWorkSubmit")
    @Log("查询报工")
    @ApiOperation("查询报工")
    public Object getAllWorkSubmit(WorkCompletQtySubmitQueryCriteria criteria, Pageable pageable) {
        return workSubmitService.getAllWorkSubmit(criteria, pageable);
    }

    /**
     * 报工
     */
    @PostMapping("/updateWorkSubmit")
    @Log("更新报工")
    @ApiOperation("更新报工")
    public ResponseEntity<Object> updateWorkSubmit(@Validated @RequestBody WorkCompletQtySubmit resources) {
        return new ResponseEntity<>(workSubmitService.updateWorkSubmit(resources), HttpStatus.OK);
    }

    @DeleteMapping("/deleteWorkSubmit")
    @Log("删除工作计划")
    @ApiOperation("删除工作计划")
    public ResponseEntity<Object> deleteWorkSubmit(@RequestBody Integer id) {
        workSubmitService.deleteWorkSubmit(id);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /**
     * 看板
     */
    @GetMapping("/getWorkListBoard")
    @Log("看板")
    @ApiOperation("看板")
    public Object getWorkListBoard(String workShop, String fArcName, String startDate, String endDate, String workOrder, Pageable pageable) {
        return workSubmitService.getWorkList(workShop, fArcName, startDate, endDate, workOrder, pageable);
    }

    /**
     * 报表界面
     */
    @GetMapping("/getWorkList")
    @Log("报表界面")
    @ApiOperation("报表界面")
    public Object getWorkList(String workShop, String fArcName, String startDate, String endDate, String workOrder, Pageable pageable) {
        return workSubmitService.getWorkList(workShop, fArcName, startDate, endDate, workOrder, pageable);
    }

    /**
     * 报表界面
     */
    @GetMapping("/getWorkList/download")
    @Log("报表下载")
    @ApiOperation("报表下载")
    public void getWorkListDownload(String workShop, String fArcName, String startDate, String endDate, String workOrder, HttpServletResponse response) {
        workSubmitService.getWorkListDownload(workShop, fArcName, startDate, endDate, workOrder, response);
    }

    /**
     * 工序下拉框
     */
    @GetMapping("/queryAllProcedure")
    @Log("工序下拉框")
    @ApiOperation("工序下拉框")
    public ResponseEntity<Object> queryAllProcedure() {
        return new ResponseEntity<>(workSubmitService.queryAllProcedure(), HttpStatus.OK);
    }

    /**
     * 时间下拉框
     */
    @GetMapping("/queryAllTimeList")
    @Log("查询时间列表")
    @ApiOperation("查询时间列表")
    public ResponseEntity<Object> queryAllTimeList() {
        return new ResponseEntity<>(workSubmitService.queryAllTimeList(), HttpStatus.OK);
    }

    /**
     * 工单号和物料关系
     */
    @GetMapping("/getInventoryByWorkOrder")
    @Log("根据工单号查物料信息")
    @ApiOperation("根据工单号查物料信息")
    public Object getInventoryByWorkOrder(String workOrder) {
        return workSubmitService.getInventoryByWorkOrder(workOrder);
    }


    /**
     * 查询此工序是否有报工
     */
    @GetMapping("/getWorkSubmitByArcId")
    @Log("根据工序查物料信息")
    @ApiOperation("根据工序查物料信息")
    public Object getWorkSubmitByArcId(Integer arcId) {
        return workSubmitService.getWorkSubmitByArcId(arcId);
    }
}
