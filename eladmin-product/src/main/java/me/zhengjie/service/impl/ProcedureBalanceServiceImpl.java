package me.zhengjie.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.base.MergeResult;
import me.zhengjie.domain.WorkStepAdjust;
import me.zhengjie.mapper.InventoryMapper;
import me.zhengjie.mapper.ProcedureBalanceMapper;
import me.zhengjie.mapper.WorkStepAdjustMapper;
import me.zhengjie.mapper.WorkStepMapper;
import me.zhengjie.service.ProcedureBalanceService;
import me.zhengjie.service.dto.InventoryDto;
import me.zhengjie.service.dto.ProcedureBalanceDto;
import me.zhengjie.service.dto.WorkStepAdjustDto;
import me.zhengjie.utils.EnvironmentUtils;
import me.zhengjie.utils.PageUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author HL
 * @create 2021/12/21 14:34
 */
@Service
@RequiredArgsConstructor
public class ProcedureBalanceServiceImpl implements ProcedureBalanceService {

    @Resource
    private final ProcedureBalanceMapper procedureBalanceMapper;

    @Resource
    private final WorkStepMapper workStepMapper;

    @Resource
    private final InventoryMapper inventoryMapper;

    @Resource
    private final WorkStepAdjustMapper workStepAdjustMapper;


    @Override
    public WorkStepAdjustDto createWorkStepAdjust(WorkStepAdjust workStepAdjust) {
        List<WorkStepAdjust> workStepAdjustList = workStepAdjustMapper.getWorkStepAdjust(getWorkStepAdjustTable(), workStepAdjust.getArcCode(), workStepAdjust.getInvCode());
        if (workStepAdjustList != null && workStepAdjustList.size() > 0) {
            //數據已存在
            return new WorkStepAdjustDto("DATA_EXIST");
        }

        if (isInventory(workStepAdjust.getInvCode())) {
            workStepAdjustMapper.insertWorkStepAdjust(getWorkStepAdjustTable(), workStepAdjust);
            return new WorkStepAdjustDto("SUCCESS");
        }
        return new WorkStepAdjustDto("INV_CODE_NOT_FOUND");
    }

    @Override
    public WorkStepAdjustDto updateWorkStepAdjust(WorkStepAdjust workStepAdjust) {
        List<WorkStepAdjust> workStepAdjustList = workStepAdjustMapper.getWorkStepAdjust(getWorkStepAdjustTable(), workStepAdjust.getArcCode(), workStepAdjust.getInvCode());
        if (workStepAdjustList == null || workStepAdjustList.size() == 0) {
            return new WorkStepAdjustDto("DATA_NOT_FOUND");
        }
        WorkStepAdjust workStepAdjust1 = workStepAdjustList.get(0);
        workStepAdjust1.setAdjustQty(workStepAdjust1.getAdjustQty() + workStepAdjust.getAdjustQty());
        workStepAdjustMapper.updateWorkStepAdjust(getWorkStepAdjustTable(), workStepAdjust1);
        return new WorkStepAdjustDto("SUCCESS");
    }

    @Override
    public Object getProcedureBalances(String arcName, Pageable pageable) {
        if (arcName == null) {
            arcName = "";
        }
        List<ProcedureBalanceDto> procedureBalanceDtos = procedureBalanceMapper.getProcedureBalance(arcName);
        List<ProcedureBalanceDto> procedureBalanceDtoList = new ArrayList<>();
        if (procedureBalanceDtos.size() < pageable.getPageNumber() * pageable.getPageSize()) {
            procedureBalanceDtoList = procedureBalanceDtos;
        } else if (procedureBalanceDtos.size() > pageable.getPageNumber() * pageable.getPageSize() && procedureBalanceDtos.size() < (pageable.getPageNumber() + 1) * pageable.getPageSize()) {
            procedureBalanceDtoList = procedureBalanceDtos.subList(pageable.getPageNumber() * pageable.getPageSize(), procedureBalanceDtos.size());
        } else if (procedureBalanceDtos.size() > pageable.getPageNumber() * pageable.getPageSize() && procedureBalanceDtos.size() >= (pageable.getPageNumber() + 1) * pageable.getPageSize()) {
            procedureBalanceDtoList = procedureBalanceDtos.subList(pageable.getPageNumber() * pageable.getPageSize(), (pageable.getPageNumber() + 1) * pageable.getPageSize());
        }

        MergeResult mergeResult = new MergeResult();
        mergeResult.totalElements = procedureBalanceDtos.size();
        mergeResult.totalPages = procedureBalanceDtos.size() % pageable.getPageSize() == 0 ? procedureBalanceDtos.size() / pageable.getPageSize() : procedureBalanceDtos.size() / pageable.getPageSize() + 1;
        mergeResult.currentPage = pageable.getPageNumber();
        mergeResult.size = pageable.getPageSize();
        mergeResult.content = PageUtil.toPage(0, pageable.getPageSize(), procedureBalanceDtoList);
        return mergeResult;
    }

    @Override
    public Object download(HashMap<String, Object> map) {
        return null;
    }

    @Override
    public Object findWorkStep() {
        return workStepMapper.queryWorkStep(getWorkStepTable());
    }

    @Override
    public Boolean isInventory(String cInvCode) {
        List<InventoryDto> inventoryDtos = inventoryMapper.getInventoryByCode(getInventoryTable(), cInvCode);
        if (inventoryDtos != null && inventoryDtos.size() > 0) {
            return true;
        }
        return false;
    }

    public String getInventoryTable() {
        return EnvironmentUtils.isProd() ? "[UFDATA_002_2020]..[Inventory]" : "[UFDATA_002_2020]..[Inventory]";
    }

    private String getWorkStepTable() {
        return EnvironmentUtils.isProd() ? "[r9-data-002]..[__mpro_work_step]" : "[r9-data-002]..[__mpro_work_step]";
    }

    private String getWorkStepAdjustTable() {
        return EnvironmentUtils.isProd() ? "[r9-data-002]..[mwork_step_adjust_quantity]" : "[r9-data-002]..[mwork_step_adjust_quantity]";
    }
}
