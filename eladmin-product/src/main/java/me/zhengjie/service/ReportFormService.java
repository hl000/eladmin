package me.zhengjie.service;

import me.zhengjie.service.dto.ReportFormDto;
import me.zhengjie.service.dto.ReportFormQueryCriteria;

import java.util.List;

/**
 * @author HL
 * @create 2021/10/19 9:05
 */
public interface ReportFormService {
    List<ReportFormDto> getReportForm(ReportFormQueryCriteria criteria);
}
