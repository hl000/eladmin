package me.zhengjie.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.base.MergeResult;
import me.zhengjie.service.ReportFormService;
import me.zhengjie.service.dto.ReportFormDto;
import me.zhengjie.service.dto.ReportFormQueryCriteria;
import me.zhengjie.utils.PageUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author HL
 * @create 2021/10/19 8:47
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "报工报表")
@RequestMapping("/api/reportFrom")
public class ReportFormController {

    private final ReportFormService reportFormService;

    @GetMapping("/getReportFrom")
    @Log("reportFrom")
    @ApiOperation("reportFrom")
    public Object getReportForm(ReportFormQueryCriteria criteria, Pageable pageable) {
        List<ReportFormDto> reportFormDtos = reportFormService.getReportForm(criteria);
        MergeResult mergeResult = new MergeResult();
        mergeResult.totalElements = reportFormDtos.size();
        mergeResult.totalPages = reportFormDtos.size() % pageable.getPageSize() == 0 ? reportFormDtos.size() / pageable.getPageSize() : reportFormDtos.size() / pageable.getPageSize() + 1;
        mergeResult.currentPage = pageable.getPageNumber() + 1;
        mergeResult.size = pageable.getPageSize();
        mergeResult.content = PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), reportFormDtos);
        return mergeResult;
    }
}
