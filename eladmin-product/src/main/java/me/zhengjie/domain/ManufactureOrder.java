package me.zhengjie.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

/**
 * @author HL
 * @create 2021/5/28 9:06
 */
@Entity
@Data
@Table(name = "report_manufacture_order")
public class ManufactureOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "主键id")
    private Integer id;

    @Column(name = "stack_specific")
    @ApiModelProperty(value = "电堆型号")
    private String stackSpecific;

    @Column(name = "stack_number")
    @ApiModelProperty(value = "电堆编号")
    private String stackNumber;

    @Column(name = "test_purpose")
    @ApiModelProperty(value = "测试目的")
    private String testPurpose;

    @Column(name = "pitch_number")
    @ApiModelProperty(value = "电堆节数")
    private Integer pitchNumber;

    @Column(name = "active_area")
    @ApiModelProperty(value = "活性面积")
    private Double activeArea;

    @JoinColumn(name = "user_id")
    @ApiModelProperty(value = "实验设计人")
    @ManyToOne(fetch = FetchType.EAGER)
    private SysUser experimentDesigner;

    @Column(name = "FBIP")
    @ApiModelProperty(value = "BIP编号")
    private String FBIP;

    @Column(name = "MEA")
    @ApiModelProperty(value = "MEA型号")
    private String MEA;

    @Column(name = "assembly_pressure")
    @ApiModelProperty(value = "组装压力")
    private Double assemblyPressure;

    @Column(name = "ambient_temperature")
    @ApiModelProperty(value = "环境温度")
    private Double ambientTemperature;

    @Column(name = "ambient_humidity")
    @ApiModelProperty(value = "环境湿度")
    private Double ambientHumidity;

    @Column(name = "assembly_date")
    @ApiModelProperty(value = "组装日期")
    private Date assemblyDate;

    @Column(name = "end_paper_model")
    @ApiModelProperty(value = "碳纸型号")
    private String endPaperModel;

    @Column(name = "end_paper_thickness")
    @ApiModelProperty(value = "端板碳纸厚度")
    private Double endPaperThickness;

    @Column(name = "stack_identification")
    @ApiModelProperty(value = "电堆新旧标识")
    private String stackIdentification;

//    @Column(name = "relief_pressure")
//    @ApiModelProperty(value = "泄压压力")
//    private Double reliefPressure;

    @Column(name = "equipment_flatness")
    @ApiModelProperty(value = "设备平整度")
    private Double equipmentFlatness;

    @JoinColumn(name = "work_device_id")
    @ApiModelProperty(value = "组装设备编号")
    @ManyToOne(fetch = FetchType.EAGER)
    private WorkDevice workDevice;

    @Column(name = "assembly_personnel")
    @ApiModelProperty(value = "组堆人员")
    private String assemblyPersonnel;

    @Column(name = "unit_number")
    @ApiModelProperty(value = "单元节数")
    private Integer unitNumber;

    @Column(name = "unit_leak_stress")
    @ApiModelProperty(value = "单元胶线测漏压力")
    private Double unitLeakStress;

    @Column(name = "unit_personnel")
    @ApiModelProperty(value = "单元组装人员")
    private String unitPersonnel;

    @Column(name = "in_option_one")
    @ApiModelProperty(value = "位置1")
    private Double inOptionOne;

    @Column(name = "in_option_two")
    @ApiModelProperty(value = "位置2")
    private Double inOptionTwo;

    @Column(name = "average_inner_ruler")
    @ApiModelProperty(value = "平均内尺寸")
    private Double averageInnerRuler;

    @Column(name = "inner_ruler_gap")
    @ApiModelProperty(value = "内尺寸高度差")
    private Double innerRulerGap;

    @Column(name = "out_option_one")
    @ApiModelProperty(value = "外尺寸1")
    private Double outOptionOne;

    @Column(name = "out_option_two")
    @ApiModelProperty(value = "外尺寸2")
    private Double outOptionTwo;

    @Column(name = "average_outer_ruler")
    @ApiModelProperty(value = "平均外尺寸")
    private Double averageOuterRuler;

    @Column(name = "outer_ruler_gap")
    @ApiModelProperty(value = "外尺寸高度差")
    private Double outerRulerGap;

    @Column(name = "spring_dimension1")
    @ApiModelProperty(value = "弹簧尺寸1")
    private Double springDimension1;

    @Column(name = "spring_dimension2")
    @ApiModelProperty(value = "弹簧尺寸2")
    private Double springDimension2;

    @Column(name = "spring_dimension3")
    @ApiModelProperty(value = "弹簧尺寸3")
    private Double springDimension3;

    @Column(name = "spring_dimension_gap")
    @ApiModelProperty(value = "弹簧尺寸差")
    private Double springDimensionGap;

    @Column(name = "hydrogen_oxygen_mix")
    @ApiModelProperty(value = "氢氧互串")
    private Double hydrogenOxygenMix;

    @Column(name = "oxygen_hydrogen_mix")
    @ApiModelProperty(value = "氧氢互串")
    private Double oxygenHydrogenMix;

    @Column(name = "average_mix")
    @ApiModelProperty(value = "互串平均值")
    private Double averageMix;

    @Column(name = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    public void copy(ManufactureOrder source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(false));
    }
}
