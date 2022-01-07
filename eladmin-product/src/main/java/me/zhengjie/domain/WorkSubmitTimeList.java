package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * @author HL
 * @create 2021/12/29 11:33
 */
@Entity
@Data
@Table(name = "work_submit_time_list")
public class WorkSubmitTimeList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @ApiModelProperty(value = "ID")
    private Integer id;

    @Column(name = "FTime_Periods")
    @ApiModelProperty(value = "时间段")
    private String fTimePeriods;

    @Column(name = "FCreate_Date")
    @ApiModelProperty(value = "创建时间")
    private String dCreateDate;

    @Column(name = "FIs_Deleted")
    @ApiModelProperty(value = "是否删除")
    private Integer fIsDeleted;
}
