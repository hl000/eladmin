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
@Table(name="admspeechhead")
public class Admspeechhead implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_Id")
    @ApiModelProperty(value = "主键id")
    private Integer fId;

    @Column(name = "F_Use_Id")
    @ApiModelProperty(value = "用户id")
    private Integer fUseId;

    @Column(name = "F_Date")
    @ApiModelProperty(value = "创建时间")
    private Timestamp fDate;

    @Column(name = "F_Address")
    @ApiModelProperty(value = "归属地")
    private String fAddress;

    @Column(name = "F_Model")
    @ApiModelProperty(value = "型号")
    private String fModel;

    @Column(name = "F_Today")
    @ApiModelProperty(value = "今日完成内容")
    private String fToday;

    @Column(name = "F_Tomorrow")
    @ApiModelProperty(value = "明日计划内容")
    private String fTomorrow;

    @Column(name = "F_Produce_Date")
    @ApiModelProperty(value = "生产日期")
    private String fProduceDate;

    public void copy(Admspeechhead source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}