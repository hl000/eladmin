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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.base.GridRes;
import me.zhengjie.base.MergeResult;
import me.zhengjie.base.ResKv;
import me.zhengjie.base.UserMoreDetail;
import me.zhengjie.domain.FixRecordInfo;
import me.zhengjie.domain.StackReplaceInfo;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.service.FixHistoryService;
import me.zhengjie.service.SysLocalService;
import me.zhengjie.service.dto.SysLocalDto;
import me.zhengjie.statistics.CommonStatistics;
import me.zhengjie.utils.DateTraUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.utils.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
@Api(tags = "售后填报管理")
@RequestMapping("/api/fix")
public class FixHistoryController {

    private final FixHistoryService fixHistoryService;
    private final SysLocalService sysLocalService;
    public final Long GAP = 12*60*60*1000L;
    public final Long BASE = 1453217415000L;


    @GetMapping("/getRecord/download")
    @Log("导出车辆维修记录")
    @ApiOperation("导出车辆维修记录")
    public void download(HttpServletResponse response,
                        String carNumber,
                        @RequestParam(defaultValue = "嘉善") String base,
                        @RequestParam(defaultValue = "0")long start,
                        @RequestParam(defaultValue = "0")long end,
                        Pageable pageable) {
        List<FixRecordInfo> result = getFixHistory( carNumber,base,start, end);
        fixHistoryService.download(response, result);

    }


    @GetMapping("/getRecord/stack/download")
    @Log("导出电堆维修记录")
    @ApiOperation("导出电堆维修记录")
    public void downloadStack(HttpServletResponse response,
                         String stackNumber,
                         @RequestParam(defaultValue = "嘉善") String base,
                         @RequestParam(defaultValue = "0")long start,
                         @RequestParam(defaultValue = "0")long end,
                         Pageable pageable) {
        List<StackReplaceInfo> result = getStackFixHistory( stackNumber,base,start, end);
        fixHistoryService.downloadStackReplace(response, result);

    }


    @PostMapping(value = "/insert")
    @Log("新增车辆售后记录")
    @ApiOperation("新增车辆记录")
    public Object insert(@Validated @RequestBody FixRecordInfo fixRecordInfo) {
        fixRecordInfo.setFEstimatedTime(getDayStr(System.currentTimeMillis()));
        UserMoreDetail userDetails = (UserMoreDetail) SecurityUtils.getCurrentUser();
        fixRecordInfo.setFPname(userDetails.getUserNickName());
        return fixHistoryService.insertCarFixRecord(fixRecordInfo) == 1 ;

    }


    @PostMapping(value = "/insert/stack")
    @Log("新增电堆售后记录")
    @ApiOperation("新增电堆维修记录")
    public Object insertStack(@Validated @RequestBody StackReplaceInfo stackReplaceInfo) {
        stackReplaceInfo.setFEstimatedTime(getDayStr(System.currentTimeMillis()));
        UserMoreDetail userDetails = (UserMoreDetail) SecurityUtils.getCurrentUser();
        stackReplaceInfo.setFPname(userDetails.getUserNickName());
        return fixHistoryService.insertStackFixRecord(stackReplaceInfo) == 1 ;

    }



    @GetMapping("/getRecord")
    @Log("查询车辆维修记录")
    @ApiOperation("查询车辆维修记录")
    public Object query(
                        String carNumber,
                        @RequestParam(defaultValue = "嘉善") String base,
                        @RequestParam(defaultValue = "0")long start,
                        @RequestParam(defaultValue = "0")long end,
                        Pageable pageable) {
        List<FixRecordInfo> result = getFixHistory(carNumber, base, start, end);
        MergeResult mergeResult = new MergeResult();
        mergeResult.totalElements = result.size();
        mergeResult.totalPages = result.size() % pageable.getPageSize() == 0 ? result.size() / pageable.getPageSize() : result.size() / pageable.getPageSize() + 1;
        mergeResult.currentPage = pageable.getPageNumber();
        mergeResult.size = pageable.getPageSize();
        mergeResult.content = PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), result);
        return mergeResult;
    }

    private List<FixRecordInfo> getFixHistory(String carNumber, String base, long start, long end) {
        List<FixRecordInfo> result = new ArrayList<>();
        UserMoreDetail userDetails = (UserMoreDetail) SecurityUtils.getCurrentUser();
        Set<String> local = sysLocalService.getLocalByuser(userDetails.getUserId());
        if (!local.contains(base)) {
            return result;
        }
        if (StringUtils.isNotEmpty(carNumber) ) {
            result = fixHistoryService.queryOne(carNumber, base);
        } else {
            if (start == 0 && end == 0) {
                start = BASE;
                end = System.currentTimeMillis();
            }
            String startDate = getDayStr(start).split(" ")[0];
            String endDate = getDayStr(end).split(" ")[0];
            result = fixHistoryService.queryAll(startDate, endDate, base);
        }
        return result;
    }

    @GetMapping("/getRecord/stack")
    @Log("查询电堆维修记录")
    @ApiOperation("查询电堆维修记录")
    public Object queryStack(
                        String carNumber,
                        @RequestParam(defaultValue = "嘉善") String base,
                        @RequestParam(defaultValue = "0")long start,
                        @RequestParam(defaultValue = "0")long end,
                        Pageable pageable) {
        List<StackReplaceInfo> result = getStackFixHistory(carNumber, base, start, end);
        MergeResult mergeResult = new MergeResult();
        mergeResult.totalElements = result.size();
        mergeResult.totalPages = result.size() % pageable.getPageSize() == 0 ? result.size() / pageable.getPageSize() : result.size() / pageable.getPageSize() + 1;
        mergeResult.currentPage = pageable.getPageNumber();
        mergeResult.size = pageable.getPageSize();
        mergeResult.content = PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), result);
        return mergeResult;
    }

    private List<StackReplaceInfo> getStackFixHistory(String carNumber, String base, long start, long end) {
        List<StackReplaceInfo> result = new ArrayList<>();
        UserMoreDetail userDetails = (UserMoreDetail) SecurityUtils.getCurrentUser();
        Set<String> local = sysLocalService.getLocalByuser(userDetails.getUserId());
        if (!local.contains(base)) {
            return result;
        }
        if (StringUtils.isNotEmpty(carNumber) ) {
            result = fixHistoryService.queryStackReplaceOne(carNumber, base);
        } else {
            if (start == 0 && end == 0) {
                start = BASE;
                end = System.currentTimeMillis();
            }
            String startDate = getDayStr(start).split(" ")[0];
            String endDate = getDayStr(end).split(" ")[0];
            result = fixHistoryService.queryStackReplaceAll(startDate, endDate, base);
        }
        return result;
    }

    @PutMapping (value = "/update")
    @Log("更新车辆维修记录")
    @ApiOperation("更新车辆维修记录")
    public Object update(@Validated @RequestBody FixRecordInfo fixRecordInfo) {
        UserMoreDetail userDetails = (UserMoreDetail) SecurityUtils.getCurrentUser();
        if (fixRecordInfo.getNumber() == 0) {
            throw new BadRequestException("Number can not be zero ！");
        }
        FixRecordInfo recordInfo =  fixHistoryService.queryById(fixRecordInfo.getNumber());
        if (recordInfo == null) {
            throw  new RuntimeException("unexpected runtime error！");
        }
        if (!recordInfo.getFPname().equals(userDetails.getUserNickName())) {
            throw new BadRequestException("no permission");
        }
        return fixHistoryService.updateCarFixRecord(fixRecordInfo)  == 1 ;
    }


    @PutMapping (value = "/update/stack")
    @Log("更新车辆维修记录")
    @ApiOperation("更新车辆维修记录")
    public Object updateStack(@Validated @RequestBody StackReplaceInfo stackReplaceInfo) {
        UserMoreDetail userDetails = (UserMoreDetail) SecurityUtils.getCurrentUser();
        if (stackReplaceInfo.getFID() == 0) {
            throw new BadRequestException("FID can not be zero ！");
        }
        StackReplaceInfo recordInfo =  fixHistoryService.queryStackReplaceById(stackReplaceInfo.getFID());
        if (recordInfo == null) {
            throw  new RuntimeException("unexpected runtime error！");
        }
        if (!recordInfo.getFPname().equals(userDetails.getUserNickName())) {
            throw new BadRequestException("no permission");
        }
        return fixHistoryService.updateStackFixRecord(stackReplaceInfo)  == 1 ;
    }

    @GetMapping("/statistics")
    @Log("查询维修记录统计记录")
    @ApiOperation("查询维修统计记录")
    public Object getFixStatistics(@RequestParam(defaultValue = "car") String type) {
        List<ResKv> ret = new ArrayList<>();

        String start = "2015-01-01";
        String end = "2050-01-11";
        List<CommonStatistics> whole= type.equals("car") ?
                fixHistoryService.getFixStatistics(start, end) : fixHistoryService.getStackReplaceStatistics(start, end);
        List<ResKv> whole_all_list =  whole.stream().sorted(Comparator.comparingLong(c -> c.totalCnt * (-1))).collect(Collectors.toList()).stream().map(c->  new ResKv(c.base, c.totalCnt)).collect(Collectors.toList()) ;
        List<ResKv> whole_dict_list = whole.stream().sorted(Comparator.comparingLong(c -> c.distCnt * (-1))).collect(Collectors.toList()).stream().map(c->  new ResKv(c.base, c.distCnt)).collect(Collectors.toList()) ;
        ret.add(new ResKv("whole_all", new GridRes(whole_all_list.stream().map(c -> c.label).collect(Collectors.toList())
                ,   whole_all_list.stream().map(c -> c.value).collect(Collectors.toList())  )));
        ret.add(new ResKv("whole_dict", new GridRes(whole_dict_list.stream().map(c -> c.label).collect(Collectors.toList())
                ,   whole_dict_list.stream().map(c -> c.value).collect(Collectors.toList())  )));

        //获取当月
        long current = System.currentTimeMillis();
        String date = formatDate(current);
        String[] pair = date.split("-");
        String cMonStart = pair[0] + "-" + pair[1]+ "-01";
        String cMonEnd = pair[0] + "-" + pair[1]+ "-31";
        List<CommonStatistics> month= fixHistoryService.getFixStatistics(cMonStart, cMonEnd);
        List<ResKv> month_all_list = month.stream().sorted(Comparator.comparingLong(c -> c.totalCnt * (-1))).collect(Collectors.toList()).stream().map(c->  new ResKv(c.base, c.totalCnt)).collect(Collectors.toList()) ;
        List<ResKv> month_dict_list = month.stream().sorted(Comparator.comparingLong(c -> c.distCnt * (-1))).collect(Collectors.toList()).stream().map(c->  new ResKv(c.base, c.distCnt)).collect(Collectors.toList()) ;
        ret.add(new ResKv("month_all", new GridRes(month_all_list.stream().map(c -> c.label).collect(Collectors.toList())
                ,   month_all_list.stream().map(c -> c.value).collect(Collectors.toList())  )));
        ret.add(new ResKv("month_dict", new GridRes(month_dict_list.stream().map(c -> c.label).collect(Collectors.toList())
                ,   month_dict_list.stream().map(c -> c.value).collect(Collectors.toList())  )));

        //获取本周
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(new Date(current));
        int dayOfweek = calendar.get(Calendar.WEEK_OF_YEAR);
        String weekDate = formatDate(current - (dayOfweek-1) * DateTraUtil.gap);
        String weekStart = weekDate.split(" ")[0];
        String weekEnd = date.split(" ")[0];
        List<CommonStatistics> week= fixHistoryService.getFixStatistics(weekStart, weekEnd);
        List<ResKv> week_all_list = week.stream().sorted(Comparator.comparingLong(c -> c.totalCnt * (-1))).collect(Collectors.toList()).stream().map(c->  new ResKv(c.base, c.totalCnt)).collect(Collectors.toList()) ;
        List<ResKv> week_dict_list = week.stream().sorted(Comparator.comparingLong(c -> c.distCnt * (-1))).collect(Collectors.toList()).stream().map(c->  new ResKv(c.base, c.distCnt)).collect(Collectors.toList()) ;
        ret.add(new ResKv("week_all", new GridRes(week_all_list.stream().map(c -> c.label).collect(Collectors.toList())
                ,   week_all_list.stream().map(c -> c.value).collect(Collectors.toList())  )));
        ret.add(new ResKv("week_dict", new GridRes(week_dict_list.stream().map(c -> c.label).collect(Collectors.toList())
                ,   week_dict_list.stream().map(c -> c.value).collect(Collectors.toList())  )));

        //获取昨日
        String yesDate = formatDate(current - DateTraUtil.gap);
        String yesStart = yesDate.split(" ")[0];
        String yesEnd = yesDate.split(" ")[0];
        List<CommonStatistics> yesterDay = fixHistoryService.getFixStatistics(yesStart, yesEnd);
        List<ResKv> yest_all_list = yesterDay.stream().sorted(Comparator.comparingLong(c -> c.totalCnt * (-1))).collect(Collectors.toList()).stream().map(c->  new ResKv(c.base, c.totalCnt)).collect(Collectors.toList()) ;
        List<ResKv> yes_dict_list = yesterDay.stream().sorted(Comparator.comparingLong(c -> c.distCnt * (-1))).collect(Collectors.toList()).stream().map(c->  new ResKv(c.base, c.distCnt)).collect(Collectors.toList()) ;
        ret.add(new ResKv("yest_all", new GridRes(yest_all_list.stream().map(c -> c.label).collect(Collectors.toList())
                ,   yest_all_list.stream().map(c -> c.value).collect(Collectors.toList())  )));
        ret.add(new ResKv("yest_dict", new GridRes(yes_dict_list.stream().map(c -> c.label).collect(Collectors.toList())
                ,   yes_dict_list.stream().map(c -> c.value).collect(Collectors.toList())  )));
        return ret;


    }


}
