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
package me.zhengjie.service;

import me.zhengjie.domain.ExpStackInfo;
import me.zhengjie.domain.FixRecordInfo;
import me.zhengjie.domain.StackReplaceInfo;
import me.zhengjie.res.StackReplaceRes;
import me.zhengjie.statistics.CommonStatistics;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
* @website https://el-admin.vip
* @description 服务接口
* @author t_k_c
* @date 2020-11-28
**/
public interface FixHistoryService {

    List<FixRecordInfo> queryOne(String car, String base);

    List<FixRecordInfo> queryAll(String start, String end, String base);

    void download(HttpServletResponse response, List<FixRecordInfo> result);

    void downloadStackReplace(HttpServletResponse response, List<StackReplaceInfo> result);

    List<StackReplaceInfo> queryStackReplaceOne(String car, String base);

    List<StackReplaceInfo> queryStackReplaceAll(String start, String end, String base);

    List<StackReplaceInfo> queryByStack(String code);

    int insertCarFixRecord (FixRecordInfo fixRecordInfo);

    int insertStackFixRecord (StackReplaceInfo stackReplaceInfo);

    int updateCarFixRecord (FixRecordInfo fixRecordInfo);

    int updateStackFixRecord (StackReplaceInfo stackReplaceInfo);

    FixRecordInfo queryById (long id);

    StackReplaceInfo queryStackReplaceById (long id);

    List<CommonStatistics> getFixStatistics(String start, String end);

    List<CommonStatistics> getStackReplaceStatistics(String start, String end);

}