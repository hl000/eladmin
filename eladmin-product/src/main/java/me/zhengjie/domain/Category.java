package me.zhengjie.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author HL
 * @create 2021/4/14 14:02
 */
@Entity
@Data
@Table(name="category")
public class Category  implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id")
    private Integer id;

    @Column(name = "dept_ids")
    @ApiModelProperty(value = "部门Id列表")
    private String deptIds;

    @Column(name = "primary_type")
    @ApiModelProperty(value = "一级分类")
    private String primaryType;

    @Column(name = "secondary_type")
    @ApiModelProperty(value = "二级分类")
    private String secondaryType;

    public void copy(Category source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }


}
