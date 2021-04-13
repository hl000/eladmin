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
package me.zhengjie.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author t_k_c
* @date 2020-11-10
**/
@Entity
@Data
@Table(name="sys_dept")
public class SysDept implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_id")
    @ApiModelProperty(value = "ID")
    private Long deptId;

    @Column(name = "pid")
    @ApiModelProperty(value = "上级部门")
    private Long pid;

    @ApiModelProperty(value = "子部门数目")
    private Integer subCount;

    @Column(name = "name",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "排序")
    private Integer deptSort;

    @Column(name = "enabled",nullable = false)
    @NotNull
    @ApiModelProperty(value = "状态")
    private Boolean enabled;

    @Column(name = "create_by")
    @ApiModelProperty(value = "创建者")
    private String createBy;

    @Column(name = "update_by")
    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @Column(name = "create_time")
    @ApiModelProperty(value = "创建日期")
    private Timestamp createTime;

    @Column(name = "update_time")
    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    public void copy(SysDept source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}