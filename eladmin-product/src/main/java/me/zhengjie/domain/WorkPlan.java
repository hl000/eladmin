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
@Table(name = "rd_work_plan")
public class WorkPlan {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键")
    private Integer id;

    @Column(name = "plan_code")
    @ApiModelProperty(value = "任务编号")
    private Integer planCode;

    @Column(name = "plan_name")
    @ApiModelProperty(value = "计划名")
    private String planName;

    @Column(name = "create_date")
    @ApiModelProperty(value = "创建时间")
    @CreationTimestamp
    private Timestamp createDate;
}
