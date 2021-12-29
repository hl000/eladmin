package me.zhengjie.service;

import me.zhengjie.domain.WorkStepAdjust;
import me.zhengjie.service.dto.WorkStepAdjustDto;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;

/**
 * @author HL
 * @create 2021/12/21 14:34
 */
public interface ProcedureBalanceService {

    WorkStepAdjustDto createWorkStepAdjust(WorkStepAdjust workStepAdjust);

    WorkStepAdjustDto updateWorkStepAdjust(WorkStepAdjust workStepAdjust);

    Object getProcedureBalances(String code, Pageable pageable);

    Object download(HashMap<String, Object> map);

    Object findWorkStep();

    Boolean isInventory(String cInvCode);
}
