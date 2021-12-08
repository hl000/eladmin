package me.zhengjie.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

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

    @Column(name = "stack_number")
    @ApiModelProperty(value = "电堆编号")
    private String stackNumber;

    @Column(name = "pitch_number")
    @ApiModelProperty(value = "节数")
    private Integer pitchNumber;

    @Column(name = "FBIP")
    @ApiModelProperty(value = "电堆编号")
    private String FBIP;

    @Column(name = "test_purpose")
    @ApiModelProperty(value = "测试目的")
    private String testPurpose;

//    @Column(name = "fake_batteries")
//    @ApiModelProperty(value = "假电池数")
//    private Integer fakeBatteries;

    @Column(name = "MEA")
    @ApiModelProperty(value = "MEA型号")
    private String MEA;

    @Column(name = "mea_date")
    @ApiModelProperty(value = "MEA日期")
    private Date meaDate;

    @Column(name = "carbon_paper")
    @ApiModelProperty(value = "碳纸类型")
    private String carbonPaper;

    @Column(name = "temperature")
    @ApiModelProperty(value = "温度")
    private String temperature;

    @Column(name = "humidity")
    @ApiModelProperty(value = "湿度")
    private String humidity;

    @Column(name = "unit_date")
    @ApiModelProperty(value = "单元组装日期")
    private Date unitDate;

    @Column(name = "unit_personnel")
    @ApiModelProperty(value = "单元组装人员")
    private String unitPersonnel;

    @Column(name = "unit_stress")
    @ApiModelProperty(value = "单元组装压力")
    private Double unitStress;

    @Column(name = "option1")
    @ApiModelProperty(value = "位置1")
    private Double option1;

    @Column(name = "spring_dimension1")
    @ApiModelProperty(value = "弹簧尺寸1")
    private Double springDimension1;

    @Column(name = "spring_dimension2")
    @ApiModelProperty(value = "弹簧尺寸2")
    private Double springDimension2;

    @Column(name = "spring_dimension3")
    @ApiModelProperty(value = "弹簧尺寸3")
    private Double springDimension3;

    @Column(name = "option4")
    @ApiModelProperty(value = "位置4")
    private Double option4;

    @Column(name = "option5")
    @ApiModelProperty(value = "位置5")
    private Double option5;

    @Column(name = "option6")
    @ApiModelProperty(value = "位置6")
    private Double option6;

    @Column(name = "option1_pressure")
    @ApiModelProperty(value = "加压后位置1")
    private Double option1Pressure;

    @Column(name = "spring_dimension1_pressure")
    @ApiModelProperty(value = "加压后弹簧尺寸1")
    private Double springDimension1Pressure;

    @Column(name = "spring_dimension2_pressure")
    @ApiModelProperty(value = "加压后弹簧尺寸2")
    private Double springDimension2Pressure;

    @Column(name = "spring_dimension3_pressure")
    @ApiModelProperty(value = "加压后弹簧尺寸3")
    private Double springDimension3Pressure;

    @Column(name = "option4_pressure")
    @ApiModelProperty(value = "加压后位置4")
    private Double option4Pressure;

    @Column(name = "option5_pressure")
    @ApiModelProperty(value = "加压后位置5")
    private Double option5Pressure;

    @Column(name = "option6_pressure")
    @ApiModelProperty(value = "加压后位置6")
    private Double option6Pressure;

    @Column(name = "assembly_stress_pressure")
    @ApiModelProperty(value = "加压后组装压力")
    private Double assemblyStressPressure;


    @Column(name = "leakage_detection")
    @ApiModelProperty(value = "外露检测")
    private String leakageDetection;

    @Column(name = "hydrogen_oxygen_mix")
    @ApiModelProperty(value = "氢氧互串")
    private String hydrogenOxygenMix;

    @Column(name = "oxygen_hydrogen_mix")
    @ApiModelProperty(value = "氧氢互串")
    private String oxygenHydrogenMix;

    @Column(name = "hydrogen_oxygen_water")
    @ApiModelProperty(value = "氢氧串水")
    private String hydrogenOxygenWater;

    @Column(name = "water_hydrogen")
    @ApiModelProperty(value = "水串氢")
    private String waterHydrogen;

    @Column(name = "water_oxygen")
    @ApiModelProperty(value = "水串氧")
    private String waterOxygen;

    @Column(name = "hydrogen_water")
    @ApiModelProperty(value = "氢串水")
    private String hydrogenWater;

    @Column(name = "oxygen_water")
    @ApiModelProperty(value = "氧串水")
    private String oxygenWater;

    @Column(name = "assembly_stress")
    @ApiModelProperty(value = "组装压力")
    private Double assemblyStress;

    @Column(name = "end_paper_height")
    @ApiModelProperty(value = "端板碳纸高度")
    private Double endPaperHeight;

    @Column(name = "carbon_paper_height")
    @ApiModelProperty(value = "光板碳纸高度")
    private Double carbonPaperHeight;

    @Column(name = "assembly_personnel")
    @ApiModelProperty(value = "组堆人员")
    private String assemblyPersonnel;

    @Column(name = "assembly_date")
    @ApiModelProperty(value = "组堆日期")
    private Date assemblyDate;

    @Column(name = "note")
    @ApiModelProperty(value = "备注")
    private String note;

    public void copy(ManufactureOrder source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(false));
    }
}
