package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author HL
 * @create 2023/12/13 16:01
 */
@Entity
@Data
@Table(name = "user_match")
public class UserMatch implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Integer id;

    @Column(name = "department")
    @ApiModelProperty(value = "部门")
    private String department;

    @Column(name = "leader")
    @ApiModelProperty(value = "负责人")
    private String leader;

    @Column(name = "process")
    @ApiModelProperty(value = "工序")
    private String process;
}
