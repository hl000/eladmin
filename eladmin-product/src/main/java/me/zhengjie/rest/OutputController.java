package me.zhengjie.rest;

import cn.hutool.json.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.base.MergeResult;
import me.zhengjie.domain.Manufacture;
import me.zhengjie.service.ManufactureService;
import me.zhengjie.service.OutputService;
import me.zhengjie.service.dto.ManufactureDtoQueryCriteria;
import me.zhengjie.service.dto.ManufactureQueryCriteria;
import me.zhengjie.service.dto.OutputDto;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.SecurityUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author HL
 * @create 2021/5/26 15:39
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "产量管理")
@RequestMapping("/api/output")
public class OutputController {

    private final OutputService outputService;

    final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping("/getOutput")
    @Log("查询产量")
    @ApiOperation("查询getOutput")
    public Object get(ManufactureDtoQueryCriteria criteria, Pageable pageable, Timestamp startDate, Timestamp endDate) throws ParseException {
        Timestamp start;
        Timestamp end;
        if (startDate != null && endDate != null) {
            start = startDate;
            end = endDate;
        } else if (startDate == null && endDate == null) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, -7);
            Date d = c.getTime();
            String day = format.format(d);
            start = new Timestamp(format.parse(day).getTime());
            end = new Timestamp(new Date().getTime());
        } else if (startDate == null && endDate != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(endDate);
            c.add(Calendar.YEAR, -5);
            Date d = c.getTime();

            start = new Timestamp(d.getTime());
            end = endDate;
        } else {
            start = startDate;
            end = new Timestamp(new Date().getTime());
        }

        log.info("start:" + start + "end:" + end);
        List<OutputDto> outputDtoList = outputService.queryManufacture(criteria, start, end);
        if (outputDtoList == null || outputDtoList.size() == 0)
            return null;
        MergeResult mergeResult = new MergeResult();
        mergeResult.totalElements = outputDtoList.size();
        mergeResult.totalPages = outputDtoList.size() % pageable.getPageSize() == 0 ? outputDtoList.size() / pageable.getPageSize() : outputDtoList.size() / pageable.getPageSize() + 1;
        mergeResult.currentPage = pageable.getPageNumber();
        mergeResult.size = pageable.getPageSize();
        mergeResult.content = PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), outputDtoList);
        return mergeResult;
    }

    @GetMapping("/getOutput/download")
    @Log("产量导出")
    @ApiOperation("产量导出")
    public void get(ManufactureDtoQueryCriteria criteria, HttpServletResponse response, Timestamp startDate, Timestamp endDate) throws ParseException {
        Timestamp start;
        Timestamp end;
        if (startDate != null && endDate != null) {
            start = startDate;
            end = endDate;
        } else if (startDate == null && endDate == null) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, -7);
            Date d = c.getTime();
            String day = format.format(d);
            start = new Timestamp(format.parse(day).getTime());
            end = new Timestamp(new Date().getTime());
        } else if (startDate == null && endDate != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(endDate);
            c.add(Calendar.YEAR, -5);
            Date d = c.getTime();

            start = new Timestamp(d.getTime());
            end = endDate;
        } else {
            start = startDate;
            end = new Timestamp(new Date().getTime());
        }

        List<OutputDto> outputDtoList = outputService.queryManufacture(criteria, start, end);
        outputService.download(outputDtoList, start, end, response);
    }
}
