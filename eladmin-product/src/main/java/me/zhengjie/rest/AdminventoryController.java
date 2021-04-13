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
package me.zhengjie.rest;

import cn.hutool.json.JSONObject;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.Adminventory;
import me.zhengjie.service.AdminventoryService;
import me.zhengjie.service.dto.AdminventoryQueryCriteria;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://el-admin.vip
* @author t_k_c
* @date 2020-11-28
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "getProduct管理")
@RequestMapping("/api/adminventory")
public class AdminventoryController {

    private final AdminventoryService adminventoryService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('adminventory:list')")
    public void download(HttpServletResponse response, AdminventoryQueryCriteria criteria) throws IOException {
        adminventoryService.download(adminventoryService.queryAll(criteria), response);
    }

  @GetMapping("/getProduct")
  @Log("查询getProduct")
  @ApiOperation("查询getProduct")
  public ResponseEntity<Object> query(AdminventoryQueryCriteria criteria, Pageable pageable){
    UserDetails userDetails = SecurityUtils.getCurrentUser();
    JSONObject deptObject = (JSONObject) new JSONObject(new JSONObject(userDetails).get("user")).get("dept");
    criteria.setFstate("0");
    criteria.setFdeptId(deptObject.get("id", String.class));
    return new ResponseEntity<>(adminventoryService.queryAll(criteria),HttpStatus.OK);
  }

    @PostMapping
    @Log("新增getProduct")
    @ApiOperation("新增getProduct")
    @PreAuthorize("@el.check('adminventory:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Adminventory resources){
        return new ResponseEntity<>(adminventoryService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改getProduct")
    @ApiOperation("修改getProduct")
    @PreAuthorize("@el.check('adminventory:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Adminventory resources){
        adminventoryService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除getProduct")
    @ApiOperation("删除getProduct")
    @PreAuthorize("@el.check('adminventory:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
        adminventoryService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
