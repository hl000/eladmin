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

import me.zhengjie.annotation.Log;
import me.zhengjie.domain.SysUser;
import me.zhengjie.service.SysUserService;
import me.zhengjie.service.dto.SysUserQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://el-admin.vip
* @author t_k_c
* @date 2020-11-10
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "user管理")
@RequestMapping("/api/sysUser")
public class SysUserController {

    private final SysUserService sysUserService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('sysUser:list')")
    public void download(HttpServletResponse response, SysUserQueryCriteria criteria) throws IOException {
        sysUserService.download(sysUserService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询user")
    @ApiOperation("查询user")
    @PreAuthorize("@el.check('sysUser:list')")
    public ResponseEntity<Object> query(SysUserQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(sysUserService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增user")
    @ApiOperation("新增user")
    @PreAuthorize("@el.check('sysUser:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody SysUser resources){
        return new ResponseEntity<>(sysUserService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改user")
    @ApiOperation("修改user")
    @PreAuthorize("@el.check('sysUser:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody SysUser resources){
        sysUserService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除user")
    @ApiOperation("删除user")
    @PreAuthorize("@el.check('sysUser:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        sysUserService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}