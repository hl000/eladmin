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
import me.zhengjie.domain.SysDept;
import me.zhengjie.service.SysDeptService;
import me.zhengjie.service.dto.SysDeptQueryCriteria;
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
@Api(tags = "dept管理")
@RequestMapping("/api/sysDept")
public class SysDeptController {

    private final SysDeptService sysDeptService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('sysDept:list')")
    public void download(HttpServletResponse response, SysDeptQueryCriteria criteria) throws IOException {
        sysDeptService.download(sysDeptService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询dept")
    @ApiOperation("查询dept")
    @PreAuthorize("@el.check('sysDept:list')")
    public ResponseEntity<Object> query(SysDeptQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(sysDeptService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增dept")
    @ApiOperation("新增dept")
    @PreAuthorize("@el.check('sysDept:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody SysDept resources){
        return new ResponseEntity<>(sysDeptService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改dept")
    @ApiOperation("修改dept")
    @PreAuthorize("@el.check('sysDept:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody SysDept resources){
        sysDeptService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除dept")
    @ApiOperation("删除dept")
    @PreAuthorize("@el.check('sysDept:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        sysDeptService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}