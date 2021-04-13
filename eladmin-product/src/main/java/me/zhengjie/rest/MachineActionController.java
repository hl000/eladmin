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
import me.zhengjie.domain.MachineOriginAction;
import me.zhengjie.domain.StackReplaceInfo;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.service.FixHistoryService;
import me.zhengjie.service.MachineService;
import me.zhengjie.service.SysLocalService;
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
@Api(tags = "机器动作")
@RequestMapping("/api/machine")
public class MachineActionController {

    private final SysLocalService sysLocalService;
    private final MachineService machineService;
    public final Long GAP = 12 * 60 * 60 * 1000L;
    public final Long BASE = 1453217415000L;


    @GetMapping("/realtime")
    @Log("查询机器实时动作计数器记录")
    @ApiOperation("查询机器实时动作计数器记录")
    public Object getActionRealTime(
            @RequestParam(defaultValue = "0") long time,
            @RequestParam(defaultValue = "嘉善") String base,
            @RequestParam(defaultValue = "1") int sampleRation) {
        List<ResKv> ret = new ArrayList<>();
        if (time == 0L) {
            time = System.currentTimeMillis();
        }
        long dayId = DateTraUtil.getDayId(time);
        List<MachineOriginAction> result = machineService.getMachineOrigin(dayId, base);
        Map<String, List<MachineOriginAction>> groupByMap = result.stream().collect(Collectors.groupingBy(MachineOriginAction::getDeviceName));

        for (Map.Entry<String, List<MachineOriginAction>> entry : groupByMap.entrySet()) {
            GridRes gridRes = new GridRes();
            gridRes.aData = entry.getValue().stream().filter(c -> c.number % sampleRation == 0).map(c -> c.addTime).collect(Collectors.toList());
            gridRes.bData = entry.getValue().stream().filter(c -> c.number % sampleRation == 0).map(c -> c.count).collect(Collectors.toList());
            ret.add(new ResKv(entry.getKey(), gridRes));
        }
        return ret;
    }


}
