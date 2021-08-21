/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.rest;

import com.sun.org.apache.bcel.internal.generic.BIPUSH;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.base.GridRes;
import me.zhengjie.base.MergeResult;
import me.zhengjie.base.ResKv;
import me.zhengjie.base.UserMoreDetail;
import me.zhengjie.domain.ExpStackInfo;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.service.StackExpService;
import me.zhengjie.service.dto.ExpStackAvg;
import me.zhengjie.statistics.CommonStatistics;
import me.zhengjie.utils.DateTraUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.utils.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static me.zhengjie.utils.DateTraUtil.formatDate;
import static me.zhengjie.utils.DateTraUtil.getDayStr;

/**
 * @author t_k_c
 * @website https://el-admin.vip
 * @date 2020-11-28
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "活化填报管理")
@RequestMapping("/api/stack/exp")
public class StackExpController {

    private final StackExpService stackExpService;
    public final Long GAP = 12 * 60 * 60 * 1000L;
    public final Long BASE = 1453217415000L;

    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/getRecord/download")
    public void download(HttpServletResponse response,
                         String stackNumber,
                         @RequestParam(defaultValue = "嘉善") String base,
                         @RequestParam(defaultValue = "0") long start,
                         @RequestParam(defaultValue = "0") long end) throws IOException {
        List<ExpStackInfo> result;
        if (StringUtils.isNotEmpty(stackNumber)) {
            result = stackExpService.queryOne(stackNumber, base);
        } else {
            if (start == 0 && end == 0) {
                end = System.currentTimeMillis();
                start = BASE;
            }
            result = stackExpService.queryAll(start, end, base);
        }
        stackExpService.download(response, result);
    }

    @GetMapping("/getExpSummary")
    @Log("查询实验记录汇总")
    @ApiOperation("查询实验记录汇总")
    public Object getExpSummary(@RequestParam(defaultValue = "嘉善") String base,
                                String start,
                                String end,
                                @RequestParam(defaultValue = "C04-2") String FBIP) {
        if (start == null) {
            start = "1970-01-01";
        }
        if (end == null) {
            end = dateFormat.format(new Date());
        }
        List<ExpStackAvg> result = stackExpService.getExpStackAvg(start, end, base, FBIP);

        return result;
    }

    @GetMapping("/getType")
    @Log("查询类型下拉框")
    @ApiOperation("查询类型下拉框")
    public Object getType() {
        return stackExpService.getType();
    }

    @GetMapping("/getRecord")
    @Log("查询活化记录")
    @ApiOperation("查询活化记录")
    public Object query(String stackNumber,
                        @RequestParam(defaultValue = "嘉善") String base,
                        @RequestParam(defaultValue = "0") long start,
                        @RequestParam(defaultValue = "0") long end,
                        Pageable pageable) {
        List<ExpStackInfo> result;
        if (StringUtils.isNotEmpty(stackNumber)) {
            result = stackExpService.queryOne(stackNumber, base);
        } else {
            if (start == 0 && end == 0) {
                start = BASE;
                end = System.currentTimeMillis();
            }
            result = stackExpService.queryAll(start, end, base);
        }
        MergeResult mergeResult = new MergeResult();
        mergeResult.totalElements = (result == null || result.size() == 0) ? 0 : result.size();
        mergeResult.totalPages = (result == null || result.size() == 0) ? 0 : (result.size() % pageable.getPageSize() == 0 ? result.size() / pageable.getPageSize() : result.size() / pageable.getPageSize() + 1);
        mergeResult.currentPage = pageable.getPageNumber();
        mergeResult.size = pageable.getPageSize();
        mergeResult.content = PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), result);
        return mergeResult;
    }

    @PostMapping(value = "/insert")
    @Log("新增活化记录")
    @ApiOperation("新增活化记录")
    public Object insert(@Validated @RequestBody ExpStackInfo stackInfo) {
        stackInfo.setFSubmitDate(System.currentTimeMillis());
        UserMoreDetail userDetails = (UserMoreDetail) SecurityUtils.getCurrentUser();
        stackInfo.setFSubmitter(userDetails.getUserNickName());
        /*List<ExpStackInfo> expStackInfoList =  stackExpService.queryOne(stackInfo.getFNumber());
        if ( expStackInfoList.size() > 0 ) {
            ExpStackInfo expStackInfo = expStackInfoList.get(0);
            if (expStackInfo.getFSubmitDate() > 0L
                    && (System.currentTimeMillis() - expStackInfo.getFSubmitDate() <= GAP)) {
                return "12小时内已提交过该电堆的活化记录，无法重复提交！请转至修改页面或者联系管理员！";
            }

        }*/
        return stackExpService.insertRecord(stackInfo) == 1;

    }


    @PutMapping(value = "/update")
    @Log("更新活化记录")
    @ApiOperation("更新活化记录")
    public Object update(@Validated @RequestBody ExpStackInfo stackInfo) {
        stackInfo.setFModifier(getDayStr(System.currentTimeMillis()));
        UserMoreDetail userDetails = (UserMoreDetail) SecurityUtils.getCurrentUser();
        if (stackInfo.getFID() == 0) {
            throw new BadRequestException("FID can not be zero ！");
        }
        ExpStackInfo expStackInfo = stackExpService.queryByFid(stackInfo.getFID());
        if (expStackInfo == null) {
            throw new RuntimeException("unexpected runtime error！");
        }
        if ((!expStackInfo.getFSubmitter().equals(userDetails.getUserNickName()))
                && (expStackInfo.getFEmp() == null || !expStackInfo.getFEmp().contains(userDetails.getUserNickName()))
                && (expStackInfo.getFZuzhaungEmp() == null || !expStackInfo.getFZuzhaungEmp().contains(userDetails.getUserNickName()))
        ) {
            throw new BadRequestException("no permission");
        }
        return stackExpService.updateRecord(stackInfo) == 1;
    }

    @GetMapping("/statistics")
    @Log("查询统计记录")
    @ApiOperation("查询统计记录")
    public Object getStatistics() {
        String start = "2015-01-01";
        String end = "2050-01-11";
        List<CommonStatistics> whole = stackExpService.getStatistics(start, end);
        List<CommonStatistics> whole_all = whole.stream().sorted(Comparator.comparingLong(c -> c.totalCnt * (-1))).collect(Collectors.toList());
        List<CommonStatistics> whole_dict = whole.stream().sorted(Comparator.comparingLong(c -> c.distCnt * (-1))).collect(Collectors.toList());

        long current = System.currentTimeMillis();
        String date = formatDate(current);
        //获取当月
        String[] pair = date.split("-");
        String cMonStart = pair[0] + "-" + pair[1] + "-01";
        String cMonEnd = pair[0] + "-" + pair[1] + "-31";
        List<CommonStatistics> month = stackExpService.getStatistics(cMonStart, cMonEnd);
        List<CommonStatistics> month_all = month.stream().sorted(Comparator.comparingLong(c -> c.totalCnt * (-1))).collect(Collectors.toList());
        List<CommonStatistics> month_dict = month.stream().sorted(Comparator.comparingLong(c -> c.distCnt * (-1))).collect(Collectors.toList());

        //获取本周
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(new Date(current));
        int dayOfweek = calendar.get(Calendar.WEEK_OF_YEAR);
        String weekDate = formatDate(current - (dayOfweek - 1) * DateTraUtil.gap);
        String weekStart = weekDate.split(" ")[0];
        String weekEnd = date.split(" ")[0];
        List<CommonStatistics> week = stackExpService.getStatistics(weekStart, weekEnd);
        List<CommonStatistics> week_all = week.stream().sorted(Comparator.comparingLong(c -> c.totalCnt * (-1))).collect(Collectors.toList());
        List<CommonStatistics> week_dict = week.stream().sorted(Comparator.comparingLong(c -> c.distCnt * (-1))).collect(Collectors.toList());

        //获取昨日
        String yesDate = formatDate(current - DateTraUtil.gap);
        String yesStart = yesDate.split(" ")[0];
        String yesEnd = yesDate.split(" ")[0];
        List<CommonStatistics> yesterDay = stackExpService.getStatistics(yesStart, yesEnd);
        List<CommonStatistics> yesterDay_all = yesterDay.stream().sorted(Comparator.comparingLong(c -> c.totalCnt * (-1))).collect(Collectors.toList());
        List<CommonStatistics> yesterDay_dict = yesterDay.stream().sorted(Comparator.comparingLong(c -> c.distCnt * (-1))).collect(Collectors.toList());


        List<ResKv> whole_all_list = whole_all.stream().map(c -> new ResKv(c.base, c.totalCnt)).collect(Collectors.toList());
        List<ResKv> whole_dict_list = whole_dict.stream().map(c -> new ResKv(c.base, c.distCnt)).collect(Collectors.toList());
        List<ResKv> month_all_list = month_all.stream().map(c -> new ResKv(c.base, c.totalCnt)).collect(Collectors.toList());
        List<ResKv> month_dict_list = month_dict.stream().map(c -> new ResKv(c.base, c.distCnt)).collect(Collectors.toList());
        List<ResKv> week_all_list = week_all.stream().map(c -> new ResKv(c.base, c.totalCnt)).collect(Collectors.toList());
        List<ResKv> week_dict_list = week_dict.stream().map(c -> new ResKv(c.base, c.distCnt)).collect(Collectors.toList());
        List<ResKv> yest_all_list = yesterDay_all.stream().map(c -> new ResKv(c.base, c.totalCnt)).collect(Collectors.toList());
        List<ResKv> yes_dict_list = yesterDay_dict.stream().map(c -> new ResKv(c.base, c.distCnt)).collect(Collectors.toList());

        List<ResKv> ret = new ArrayList<>();
        ret.add(new ResKv("whole_all", new GridRes(whole_all_list.stream().map(c -> c.label).collect(Collectors.toList()), whole_all_list.stream().map(c -> c.value).collect(Collectors.toList()))));
        ret.add(new ResKv("whole_dict", new GridRes(whole_dict_list.stream().map(c -> c.label).collect(Collectors.toList()), whole_dict_list.stream().map(c -> c.value).collect(Collectors.toList()))));
        ret.add(new ResKv("month_all", new GridRes(month_all_list.stream().map(c -> c.label).collect(Collectors.toList()), month_all_list.stream().map(c -> c.value).collect(Collectors.toList()))));
        ret.add(new ResKv("month_dict", new GridRes(month_dict_list.stream().map(c -> c.label).collect(Collectors.toList()), month_dict_list.stream().map(c -> c.value).collect(Collectors.toList()))));
        ret.add(new ResKv("week_all", new GridRes(week_all_list.stream().map(c -> c.label).collect(Collectors.toList()), week_all_list.stream().map(c -> c.value).collect(Collectors.toList()))));
        ret.add(new ResKv("week_dict", new GridRes(week_dict_list.stream().map(c -> c.label).collect(Collectors.toList()), week_dict_list.stream().map(c -> c.value).collect(Collectors.toList()))));
        ret.add(new ResKv("yest_all", new GridRes(yest_all_list.stream().map(c -> c.label).collect(Collectors.toList()), yest_all_list.stream().map(c -> c.value).collect(Collectors.toList()))));
        ret.add(new ResKv("yest_dict", new GridRes(yes_dict_list.stream().map(c -> c.label).collect(Collectors.toList()), yes_dict_list.stream().map(c -> c.value).collect(Collectors.toList()))));
        return ret;


    }
}
