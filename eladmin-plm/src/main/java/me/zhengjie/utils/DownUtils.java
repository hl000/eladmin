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
package me.zhengjie.utils;

import me.zhengjie.dto.MainDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DownUtils extends cn.hutool.core.io.FileUtil {

  private static final Logger log = LoggerFactory.getLogger(DownUtils.class);

  public static void downloadMain(List<MainDto> ret, HttpServletResponse response) throws IOException {
    List<Map<String, Object>> list = new ArrayList<>();
    for (MainDto mainDto : ret) {
      Map<String, Object> map = new LinkedHashMap<>();
      map.put("系统编号", mainDto.getXITONG());
      map.put("电堆编号", mainDto.getDIANDUI());
      map.put("增湿器",  mainDto.getZENGSHIQI());
      list.add(map);
    }
    FileUtil.downloadExcel(list, response);
  }
}
