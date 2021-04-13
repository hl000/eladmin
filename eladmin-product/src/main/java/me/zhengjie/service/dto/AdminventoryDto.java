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
package me.zhengjie.service.dto;

import lombok.Data;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author t_k_c
* @date 2020-11-28
**/
@Data
public class AdminventoryDto implements Serializable {

    private Integer fid;

    /** 存货编码 */
    private String fnumber;

    /** 存货名称 */
    private String fname;

    /** 规格型号 */
    private String fstd;

    /** 单位 */
    private String funit;

    /** 归属部门 */
    private String fdept;

    /** 原始存货编码 */
    private String foldNumber;

    /** 状态（0可用，1不可用） */
    private String fstate;

    /** 备注 */
    private String fremark;

    /** 关联部门 */
    private String fdeptId;

    /** 是否必填（0：可选，1：必填） */
    private String frequired;
}