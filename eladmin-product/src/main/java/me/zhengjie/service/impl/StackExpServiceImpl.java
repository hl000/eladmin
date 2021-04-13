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
package me.zhengjie.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.*;
import me.zhengjie.mapper.StackExpMapper;
import me.zhengjie.service.StackExpService;
import me.zhengjie.statistics.CommonStatistics;
import me.zhengjie.utils.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static me.zhengjie.utils.DateTraUtil.getDayStr;

/**
 * @website https://el-admin.vip
 * @description 服务实现
 * @date 2020-11-10
 **/
@Service
@RequiredArgsConstructor
public class StackExpServiceImpl implements StackExpService {
  @Resource
  private StackExpMapper stackExpMapper;

  @Override
  public void download(HttpServletResponse response, List<ExpStackInfo> result) {
    List<Map<String, Object>> list = new ArrayList<>();
    for (ExpStackInfo expStackDto : result) {
      Map<String, Object> map = new LinkedHashMap<>();
      map.put("提交日期", getDayStr(expStackDto.getFSubmitDate()));
      map.put("基地", expStackDto.getFBase());
      map.put("提交者", expStackDto.getFSubmitter());
      map.put("电堆编号", expStackDto.getFNumber());
      map.put("节数", expStackDto.getFjieshu());
      map.put("BIP编号", expStackDto.getFBIP());
      map.put("MEA型号", expStackDto.getFMEA());
      map.put("MEA日期", expStackDto.getFMEADate());
      map.put("碳纸类型", expStackDto.getFTanzhi());
      map.put("活化日期", expStackDto.getFDate());
      map.put("活化次数", expStackDto.getFCishu());
      map.put("活化人员", expStackDto.getFEmp());
      map.put("是否加压", expStackDto.getFJiaya());
      map.put("组装力", expStackDto.getFZuzhaung());
      map.put("组装人员", expStackDto.getFZuzhaungEmp());
      map.put("500电密总电压", expStackDto.getF500V());
      map.put("500电密总功率", expStackDto.getF500KW());
      map.put("600电密总电压", expStackDto.getF600V());
      map.put("600电密总功率", expStackDto.getF600KW());
      map.put("700电密总电压", expStackDto.getF700V());
      map.put("700电密总功率", expStackDto.getF700KW());
      map.put("800电密总电压", expStackDto.getF800V());
      map.put("800电密总功率", expStackDto.getF800KW());
      map.put("900电密总电压", expStackDto.getF900V());
      map.put("900电密总功率", expStackDto.getF900KW());
      map.put("1000电密总电压", expStackDto.getF1000V());
      map.put("1000电密总功率", expStackDto.getF1000KW());
      map.put("平均电压", expStackDto.getFPingjunV());
      map.put("最低节数 最低电压1", expStackDto.getFZuidi1());
      map.put("最低节数 最低电压2", expStackDto.getFZuidi2());
      map.put("最低节数 最低电压3", expStackDto.getFZuidi3());
      map.put("备注", expStackDto.getFNote());
      map.put("修改记录", expStackDto.getFModifier());
      list.add(map);
    }
    try {
      FileUtil.downloadExcel(list, response);
    }catch (Exception e) { }

  }

  @Override
  public int insertRecord(final ExpStackInfo expStackInfo) {
    try {
      int count = new RetryT<Integer>() {
        @Override
        protected Integer doAction() throws Exception {
          return stackExpMapper.insertRecord( getTable()  , expStackInfo);
        }
      }.execute();
      return  count;
    } catch (Exception e) {
      return -1;
    }
  }


  @Override
  public List<ExpStackInfo> queryOne(final String code, final String base) {
    try {
      return new RetryT<List<ExpStackInfo>>() {
        @Override
        protected List<ExpStackInfo> doAction() throws Exception {
          return stackExpMapper.queryOne( getTable()  ,code, base);
        }
      }.execute();
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  @Override
  public List<ExpStackInfo> queryAll(final long start,
                              final long end, final String base) {
    try {
      return new RetryT<List<ExpStackInfo>>() {
        @Override
        protected List<ExpStackInfo> doAction() throws Exception {
          return stackExpMapper.queryAll( getTable()  ,start, end, base);
        }
      }.execute();
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }
  @Override
  public ExpStackInfo queryByFid (final long Fid){
    try {
      return new RetryT<ExpStackInfo>() {
        @Override
        protected ExpStackInfo doAction() throws Exception {
          return stackExpMapper.queryByFid( getTable()  , Fid);
        }
      }.execute();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public int updateRecord(final ExpStackInfo stackInfo) {
    try {
      return new RetryT<Integer>() {
        @Override
        protected Integer doAction() throws Exception {
          return stackExpMapper.updateRecord( getTable()  ,stackInfo);
        }
      }.execute();
    } catch (Exception e) {
      return -1;
    }
  }

  @Override
  public List<CommonStatistics> getStatistics(final String start, final String end) {
    try {
      return new RetryT<List<CommonStatistics>>() {
        @Override
        protected List<CommonStatistics> doAction() throws Exception {
          return stackExpMapper.getStatistics( getTable()  ,start, end);
        }
      }.execute();
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }


  public String getTable() {
    return EnvironmentUtils.isProd() ? "[CEMT]..[ADMhuohua]" : "[CEMT_TEST]..[ADMhuohua]";
  }



}
