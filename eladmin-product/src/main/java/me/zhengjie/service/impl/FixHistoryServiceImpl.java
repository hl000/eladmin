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
import me.zhengjie.base.ResKv;
import me.zhengjie.constant.ProductConstant;
import me.zhengjie.domain.ExpStackInfo;
import me.zhengjie.domain.FixRecordInfo;
import me.zhengjie.domain.StackReplaceInfo;
import me.zhengjie.mapper.FixHistoryMapper;
import me.zhengjie.mapper.StackExpMapper;
import me.zhengjie.service.FixHistoryService;
import me.zhengjie.service.StackExpService;
import me.zhengjie.statistics.CommonStatistics;
import me.zhengjie.utils.EnvironmentUtils;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.RetryT;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static me.zhengjie.utils.DateTraUtil.getDayStr;

/**
 * @website https://el-admin.vip
 * @description 服务实现
 * @date 2020-11-10
 **/
@Service
@RequiredArgsConstructor
public class FixHistoryServiceImpl implements FixHistoryService {
  @Resource
  private FixHistoryMapper fixHistoryMapper;


  @Override
  public List<FixRecordInfo> queryOne(final String car, final String base) {
    try {
      return new RetryT<List<FixRecordInfo>>() {
        @Override
        protected List<FixRecordInfo> doAction() throws Exception {
          return fixHistoryMapper.queryOne( getTable()  ,car, base );
        }
      }.execute();
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  @Override
  public List<FixRecordInfo> queryAll(final String start, final String end, final String base) {
    try {
      return new RetryT<List<FixRecordInfo>>() {
        @Override
        protected List<FixRecordInfo> doAction() throws Exception {
          return fixHistoryMapper.queryAll( getTable()  ,start, end, base);
        }
      }.execute();
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }


  @Override
  public void download(HttpServletResponse response, List<FixRecordInfo> result) {
    List<Map<String, Object>> list = new ArrayList<>();
    for (FixRecordInfo fixRecordInfo : result) {
      Map<String, Object> map = new LinkedHashMap<>();
      map.put("车牌号",  fixRecordInfo.getFlicensenumber());
      map.put("基地", fixRecordInfo.getFaddress());
      map.put("报修日期", fixRecordInfo.getFmaintenancedate());
      map.put("车辆类型", fixRecordInfo.getFCLEIXING());
      map.put("地点", fixRecordInfo.getFplace());
      map.put("故障码", fixRecordInfo.getFfaultcode());
      map.put("故障等级", fixRecordInfo.getFdengji());
      map.put("故障分类", fixRecordInfo.getFfailure());
      map.put("故障描述", fixRecordInfo.getFfailure1());
      map.put("解决方案", fixRecordInfo.getFsolution());
      map.put("是否更换部件", fixRecordInfo.getFsubstitutepart());
      map.put("更换部件", fixRecordInfo.getFspareparts());
      map.put("是否维修完毕", fixRecordInfo.getFrepair());
      map.put("未维修完成原因", fixRecordInfo.getFcause());
      map.put("维修耗时(h)", fixRecordInfo.getFduration());
      map.put("维修人员", fixRecordInfo.getFpersonnel());
      map.put("备注", fixRecordInfo.getFNote());
      list.add(map);
    }
    try {
      FileUtil.downloadExcel(list, response);
    }catch (Exception e) { }
  }

  @Override
  public void downloadStackReplace(HttpServletResponse response, List<StackReplaceInfo> result) {
    List<Map<String, Object>> list = new ArrayList<>();
    for (StackReplaceInfo stackReplaceInfo : result) {
      Map<String, Object> map = new LinkedHashMap<>();
      map.put("日期", stackReplaceInfo.getFDATE() );
      map.put("车牌", stackReplaceInfo.getFCHEPAI() );
      map.put("基地", stackReplaceInfo.getFjidi() );
      map.put("车辆类型", stackReplaceInfo.getFleixing() );
      map.put("故障类别", stackReplaceInfo.getFCATE() );
      map.put("故障码", stackReplaceInfo.getFdaima() );
      map.put("故障等级", stackReplaceInfo.getFjibie() );
      map.put("故障原因", stackReplaceInfo.getFYUANYIN() );
      map.put("原电堆编号", stackReplaceInfo.getFYBIANHAO() );
      map.put("原系统编号", stackReplaceInfo.getFyuanxitong());
      map.put("原电堆行驶里程数(KM)", stackReplaceInfo.getFGONGLI() );
      map.put("新电堆编号", stackReplaceInfo.getFGBIANHAO() );
      map.put("新系统编号", stackReplaceInfo.getFxinxitong() );
      map.put("维修耗时(小时)", stackReplaceInfo.getFGONGSHI() );
      map.put("维修人员", stackReplaceInfo.getFRENYUAN() );


      list.add(map);
    }
    try {
      FileUtil.downloadExcel(list, response);
    }catch (Exception e) { }
  }



  @Override
  public List<StackReplaceInfo> queryStackReplaceOne(String car, String base) {
    try {
      return new RetryT<List<StackReplaceInfo>>() {
        @Override
        protected List<StackReplaceInfo> doAction() throws Exception {
          return fixHistoryMapper.queryStackReplaceOne( getReplaceTable()  ,car, base );
        }
      }.execute();
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }



  @Override
  public List<StackReplaceInfo> queryStackReplaceAll(String start, String end, String base) {
    try {
      return new RetryT<List<StackReplaceInfo>>() {
        @Override
        protected List<StackReplaceInfo> doAction() throws Exception {
          return fixHistoryMapper.queryStackReplaceAll( getReplaceTable()  ,start, end, base);
        }
      }.execute();
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }


  @Override
  public List<StackReplaceInfo> queryByStack(String code) {
    try {
      return new RetryT<List<StackReplaceInfo>>() {
        @Override
        protected List<StackReplaceInfo> doAction() throws Exception {
          return fixHistoryMapper.queryByStack( getReplaceTable()  ,code);
        }
      }.execute();
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  @Override
  public int insertCarFixRecord(final FixRecordInfo fixRecordInfo) {
    try {
      int count = new RetryT<Integer>() {
        @Override
        protected Integer doAction() throws Exception {
          return fixHistoryMapper.insertCarFixRecord( getTable()  , fixRecordInfo);
        }
      }.execute();
      return  count;
    } catch (Exception e) {
      return -1;
    }
  }

  @Override
  public int insertStackFixRecord(final StackReplaceInfo stackReplaceInfo) {
    try {
      int count = new RetryT<Integer>() {
        @Override
        protected Integer doAction() throws Exception {
          return fixHistoryMapper.insertStackFixRecord( getReplaceTable()  , stackReplaceInfo);
        }
      }.execute();
      return  count;
    } catch (Exception e) {
      return -1;
    }
  }

  @Override
  public int updateCarFixRecord(final FixRecordInfo fixRecordInfo) {
    try {
      return new RetryT<Integer>() {
        @Override
        protected Integer doAction() throws Exception {
          return fixHistoryMapper.updateCarFixRecord( getTable()  , fixRecordInfo);
        }
      }.execute();
    } catch (Exception e) {
      return -1;
    }
  }

  @Override
  public int updateStackFixRecord(final StackReplaceInfo stackReplaceInfo) {
    try {
      return new RetryT<Integer>() {
        @Override
        protected Integer doAction() throws Exception {
          return fixHistoryMapper.updateStackFixRecord( getReplaceTable()  , stackReplaceInfo);
        }
      }.execute();
    } catch (Exception e) {
      return -1;
    }
  }

  @Override
  public FixRecordInfo queryById(final long id) {
    try {
      return new RetryT<FixRecordInfo>() {
        @Override
        protected FixRecordInfo doAction() throws Exception {
          return fixHistoryMapper.queryByFid(getTable()  , id);
        }
      }.execute();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public StackReplaceInfo queryStackReplaceById(final long id) {
    try {
      return new RetryT<StackReplaceInfo>() {
        @Override
        protected StackReplaceInfo doAction() throws Exception {
          return fixHistoryMapper.queryStackReplaceById(getReplaceTable()  , id);
        }
      }.execute();
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public List<CommonStatistics> getFixStatistics(String start, String end) {
    try {
      return new RetryT<List<CommonStatistics>>() {
        @Override
        protected List<CommonStatistics> doAction() throws Exception {
          return fixHistoryMapper.getFixStatistics( getTable()  ,start, end);
        }
      }.execute();
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  @Override
  public List<CommonStatistics> getStackReplaceStatistics(String start, String end) {
    try {
      return new RetryT<List<CommonStatistics>>() {
        @Override
        protected List<CommonStatistics> doAction() throws Exception {
          return fixHistoryMapper.getStackReplaceStatistics( getReplaceTable()  ,start, end);
        }
      }.execute();
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  public String getTable() {
    return  EnvironmentUtils.isProd() ? "[CEMT]..[ADMSHWX]" : "[CEMT_TEST]..[ADMSHWX]";
  }
  private String getReplaceTable() {
    return EnvironmentUtils.isProd() ? "[CEMT]..[ADMXTGH]" : "[CEMT_TEST]..[ADMXTGH]";
  }
}
