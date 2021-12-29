package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author HL
 * @create 2021/12/8 16:00
 */
@Entity
@Data
@Table(name = "rd_work_plan_detail")
public class WorkPlanDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Integer id;

    @JoinColumn(name = "work_plan_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private WorkPlan workPlan;

    @Column(name = "detail_name")
    @ApiModelProperty(value = "任务描述")
    private String detailName;

    @Column(name = "detail_code")
    @ApiModelProperty(value = "任务编码")
    private Integer detailCode;

    @Column(name = "duty_person")
    @ApiModelProperty(value = "负责人")
    private String dutyPerson;

    @JoinColumn(name = "output_result_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private WorkPlanDetailOutputType workPlanDetailOutputType;

    @Column(name = "plan_start_date")
    @ApiModelProperty(value = "计划开始时间")
    private String planStartDate;

    @Column(name = "plan_finish_date")
    @ApiModelProperty(value = "计划结束时间")
    private String planFinishDate;

    @Column(name = "act_finish_date")
    @ApiModelProperty(value = "实际结束时间")
    private String actFinishDate;

    @Column(name = "create_date")
    @ApiModelProperty(value = "创建时间")
    @CreationTimestamp
    private Timestamp createDate;

    @Column(name = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkPlanDetail that = (WorkPlanDetail) o;
        return Objects.equals(id, that.id) && Objects.equals(workPlan.getId(), that.workPlan.getId())&& Objects.equals(detailName, that.detailName) && Objects.equals(detailCode, that.detailCode) && Objects.equals(dutyPerson, that.dutyPerson) && Objects.equals(workPlanDetailOutputType.getId(), that.workPlanDetailOutputType.getId()) && Objects.equals(planStartDate, that.planStartDate) && Objects.equals(planFinishDate, that.planFinishDate) && Objects.equals(actFinishDate, that.actFinishDate) && Objects.equals(remark, that.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workPlan, detailName, detailCode, dutyPerson, workPlanDetailOutputType, planStartDate, planFinishDate, actFinishDate, createDate, remark);
    }
}
