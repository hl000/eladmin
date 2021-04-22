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

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.domain.Category;

import javax.persistence.Column;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class TechniqueInfoDto implements Serializable {

    private Integer id;

    /**
     * 工艺序号
     **/
    private String workpieceNumber;

    /**
     * 产品代码
     **/
    private String productCode;

    /**
     * 指导书编号
     **/
    private String instructorNumber;

    /**
     * 指导书版本
     **/
    private String instructorVersion;

    /**
     * 工艺人员
     **/
    private String technologist;

    /**
     * 最大设备产能
     **/
    private Double equipmentMaxCapacity;

    /**
     * 最大当前设备产能
     **/
    private Double currentMaxCapacity;

    /**
     * 工时定额
     **/
    private Double hourNorm;

    /**
     * 工艺变更人员
     */
    private String processChanger;

    /**
     * 工艺变更时日期
     */
    private Timestamp changeDate;

    /**
     * 材料1定额
     **/
    private Double material1Quota;

    /**
     * 材料2定额
     **/
    private Double material2Quota;

    /**
     * 材料3定额
     **/
    private Double material3Quota;

    /**
     * 分类
     **/
    private Category category;

    /**用户Id**/
    private Long userId;
}