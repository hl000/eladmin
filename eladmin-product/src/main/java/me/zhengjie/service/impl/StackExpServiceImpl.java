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

import cn.hutool.json.JSONObject;
import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.*;
import me.zhengjie.mapper.StackExpMapper;
import me.zhengjie.repository.DailyPlanRepository;
import me.zhengjie.repository.TypeRepository;
import me.zhengjie.service.StackExpService;
import me.zhengjie.service.dto.DailyPlanQueryCriteria;
import me.zhengjie.service.dto.ExpStackAvg;
import me.zhengjie.service.dto.ManufactureDto;
import me.zhengjie.service.dto.StackSummary;
import me.zhengjie.statistics.CommonStatistics;
import me.zhengjie.utils.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

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

    private final TypeRepository typeRepository;


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
        } catch (Exception e) {
        }

    }

    @Override
    public int insertRecord(final ExpStackInfo expStackInfo) {
        try {
            int count = new RetryT<Integer>() {
                @Override
                protected Integer doAction() throws Exception {
                    return stackExpMapper.insertRecord(getTable(), expStackInfo);
                }
            }.execute();
            return count;
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
                    return stackExpMapper.queryOne(getTable(), code, base);
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
                    return stackExpMapper.queryAll(getTable(), start, end, base);
                }
            }.execute();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<ExpStackInfo> queryExpStacks(final String start,
                                             final String end, final String base, String FBIP) {
        try {
            return new RetryT<List<ExpStackInfo>>() {
                @Override
                protected List<ExpStackInfo> doAction() throws Exception {
                    return stackExpMapper.queryExpStacks(getTable(), start, end, base, FBIP);
                }
            }.execute();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<ExpStackAvg> getExpStackAvg(final String start,
                                            String end, final String base, String FBIP) {
        List<ExpStackInfo> stackInfoList = queryExpStacks(start, end, base, FBIP);


        List<ExpStackAvg> expStackAvgList = new ArrayList<>();
        if (stackInfoList == null && stackInfoList.size() == 0) {
            return expStackAvgList;
        }
        for (ExpStackInfo expStackInfo : stackInfoList) {
            int pitchNumber = Integer.valueOf(expStackInfo.getFjieshu()) == 0 ? 1 : Integer.valueOf(expStackInfo.getFjieshu());
            if (pitchNumber > 100) {
                ExpStackAvg expStackAvg = new ExpStackAvg();
                expStackAvg.setAddress(expStackInfo.getFBase());
                expStackAvg.setDate(expStackInfo.getFDate());
                expStackAvg.setAvg500(expStackInfo.F500V / pitchNumber);
                expStackAvg.setAvg600(expStackInfo.F600V / pitchNumber);
                expStackAvg.setAvg700(expStackInfo.F700V / pitchNumber);
                expStackAvg.setAvg800(expStackInfo.F800V / pitchNumber);
                expStackAvg.setAvg900(expStackInfo.F900V / pitchNumber);
                expStackAvg.setAvg1000(expStackInfo.F1000V / pitchNumber);
                expStackAvgList.add(expStackAvg);
            }
        }

        List<ExpStackAvg> expStackAvgs = new ArrayList<>();
        Map<String, List<ExpStackAvg>> expStackAvgMap = expStackAvgList.stream().collect(Collectors.groupingBy(ExpStackAvg::getDate));
        for (Map.Entry<String, List<ExpStackAvg>> entry : expStackAvgMap.entrySet()) {
            ExpStackAvg expStackAvg = new ExpStackAvg();
            expStackAvg.setDate(entry.getKey());
            expStackAvg.setAddress(entry.getValue().get(0).getAddress());
            List<ExpStackAvg> expStackAvgsList1 = entry.getValue();

            List<ExpStackAvg> expStackAvg500 = expStackAvgsList1.stream().filter(b -> {
                return b.getAvg500() > 0;
            }).collect(Collectors.toList());
            Double a = 0d;
            if (expStackAvg500 != null && expStackAvg500.size() > 0) {
                a = expStackAvg500.stream().mapToDouble(expStackAvg1 -> {
                    return expStackAvg1.getAvg500();
                }).average().getAsDouble();
            }
            expStackAvg.setAvg500(convertDouble(a));


            List<ExpStackAvg> expStackAvg600 = expStackAvgsList1.stream().filter(b -> {
                return b.getAvg600() > 0;
            }).collect(Collectors.toList());
            a = 0d;
            if (expStackAvg600 != null && expStackAvg600.size() > 0) {
                a = expStackAvg600.stream().mapToDouble(expStackAvg1 -> {
                    return expStackAvg1.getAvg600();
                }).average().getAsDouble();
            }
            expStackAvg.setAvg600(convertDouble(a));


            List<ExpStackAvg> expStackAvg700 = expStackAvgsList1.stream().filter(b -> {
                return b.getAvg700() > 0;
            }).collect(Collectors.toList());
            a = 0d;
            if (expStackAvg700 != null && expStackAvg700.size() > 0) {
                a = expStackAvg700.stream().mapToDouble(expStackAvg1 -> {
                    return expStackAvg1.getAvg700();
                }).average().getAsDouble();
            }
            expStackAvg.setAvg700(convertDouble(a));


            List<ExpStackAvg> expStackAvg800 = expStackAvgsList1.stream().filter(b -> {
                return b.getAvg800() > 0;
            }).collect(Collectors.toList());
            a = 0d;
            if (expStackAvg800 != null && expStackAvg800.size() > 0) {
                a = expStackAvg800.stream().mapToDouble(expStackAvg1 -> {
                    return expStackAvg1.getAvg800();
                }).average().getAsDouble();
            }
            expStackAvg.setAvg800(convertDouble(a));


            List<ExpStackAvg> expStackAvg900 = expStackAvgsList1.stream().filter(b -> {
                return b.getAvg900() > 0;
            }).collect(Collectors.toList());
            a = 0d;
            if (expStackAvg900 != null && expStackAvg900.size() > 0) {
                a = expStackAvg900.stream().mapToDouble(expStackAvg1 -> {
                    return expStackAvg1.getAvg900();
                }).average().getAsDouble();
            }
            expStackAvg.setAvg900(convertDouble(a));

            List<ExpStackAvg> expStackAvg1000 = expStackAvgsList1.stream().filter(b -> {
                return b.getAvg1000() > 0;
            }).collect(Collectors.toList());
            a = 0d;
            if (expStackAvg1000 != null && expStackAvg1000.size() > 0) {
                a = expStackAvg1000.stream().mapToDouble(expStackAvg1 -> {
                    return expStackAvg1.getAvg1000();
                }).average().getAsDouble();
            }
            expStackAvg.setAvg1000(convertDouble(a));
            expStackAvgs.add(expStackAvg);
        }

        expStackAvgs = expStackAvgs.stream().sorted(Comparator.comparing(ExpStackAvg::getDate, Comparator.reverseOrder())).collect(Collectors.toList());
        return expStackAvgs;
    }

    private Double convertDouble(Double source) {
        DecimalFormat decimalFormat = new DecimalFormat("#########.####");
        return Double.valueOf(decimalFormat.format(source));
    }

    @Override
    public ExpStackInfo queryByFid(final long Fid) {
        try {
            return new RetryT<ExpStackInfo>() {
                @Override
                protected ExpStackInfo doAction() throws Exception {
                    return stackExpMapper.queryByFid(getTable(), Fid);
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
                    return stackExpMapper.updateRecord(getTable(), stackInfo);
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
                    return stackExpMapper.getStatistics(getTable(), start, end);
                }
            }.execute();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Object getType() {
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        String userAddress = (String) new JSONObject(new JSONObject(userDetails).get("user")).get("userAddress");

        List<TypeInfo> typeInfos = typeRepository.findAll();
        Map<String, List<TypeInfo>> groupByMap = typeInfos.stream().collect(Collectors.groupingBy(TypeInfo::getName));

        Map<String, List<TypeInfo>> map = new HashMap<>();
        for (Map.Entry<String, List<TypeInfo>> entry : groupByMap.entrySet()) {
            List<TypeInfo> typeInfoList = entry.getValue();
            if (userAddress != null && !"".equals(userAddress)) {
                typeInfoList = typeInfoList.stream().filter(a -> {
                    return a.getAddress().contains(userAddress);
                }).collect(Collectors.toList());
            }
            typeInfoList = typeInfoList.stream().sorted(Comparator.comparing(TypeInfo::getValue)).collect(Collectors.toList());
            map.put(entry.getKey(), typeInfoList);
        }
        return map;
    }

    public String getTable() {
        return EnvironmentUtils.isProd() ? "[CEMT]..[ADMhuohua]" : "[CEMT_TEST]..[ADMhuohua]";
    }


}
