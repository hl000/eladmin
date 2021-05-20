package me.zhengjie.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author HL
 * @create 2021/4/13 15:11
 */
@Data
public class CategoryDto implements Serializable {

    private Integer id;

    /**
     * 部门Id
     */
    private String deptIds;

    /**
     * 一级分类
     */
    private String primaryType;

    private String secondaryType;

    private Integer processSequence;


}
