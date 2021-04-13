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

import me.zhengjie.base.PageRequest;
import me.zhengjie.base.PageResult;
import me.zhengjie.domain.Admspeechhead;
import me.zhengjie.domain.Product;
import me.zhengjie.service.dto.AdmspeechheadDto;
import me.zhengjie.service.dto.AdmspeechheadQueryCriteria;
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
public interface AdmspeechheadService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(AdmspeechheadQueryCriteria criteria, Pageable pageable);

  /**
   * 分页查询接口
   * 这里统一封装了分页请求和结果，避免直接引入具体框架的分页对象, 如MyBatis或JPA的分页对象
   * 从而避免因为替换ORM框架而导致服务层、控制层的分页接口也需要变动的情况，替换ORM框架也不会
   * 影响服务层以上的分页接口，起到了解耦的作用
   * @param pageRequest 自定义，统一分页查询请求
   * @return PageResult 自定义，统一分页查询结果
   */
   PageResult findAdmspeechheadByPage(AdmspeechheadQueryCriteria record, PageRequest pageRequest);



    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<AdmspeechheadDto>
    */
    List<AdmspeechheadDto> queryAll(AdmspeechheadQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param fId ID
     * @return AdmspeechheadDto
     */
    AdmspeechheadDto findById(Integer fId);

    /**
    * 创建
    * @param resources /
    * @return AdmspeechheadDto
    */
    int create(AdmspeechheadDto resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(AdmspeechheadDto resources);

    /**
    * 多选删除
    * @param ids /
    */
    void deleteAll(Integer[] ids);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<AdmspeechheadDto> all, HttpServletResponse response) throws IOException;

    void downloadProduct(List<Product> all, HttpServletResponse response) throws IOException;

    void downloadProductNum(List<Map<String, Object>> all, HttpServletResponse response) throws IOException;

    List<AdmspeechheadDto> queryByAdress(AdmspeechheadQueryCriteria criteria);
}
