package me.zhengjie.mapper;

import me.zhengjie.annotation.DatabaseType;
import me.zhengjie.annotation.DynamicDao;
import me.zhengjie.service.dto.ProcessCompletedDto;
import me.zhengjie.service.dto.ReportFormWorkDto;
import me.zhengjie.service.dto.SemiFinishedStockDto;

import java.util.List;

/**
 * @author HL
 * @create 2022/2/14 17:40
 */
@DynamicDao(type = DatabaseType.second)
public interface ReportFormMapper {
    List<ReportFormWorkDto> queryReportFormWorkDto(String startDate, String endDate, String address, String invName);

    List<ProcessCompletedDto> queryProcessCompleted(String startDate, String endDate, String address, String invProcess);

    List<SemiFinishedStockDto> getRepertory(String cInvUnit, String adrName);

    List<String> getPitchList();
}
