package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author HL
 * @create 2021/6/7 9:37
 */
@Entity
@Data
@Table(name = "view_stack_remain")
public class StackRemainView {

    @Id
    @Column(name = "uuid")
    @ApiModelProperty(value = "随机唯一id")
    private String uuid;

    @Column(name = "manufacture_address")
    @ApiModelProperty(value = "生产基地")
    private String manufactureAddress;

    @Column(name = "manufacture_name")
    @ApiModelProperty(value = "报工名称")
    private String manufactureName;

    @Column(name = "Component")
    @ApiModelProperty(value = "产品名称")
    private String component;

    @Column(name = "remain_quantity")
    @ApiModelProperty(value = "结存")
    private Integer remainQuantity;
}
