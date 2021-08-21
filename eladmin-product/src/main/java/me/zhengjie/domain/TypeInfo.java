package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * @author HL
 * @create 2021/7/12 15:24
 */
@Entity
@Data
@Table(name = "report_type")
public class TypeInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "主键id")
    private Integer id;

    @Column(name = "name")
    @ApiModelProperty(value = "名称")
    private  String name;

    @Column(name = "value")
    @ApiModelProperty(value = "值")
    private String value;

    @Column(name = "address")
    @ApiModelProperty(value = "基地")
    private String address;
}
