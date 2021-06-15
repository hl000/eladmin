package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * @author HL
 * @create 2021/6/10 19:19
 */
@Entity
@Data
@Table(name = "report_reasons")
public class Reasons {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Integer id;

    @Column(name = "reason")
    @ApiModelProperty(value = "不合格原因")
    private String reason;
}
