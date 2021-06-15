package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * @author HL
 * @create 2021/6/7 9:37
 */
@Entity
@Data
@Table(name = "view_stack_work")
public class StackWorkView {

    @Id
    @Column(name = "uuid")
    @ApiModelProperty(value = "随机唯一id")
    private String uuid;

    @Column(name = "fill_date")
    @ApiModelProperty(value = "填报日期")
    private String fillDate;

    @Column(name = "manufacture_address")
    @ApiModelProperty(value = "生产基地")
    private String manufactureAddress;


    @Column(name = "manufacture_name")
    @ApiModelProperty(value = "报工名称")
    private String manufactureName;

    @Column(name = "Component")
    @ApiModelProperty(value = "产品名称")
    private String component;

    @Column(name = "daily_output")
    @ApiModelProperty(value = "日产量")
    private Integer dailyOutput;
}
