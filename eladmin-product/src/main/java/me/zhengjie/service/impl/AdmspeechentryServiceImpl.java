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

import me.zhengjie.domain.Admspeechentry;
import me.zhengjie.mapper.AdmspeechentryBatisMapper;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.repository.AdmspeechentryRepository;
import me.zhengjie.service.AdmspeechentryService;
import me.zhengjie.service.dto.AdmspeechentryDto;
import me.zhengjie.service.dto.AdmspeechentryQueryCriteria;
import me.zhengjie.service.mapstruct.AdmspeechentryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @website https://el-admin.vip
* @description 服务实现
* @author t_k_c
* @date 2020-11-10
**/
@Service
@RequiredArgsConstructor
public class AdmspeechentryServiceImpl implements AdmspeechentryService {

    @Resource
    private AdmspeechentryBatisMapper admspeechentryBatisMapper;

    private final AdmspeechentryRepository admspeechentryRepository;

    private final AdmspeechentryMapper admspeechentryMapper;

    @Override
    public Map<String,Object> queryAll(AdmspeechentryQueryCriteria criteria, Pageable pageable){
        Page<Admspeechentry> page = admspeechentryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(admspeechentryMapper::toDto));
    }

    @Override
    public List<AdmspeechentryDto> queryAll(AdmspeechentryQueryCriteria criteria){
        return admspeechentryBatisMapper.getAdmspeechentry(criteria);
    }

    @Override
    @Transactional
    public AdmspeechentryDto findById(Integer fid) {
        Admspeechentry admspeechentry = admspeechentryRepository.findById(fid).orElseGet(Admspeechentry::new);
        ValidationUtil.isNull(admspeechentry.getFid(),"Admspeechentry","fid",fid);
        return admspeechentryMapper.toDto(admspeechentry);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int create(AdmspeechentryDto resources) {
        return admspeechentryBatisMapper.insertSelective(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AdmspeechentryDto resources) {
      admspeechentryBatisMapper.insertSelective(resources);
    }

    @Override
    public void deleteAll(Integer fHeadid) {
      admspeechentryBatisMapper.deleteByHeadId(fHeadid);
    }

    @Override
    public void download(List<AdmspeechentryDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AdmspeechentryDto admspeechentry : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("产品编号", admspeechentry.getFNumber());
            map.put("产品名称", admspeechentry.getFName());
            map.put("当日产量", admspeechentry.getFOutputquantity());
            map.put("不良品", admspeechentry.getFRejectsquantity());
            map.put("结存", admspeechentry.getFBalancequantity());
            map.put("备注", admspeechentry.getFNote());
            map.put("关联表id", admspeechentry.getFHeadid());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
