package me.zhengjie.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.zhengjie.domain.WorkPlanDetail;

import java.util.List;

/**
 * @author HL
 * @create 2021/12/13 10:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkPlanGroupDto {

    private String planName;

    private List<WorkPlanDetail> workPlanDetails;

    private String exist;

    private Integer row;

}
