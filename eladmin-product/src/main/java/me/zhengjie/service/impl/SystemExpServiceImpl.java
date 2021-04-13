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
import me.zhengjie.domain.ExpStackInfo;
import me.zhengjie.domain.ExpSystemInfo;
import me.zhengjie.mapper.StackExpMapper;
import me.zhengjie.mapper.SystemExpMapper;
import me.zhengjie.service.StackExpService;
import me.zhengjie.service.SystemExpService;
import me.zhengjie.statistics.CommonStatistics;
import me.zhengjie.utils.EnvironmentUtils;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.RetryT;
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
public class SystemExpServiceImpl implements SystemExpService {


  @Resource
  private SystemExpMapper systemExpMapper;


  @Override
  public void download(HttpServletResponse response, List<ExpSystemInfo> result) {
    List<Map<String, Object>> list = new ArrayList<>();
    for (ExpSystemInfo expStackDto : result) {
      Map<String, Object> map = new LinkedHashMap<>();
      map.put("提交日期", getDayStr(expStackDto.getFSubmitDate()));
      map.put("基地", expStackDto.getFBase());
      map.put("提交者", expStackDto.getFSubmitter());
      map.put("电堆编号", expStackDto.getFNumber());
      map.put("节数", expStackDto.getFJieshu());
      map.put("测试日期", expStackDto.getFDate());
      map.put("测试人员", expStackDto.getFEmp());
      map.put("100电密电压", expStackDto.getF100V());
      map.put("200电密电压", expStackDto.getF200V());
      map.put("300电密电压", expStackDto.getF300V());
      map.put("400电密电压", expStackDto.getF400V());
      map.put("500电密电压", expStackDto.getF500V());
      map.put("600电密电压", expStackDto.getF600V());
      map.put("700电密电压", expStackDto.getF700V());
      map.put("800电密电压", expStackDto.getF800V());
      map.put("900电密电压", expStackDto.getF900V());
      map.put("1000电密电压", expStackDto.getF1000V());
      map.put("最低电压节数", expStackDto.getFZuidiID());
      map.put("最低电压值", expStackDto.getFZuidiV());
      map.put("用途", expStackDto.getFusage());
      map.put("备注", expStackDto.getFNote());
      map.put("修改记录", expStackDto.getFModifier());
      list.add(map);
    }
    try {
      FileUtil.downloadExcel(list, response);
    }catch (Exception e) { }

  }

  @Override
  public int insertRecord(ExpSystemInfo expStackInfo) {
    try {
      int count = new RetryT<Integer>() {
        @Override
        protected Integer doAction() throws Exception {
          return systemExpMapper.insertRecord( getTable()  , expStackInfo);
        }
      }.execute();
      return  count;
    } catch (Exception e) {
      return -1;
    }
  }

  @Override
  public List<ExpSystemInfo> queryOne(String code, String base) {
    try {
      return new RetryT<List<ExpSystemInfo>>() {
        @Override
        protected List<ExpSystemInfo> doAction() throws Exception {
          return systemExpMapper.queryOne( getTable()  ,code, base);
        }
      }.execute();
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  @Override
  public List<ExpSystemInfo> queryAll(long start, long end, String base) {
    try {
      return new RetryT<List<ExpSystemInfo>>() {
        @Override
        protected List<ExpSystemInfo> doAction() throws Exception {
          return systemExpMapper.queryAll( getTable()  ,start, end, base);
        }
      }.execute();
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  @Override
  public ExpSystemInfo queryByFid(long Fid) {
    try {
      return new RetryT<ExpSystemInfo>() {
        @Override
        protected ExpSystemInfo doAction() throws Exception {
          return systemExpMapper.queryByFid( getTable()  , Fid);
        }
      }.execute();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public int updateRecord(ExpSystemInfo stackInfo) {
    try {
      return new RetryT<Integer>() {
        @Override
        protected Integer doAction() throws Exception {
          return systemExpMapper.updateRecord( getTable()  ,stackInfo);
        }
      }.execute();
    } catch (Exception e) {
      return -1;
    }
  }

  @Override
  public List<CommonStatistics> getStatistics(String start, String end) {
    try {
      return new RetryT<List<CommonStatistics>>() {
        @Override
        protected List<CommonStatistics> doAction() throws Exception {
          return systemExpMapper.getStatistics( getTable()  ,start, end);
        }
      }.execute();
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  public String getTable() {
    return EnvironmentUtils.isProd() ? "[CEMT]..[ADMSystem]" : "[CEMT_TEST]..[ADMSystem]";
  }

}
