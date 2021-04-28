package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author HL
 * @create 2021/4/27 17:52
 */
@Entity
@Data
@Table(name="view_report_summary")
public class SummaryView implements Serializable {
    @Id
    @Column(name = "class1")
    @ApiModelProperty(value = "一级分类")
    private String class1;

    @Column(name = "class2")
    @ApiModelProperty(value = "二级分类")
    private String class2;

    @Column(name = "sumDate")
    @ApiModelProperty(value = "报工日期")
    private Timestamp sumDate;

    @Column(name = "baNum")
    @ApiModelProperty(value = "生产批次号")
    private String baNum;

    @Column(name = "planName")
    @ApiModelProperty(value = "批计划名称")
    private String planName;

    @Column(name = "proName")
    @ApiModelProperty(value = "报工名称")
    private String proName;

    @Column(name = "proNum")
    @ApiModelProperty(value = "产品代码")
    private String proNum;

    @Column(name = "outPuts")
    @ApiModelProperty(value = "日实际产量（含不良品）")
    private Integer outPuts;

    @Column(name = "rejects")
    @ApiModelProperty(value = "日不良品数量")
    private Integer rejects;

    @Column(name = "rejectRate")
    @ApiModelProperty(value = "日不良率")
    private Double rejectRate;

    @Column(name = "reasonRej")
    @ApiModelProperty(value = "废品原因说明")
    private String reasonRej;

    @Column(name = "workHour")
    @ApiModelProperty(value = "工作时长")
    private Double workHour;

    @Column(name = "realWork")
    @ApiModelProperty(value = "实际工时")
    private Double realWork;

    @Column(name = "rateWork")
    @ApiModelProperty(value = "工时达成率")
    private Double rateWork;

    @Column(name = "balance")
    @ApiModelProperty(value = "工序结存数")
    private Integer balance;

    @Column(name = "rateCap")
    @ApiModelProperty(value = "产能利用率")
    private Double rateCap;

    @Column(name = "reasonCap")
    @ApiModelProperty(value = "日产能未完成原因")
    private String reasonCap;

    @Column(name = "capMaxHour")
    @ApiModelProperty(value = "当前每小时最大产能")
    private Double capMaxHour;

    @Column(name = "capEight")
    @ApiModelProperty(value = "八小时产能")
    private Double capEight;

    @Column(name = "capTen")
    @ApiModelProperty(value = "十小时产能")
    private Double capTen;

    @Column(name = "capForW")
    @ApiModelProperty(value = "一周产能预测")
    private Double capForW;

    @Column(name = "manMaxHour")
    @ApiModelProperty(value = "设备最大产能")
    private Double manMaxHour;

    @Column(name = "yieldDay")
    @ApiModelProperty(value = "日实际产量（含不良品）")
    private Integer yieldDay;

    @Column(name = "rateDp")
    @ApiModelProperty(value = "日计划完成率")
    private Double rateDp;

    @Column(name = "theWork")
    @ApiModelProperty(value = "理论工时")
    private Double theWork;

    @Column(name = "menNum")
    @ApiModelProperty(value = "班组人员数")
    private Integer menNum;

    @Column(name = "quaHour")
    @ApiModelProperty(value = "工时定额")
    private Double quaHour;

    @Column(name = "quaMeta1")
    @ApiModelProperty(value = "材料1定额")
    private Double quaMeta1;

    @Column(name = "quaMeta2")
    @ApiModelProperty(value = "材料2定额")
    private Double quaMeta2;

    @Column(name = "quaMeta3")
    @ApiModelProperty(value = "材料3定额")
    private Double quaMeta3;

    @Column(name = "conMate1")
    @ApiModelProperty(value = "材料1实际")
    private Double conMate1;

    @Column(name = "conMate2")
    @ApiModelProperty(value = "材料2实际")
    private Double conMate2;

    @Column(name = "conMate3")
    @ApiModelProperty(value = "材料3实际")
    private Double conMate3;

    @Column(name = "rateMate1")
    @ApiModelProperty(value = "实际材料1定额达成率")
    private Double rateMate1;

    @Column(name = "rateMate2")
    @ApiModelProperty(value = "实际材料2定额达成率")
    private Double rateMate2;

    @Column(name = "rateMate3")
    @ApiModelProperty(value = "实际材料3定额达成率")
    private Double rateMate3;

    @Column(name = "conMeta1")
    @ApiModelProperty(value = "意外消耗材料1")
    private Double conMeta1;

    @Column(name = "conMeta2")
    @ApiModelProperty(value = "意外消耗材料2")
    private Double conMeta2;

    @Column(name = "conMeta3")
    @ApiModelProperty(value = "意外消耗材料3")
    private Double conMeta3;

    @Column(name = "insNum")
    @ApiModelProperty(value = "工艺指导书编号")
    private String insNum;

    @Column(name = "insVer")
    @ApiModelProperty(value = "工艺指导书版本")
    private String insVer;

    @Column(name = "proceName")
    @ApiModelProperty(value = "工艺人员")
    private String proceName;

    @Column(name = "proceCdate")
    @ApiModelProperty(value = "工艺变更日期")
    private Timestamp proceCdate;

    @Column(name = "proceCname")
    @ApiModelProperty(value = "工艺变更人员")
    private String proceCname;
}