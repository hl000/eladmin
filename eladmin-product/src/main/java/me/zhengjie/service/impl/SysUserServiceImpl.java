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

import me.zhengjie.domain.SysUser;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.repository.SysUserRepository;
import me.zhengjie.service.SysUserService;
import me.zhengjie.service.dto.SysUserDto;
import me.zhengjie.service.dto.SysUserQueryCriteria;
import me.zhengjie.service.mapstruct.SysUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
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
public class SysUserServiceImpl implements SysUserService {

    private final SysUserRepository sysUserRepository;
    private final SysUserMapper sysUserMapper;

    @Override
    public Map<String,Object> queryAll(SysUserQueryCriteria criteria, Pageable pageable){
        Page<SysUser> page = sysUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(sysUserMapper::toDto));
    }

    @Override
    public List<SysUserDto> queryAll(SysUserQueryCriteria criteria){
        return sysUserMapper.toDto(sysUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public SysUserDto findById(Long userId) {
        SysUser sysUser = sysUserRepository.findById(userId).orElseGet(SysUser::new);
        ValidationUtil.isNull(sysUser.getUserId(),"SysUser","userId",userId);
        return sysUserMapper.toDto(sysUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysUserDto create(SysUser resources) {
        if(sysUserRepository.findByUsername(resources.getUsername()) != null){
            throw new EntityExistException(SysUser.class,"username",resources.getUsername());
        }
        if(sysUserRepository.findByEmail(resources.getEmail()) != null){
            throw new EntityExistException(SysUser.class,"email",resources.getEmail());
        }
        return sysUserMapper.toDto(sysUserRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysUser resources) {
        SysUser sysUser = sysUserRepository.findById(resources.getUserId()).orElseGet(SysUser::new);
        ValidationUtil.isNull( sysUser.getUserId(),"SysUser","id",resources.getUserId());
//        sysUser1 = sysUserRepository.findByUsername(resources.getUsername());
//        if(sysUser1 != null && !sysUser1.getUserId().equals(sysUser.getUserId())){
//            throw new EntityExistException(SysUser.class,"username",resources.getUsername());
//        }
//        sysUser1 = sysUserRepository.findByEmail(resources.getEmail());
//        if(sysUser1 != null && !sysUser1.getUserId().equals(sysUser.getUserId())){
//            throw new EntityExistException(SysUser.class,"email",resources.getEmail());
//        }
        sysUser.copy(resources);
        sysUserRepository.save(sysUser);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long userId : ids) {
            sysUserRepository.deleteById(userId);
        }
    }

    @Override
    public void download(List<SysUserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SysUserDto sysUser : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("部门名称", sysUser.getDeptId());
            map.put("用户名", sysUser.getUsername());
            map.put("昵称", sysUser.getNickName());
            map.put("性别", sysUser.getGender());
            map.put("手机号码", sysUser.getPhone());
            map.put("邮箱", sysUser.getEmail());
            map.put("头像地址", sysUser.getAvatarName());
            map.put("头像真实路径", sysUser.getAvatarPath());
            map.put("密码", sysUser.getPassword());
            map.put("是否为admin账号", sysUser.getIsAdmin());
            map.put("状态：1启用、0禁用", sysUser.getEnabled());
            map.put("创建者", sysUser.getCreateBy());
            map.put("更新着", sysUser.getUpdateBy());
            map.put("修改密码的时间", sysUser.getPwdResetTime());
            map.put("创建日期", sysUser.getCreateTime());
            map.put("更新时间", sysUser.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}