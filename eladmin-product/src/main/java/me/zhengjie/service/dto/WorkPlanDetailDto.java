package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.domain.WorkPlan;
import me.zhengjie.domain.WorkPlanDetailOutputType;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author HL
 * @create 2022/1/14 15:33
 */
@Data
public class WorkPlanDetailDto implements Serializable {

    private WorkPlan workPlan;

    private String detailName;

    private String detailCode;

    private String dutyPerson;

    private String updater;

    private String updateReason;

    private WorkPlanDetailOutputType workPlanDetailOutputType;

    private String planStartDate;

    private String planFinishDate;

    private String actFinishDate;

    private Timestamp createDate;

    private String remark;

    private Boolean isUpdate;

    private Boolean isDelete;

}
