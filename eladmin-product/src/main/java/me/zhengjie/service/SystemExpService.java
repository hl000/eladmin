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

import me.zhengjie.domain.ExpSystemInfo;
import me.zhengjie.statistics.CommonStatistics;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
* @website https://el-admin.vip
* @description 服务接口
* @author t_k_c
* @date 2020-11-28
**/
public interface SystemExpService {


    void download(HttpServletResponse response, List<ExpSystemInfo> result);

    int insertRecord(ExpSystemInfo expSystemInfo);

    List<ExpSystemInfo> queryOne(String code, String base);

    List<ExpSystemInfo> queryAll(long start, long end, String base);

    ExpSystemInfo queryByFid(long Fid);

    int updateRecord(ExpSystemInfo expSystemInfo);

    public List<CommonStatistics> getStatistics(final String start, final String end);
}