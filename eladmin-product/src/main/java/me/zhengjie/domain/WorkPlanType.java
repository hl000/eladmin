package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author HL
 * @create 2021/12/8 15:58
 */
@Entity
@Data
@Table(name = "rd_work_plan_type")
public class WorkPlanType {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键")
    private Integer id;

    @Column(name = "plan_type_name")
    @ApiModelProperty(value = "计划分类名")
    private String planTypeName;

    @Column(name = "row")
    @ApiModelProperty(value = "排序号")
    private Integer row;
}
