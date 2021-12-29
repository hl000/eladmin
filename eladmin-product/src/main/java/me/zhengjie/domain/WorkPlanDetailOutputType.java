package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * @author HL
 * @create 2021/12/9 16:40
 */
@Entity
@Data
@Table(name="rd_work_plan_detail_output_type")
public class WorkPlanDetailOutputType {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键")
    private Integer id;

    @Column(name = "output_result")
    @ApiModelProperty(value = "输出类型")
    private String outputResult;
}
