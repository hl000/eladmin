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
* @date 2020-11-10
**/
@Data
public class AdmspeechentryDto implements Serializable {

    /** 主键id */
    private Integer fid;

    /** 产品编号 */
    private String fNumber;

    /** 产品名称 */
    private String fName;

    /** 当日产量 */
    private Float fOutputquantity;

    /** 不良品 */
    private Float fRejectsquantity;

    /** 结存 */
    private Float fBalancequantity;

    /** 备注 */
    private String fNote;

    /** 关联表id */
    private Integer fHeadid;
}