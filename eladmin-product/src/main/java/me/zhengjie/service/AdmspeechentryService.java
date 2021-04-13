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

import me.zhengjie.domain.Admspeechentry;
import me.zhengjie.service.dto.AdmspeechentryDto;
import me.zhengjie.service.dto.AdmspeechentryQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://el-admin.vip
* @description 服务接口
* @author t_k_c
* @date 2020-11-10
**/
public interface AdmspeechentryService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(AdmspeechentryQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<AdmspeechentryDto>
    */
    List<AdmspeechentryDto> queryAll(AdmspeechentryQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param fid ID
     * @return AdmspeechentryDto
     */
    AdmspeechentryDto findById(Integer fid);

    /**
    * 创建
    * @param resources /
    * @return AdmspeechentryDto
    */
    int create(AdmspeechentryDto resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(AdmspeechentryDto resources);

    /**
    * 删除
    * @param fHeadid /
    */
    void deleteAll(Integer fHeadid);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<AdmspeechentryDto> all, HttpServletResponse response) throws IOException;
}
