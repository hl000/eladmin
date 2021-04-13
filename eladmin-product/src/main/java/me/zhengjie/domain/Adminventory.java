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
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author t_k_c
* @date 2020-11-28
**/
@Entity
@Data
@Table(name="adminventory")
public class Adminventory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FID")
    @ApiModelProperty(value = "fid")
    private Integer fid;

    @Column(name = "FNumber")
    @ApiModelProperty(value = "存货编码")
    private String fnumber;

    @Column(name = "FName")
    @ApiModelProperty(value = "存货名称")
    private String fname;

    @Column(name = "FStd")
    @ApiModelProperty(value = "规格型号")
    private String fstd;

    @Column(name = "FUnit")
    @ApiModelProperty(value = "单位")
    private String funit;

    @Column(name = "FDept")
    @ApiModelProperty(value = "归属部门")
    private String fdept;

    @Column(name = "FOld_Number")
    @ApiModelProperty(value = "原始存货编码")
    private String foldNumber;

    @Column(name = "FState")
    @ApiModelProperty(value = "状态（0可用，1不可用）")
    private String fstate;

    @Column(name = "FRemark")
    @ApiModelProperty(value = "备注")
    private String fremark;

    @Column(name = "FDept_Id")
    @ApiModelProperty(value = "关联部门")
    private String fdeptId;

    @Column(name = "FRequired")
    @ApiModelProperty(value = "是否必填（0：可选，1：必填）")
    private String frequired;

    public void copy(Adminventory source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}