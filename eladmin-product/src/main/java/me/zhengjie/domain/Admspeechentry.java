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
* @date 2020-11-10
**/
@Entity
@Data
@Table(name="admspeechentry")
public class Admspeechentry implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FID")
    @ApiModelProperty(value = "主键id")
    private Integer fid;

    @Column(name = "FNumber")
    @ApiModelProperty(value = "产品编号")
    private String fNumber;

    @Column(name = "FName")
    @ApiModelProperty(value = "产品名称")
    private String fName;

    @Column(name = "FOutputQuantity")
    @ApiModelProperty(value = "当日产量")
    private Float fOutputquantity;

    @Column(name = "FRejectsQuantity")
    @ApiModelProperty(value = "不良品")
    private Float fRejectsquantity;

    @Column(name = "FBalanceQuantity")
    @ApiModelProperty(value = "结存")
    private Float fBalancequantity;

    @Column(name = "FNote")
    @ApiModelProperty(value = "备注")
    private String fNote;

    @Column(name = "FHeadId",nullable = false)
    @NotNull
    @ApiModelProperty(value = "关联表id")
    private Integer fHeadid;

    public void copy(Admspeechentry source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}