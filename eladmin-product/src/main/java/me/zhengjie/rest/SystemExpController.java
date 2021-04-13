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
import me.zhengjie.domain.ExpStackInfo;
import me.zhengjie.domain.ExpSystemInfo;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.service.StackExpService;
import me.zhengjie.service.SystemExpService;
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
@Api(tags = "系统测试填报管理")
@RequestMapping("/api/system/exp")
public class SystemExpController {

    private final SystemExpService systemExpService;
    public final Long GAP = 12*60*60*1000L;
    public final Long BASE = 1453217415000L;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/getRecord/download")
    public void download(HttpServletResponse response,
                         String stackNumber,
                         @RequestParam(defaultValue = "嘉善")String base,
                         @RequestParam(defaultValue = "0")long start,
                         @RequestParam(defaultValue = "0")long end ) throws IOException {
        List<ExpSystemInfo> result;
        if (StringUtils.isNotEmpty(stackNumber)) {
            result = systemExpService.queryOne(stackNumber,base);
        } else {
            if (start == 0 && end == 0) {
                end = System.currentTimeMillis();
                start = BASE;
            }
            result = systemExpService.queryAll(start, end, base);
        }
        systemExpService.download(response, result);
    }

    @GetMapping("/getRecord")
    @Log("查询系统测试记录")
    @ApiOperation("查询系统测试记录")
    public Object query(String stackNumber,
                        @RequestParam(defaultValue = "嘉善")String base,
                        @RequestParam(defaultValue = "0")long start,
                        @RequestParam(defaultValue = "0")long end,
                        Pageable pageable) {
        List<ExpSystemInfo> result;
        if (StringUtils.isNotEmpty(stackNumber)) {
            result = systemExpService.queryOne(stackNumber, base);
        } else {
            if (start == 0 && end == 0) {
                start = BASE;
                end = System.currentTimeMillis();
            }
            result = systemExpService.queryAll(start, end, base);
        }
        MergeResult mergeResult = new MergeResult();
        mergeResult.totalElements = result.size();
        mergeResult.totalPages = result.size() % pageable.getPageSize() == 0 ? result.size() / pageable.getPageSize() : result.size() / pageable.getPageSize() + 1;
        mergeResult.currentPage = pageable.getPageNumber();
        mergeResult.size = pageable.getPageSize();
        mergeResult.content = PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), result);
        return mergeResult;
    }

    @PostMapping(value = "/insert")
    @Log("新增系统测试记录")
    @ApiOperation("新增系统测试记录")
    public Object insert(@Validated @RequestBody ExpSystemInfo stackInfo) {
        stackInfo.setFSubmitDate(System.currentTimeMillis());
        UserMoreDetail userDetails = (UserMoreDetail) SecurityUtils.getCurrentUser();
        stackInfo.setFSubmitter(userDetails.getUserNickName());
        return systemExpService.insertRecord(stackInfo) == 1 ;

    }


    @PutMapping (value = "/update")
    @Log("更新系统测试记录")
    @ApiOperation("更新系统测试记录")
    public Object update(@Validated @RequestBody ExpSystemInfo stackInfo) {
        stackInfo.setFModifier(getDayStr(System.currentTimeMillis()));
        UserMoreDetail userDetails = (UserMoreDetail) SecurityUtils.getCurrentUser();
        if (stackInfo.getFID() == 0) {
            throw new BadRequestException("FID can not be zero ！");
        }
        ExpSystemInfo expSystemInfo =  systemExpService.queryByFid(stackInfo.getFID());
        if (expSystemInfo == null) {
            throw  new RuntimeException("unexpected runtime error！");
        }
        if ((!expSystemInfo.getFSubmitter().equals(userDetails.getUserNickName()))) {
            throw new BadRequestException("no permission");
        }
        return systemExpService.updateRecord(stackInfo)  == 1 ;
    }

    @GetMapping("/statistics")
    @Log("查询统计记录")
    @ApiOperation("查询统计记录")
    public Object getStatistics() {
        return null;

    }
}
