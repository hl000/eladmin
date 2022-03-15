package me.zhengjie.service;

import me.zhengjie.service.dto.*;

import java.util.List;

/**
 * @author HL
 * @create 2021/10/19 9:05
 */
public interface ReportFormService {
    List<ReportFormDto> getReportForm(ReportFormQueryCriteria criteria);

    List<ReportFormGroupDto> getAllReportForm(String startDate,String endDate, String address, String invName);

    List<ProcessCompletedGroupDto> getProcessCompleted(String startDate, String endDate, String address, String invProcess);

    Object getFactoryProcess();

    List<SemiFinishedStockDto> getRepertory(String cInvUnit, String adrName);

    Object getPitchList();
}
