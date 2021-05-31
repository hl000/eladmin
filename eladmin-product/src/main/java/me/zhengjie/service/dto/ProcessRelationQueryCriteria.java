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
import me.zhengjie.annotation.Query;
import me.zhengjie.domain.Category;
import me.zhengjie.domain.ProductParameter;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author HL
 * @date 2021-4-13
 **/
@Data
public class ProcessRelationQueryCriteria {

    @Query
    private String process;

    @Query
    private Integer projectOrder;

    @Query(propName = "manufactureName",joinName = "productParameter")
    private String manufactureName;

    @Query(propName = "secondaryType",joinName = "category")
    private String secondaryType;
}