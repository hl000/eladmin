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
import me.zhengjie.mapper.SysLocalBatisMapper;
import me.zhengjie.service.SysLocalService;
import me.zhengjie.service.dto.SysLocalDto;
import me.zhengjie.utils.EnvironmentUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SysLocalServiceImpl implements SysLocalService {

  @Resource
  private SysLocalBatisMapper sysLocalBatisMapper;


  public List<SysLocalDto> getLocal(){
    return sysLocalBatisMapper.getLocal();
  }

  public Set<String> getLocalByuser (final long id){
    return sysLocalBatisMapper.getLocalByUser(getTable1(), getTable2(), id);
  }

  public String getTable1() {
    return EnvironmentUtils.isProd() ? "[eladmin]..[sys_user_local]" : "[eladminTEST]..[sys_user_local]";
  }

  public String getTable2() {
    return EnvironmentUtils.isProd() ? "[eladmin]..[sys_local]" : "[eladminTEST]..[sys_local]";
  }

}
