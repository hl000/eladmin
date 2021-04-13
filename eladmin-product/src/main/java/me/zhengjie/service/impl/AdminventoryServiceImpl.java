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

import me.zhengjie.domain.Adminventory;
import me.zhengjie.mapper.AdminventoryBatisMapper;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.repository.AdminventoryRepository;
import me.zhengjie.service.AdminventoryService;
import me.zhengjie.service.dto.AdminventoryDto;
import me.zhengjie.service.dto.AdminventoryQueryCriteria;
import me.zhengjie.service.mapstruct.AdminventoryMapper;
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
* @date 2020-11-28
**/
@Service
@RequiredArgsConstructor
public class AdminventoryServiceImpl implements AdminventoryService {

    @Resource
    private AdminventoryBatisMapper adminventoryBatisMapper;

    private final AdminventoryRepository adminventoryRepository;
    private final AdminventoryMapper adminventoryMapper;

    @Override
    public Map<String,Object> queryAll(AdminventoryQueryCriteria criteria, Pageable pageable){
        Page<Adminventory> page = adminventoryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(adminventoryMapper::toDto));
    }

    @Override
    public List<AdminventoryDto> queryAll(AdminventoryQueryCriteria criteria){
        return adminventoryBatisMapper.getAdminventory(criteria);
    }

    @Override
    @Transactional
    public AdminventoryDto findById(Integer fid) {
        Adminventory adminventory = adminventoryRepository.findById(fid).orElseGet(Adminventory::new);
        ValidationUtil.isNull(adminventory.getFid(),"Adminventory","fid",fid);
        return adminventoryMapper.toDto(adminventory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AdminventoryDto create(Adminventory resources) {
        return adminventoryMapper.toDto(adminventoryRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Adminventory resources) {
        Adminventory adminventory = adminventoryRepository.findById(resources.getFid()).orElseGet(Adminventory::new);
        ValidationUtil.isNull( adminventory.getFid(),"Adminventory","id",resources.getFid());
        adminventory.copy(resources);
        adminventoryRepository.save(adminventory);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer fid : ids) {
            adminventoryRepository.deleteById(fid);
        }
    }

    @Override
    public void download(List<AdminventoryDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AdminventoryDto adminventory : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("存货编码", adminventory.getFnumber());
            map.put("存货名称", adminventory.getFname());
            map.put("规格型号", adminventory.getFstd());
            map.put("单位", adminventory.getFunit());
            map.put("归属部门", adminventory.getFdept());
            map.put("原始存货编码", adminventory.getFoldNumber());
            map.put("状态（0可用，1不可用）", adminventory.getFstate());
            map.put("备注", adminventory.getFremark());
            map.put("关联部门", adminventory.getFdeptId());
            map.put("是否必填（0：可选，1：必填）", adminventory.getFrequired());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
