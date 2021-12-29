package me.zhengjie.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.WorkStepAdjust;
import me.zhengjie.service.ProcedureBalanceService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author HL
 * @create 2021/12/21 14:31
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "工序結存管理")
@RequestMapping("/api/procedureBalance")
public class ProcedureBalanceController {

    private final ProcedureBalanceService procedureBalanceService;

    @GetMapping("/getProcedureBalance")
    @Log("查詢工序結存")
    @ApiOperation("查詢工序結存")
    public Object getProcedureBalance(String name, Pageable pageable) {
        return procedureBalanceService.getProcedureBalances(name, pageable);
    }

    @PostMapping("/createWorkStepAdjust")
    @Log("新增工序結存")
    @ApiOperation("新增工序結存")
    public Object createWorkStepAdjust(WorkStepAdjust workStepAdjust) {
        return procedureBalanceService.createWorkStepAdjust(workStepAdjust);
    }

    @PostMapping("/updateWorkStepAdjust")
    @Log("更新工序結存")
    @ApiOperation("更新工序結存")
    public Object updateWorkStepAdjust(WorkStepAdjust workStepAdjust) {
        return procedureBalanceService.updateWorkStepAdjust(workStepAdjust);
    }

    public Object download(HashMap<String, Object> map) {
        return procedureBalanceService.download(map);
    }

    @GetMapping("/findWorkStep")
    @Log("查詢工序")
    @ApiOperation("查詢工序")
    public Object findWorkStep() {
        return procedureBalanceService.findWorkStep();
    }
}
