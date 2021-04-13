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
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author t_k_c
* @date 2020-11-10
**/
@Data
public class AdmspeechheadDto implements Serializable {

    /** 主键id */
    private Integer fId;

    /** 用户id */
    private Integer fUseId;

    /** 创建时间 */
    private Timestamp fDate;

    /** 归属地 */
    private String fAddress;

    /** 型号 */
    private String fModel;

    /** 今日完成内容 */
    private String fToday;

    /** 明日计划内容 */
    private String fTomorrow;

    /** 生产日期 */
    private String fProduceDate;

    /** 更新日期 */
    private String fUpdate;
}
