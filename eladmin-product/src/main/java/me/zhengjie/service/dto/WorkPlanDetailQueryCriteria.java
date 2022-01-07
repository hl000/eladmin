package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author HL
 * @create 2021/12/13 10:22
 */
@Data
public class WorkPlanDetailQueryCriteria {

    @Query(propName = "id", joinName = "workPlan")
    private Integer workPlanId;

    @Query
    private Integer id;

    @Query(propName = "outputResult", joinName = "workPlanDetailOutputType")
    private String outputResult;

    @Query(type = Query.Type.INNER_LIKE)
    private String detailName;

    @Query
    private String detailCode;
//
//    @Query
//    private String dutyPerson;

    @Query(type = Query.Type.BETWEEN)
    private List<String> planStartDate;

    @Query(type = Query.Type.BETWEEN)
    private List<String> planFinishDate;

    @Query
    private String status;

    @Query
    private String planTypeName;

//    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//    private String today = dateFormat.format(new Date());
}
