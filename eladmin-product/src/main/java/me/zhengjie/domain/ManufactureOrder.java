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

    @Column(name = "test_purpose")
    @ApiModelProperty(value = "测试目的")
    private String testPurpose;

    @Column(name = "FBIP")
    @ApiModelProperty(value = "BIP编号")
    private String FBIP;

//    @Column(name = "fake_batteries")
//    @ApiModelProperty(value = "假电池数")
//    private Integer fakeBatteries;

    @Column(name = "MEA")
    @ApiModelProperty(value = "MEA型号")
    private String MEA;

    @Column(name = "mea_date")
    @ApiModelProperty(value = "MEA日期")
    private Date meaDate;

    @Column(name = "carbon_paper_thickness")
    @ApiModelProperty(value = "碳纸厚度")
    private Double carbonPaperThickness;

    @Column(name = "proton_membrane_thickness")
    @ApiModelProperty(value = "质子膜厚度")
    private Double protonMembraneThickness;

    @Column(name = "border_thickness")
    @ApiModelProperty(value = "边框厚度")
    private Double borderThickness;

    @Column(name = "unit_number")
    @ApiModelProperty(value = "单元节数")
    private Integer unitNumber;

    @Column(name = "unit_personnel")
    @ApiModelProperty(value = "单元组装人员")
    private String unitPersonnel;


    @Column(name = "unit_leak_stress")
    @ApiModelProperty(value = "单元胶线测漏压力")
    private Double unitLeakStress;

    @Column(name = "option1_one")
    @ApiModelProperty(value = "位置1一次")
    private Double option1One;

    @Column(name = "option1_two")
    @ApiModelProperty(value = "位置1二次")
    private Double option1Two;

    @Column(name = "option1_three")
    @ApiModelProperty(value = "位置1三次")
    private Double option1Three;

    @Column(name = "option2_one")
    @ApiModelProperty(value = "位置2一次")
    private Double option2One;

    @Column(name = "option2_two")
    @ApiModelProperty(value = "位置2二次")
    private Double option2Two;

    @Column(name = "option2_three")
    @ApiModelProperty(value = "位置2三次")
    private Double option2Three;

    @Column(name = "option3_one")
    @ApiModelProperty(value = "位置3一次")
    private Double option3One;

    @Column(name = "option3_two")
    @ApiModelProperty(value = "位置3二次")
    private Double option3Two;

    @Column(name = "option3_three")
    @ApiModelProperty(value = "位置3三次")
    private Double option3Three;

    @Column(name = "option4_one")
    @ApiModelProperty(value = "位置4一次")
    private Double option4One;

    @Column(name = "option4_two")
    @ApiModelProperty(value = "位置4二次")
    private Double option4Two;

    @Column(name = "option4_three")
    @ApiModelProperty(value = "位置4三次")
    private Double option4Three;

    @Column(name = "spring_dimension1_one")
    @ApiModelProperty(value = "弹簧尺寸1一次")
    private Double springDimension1One;

    @Column(name = "spring_dimension1_two")
    @ApiModelProperty(value = "弹簧尺寸1二次")
    private Double springDimension1Two;

    @Column(name = "spring_dimension1_three")
    @ApiModelProperty(value = "弹簧尺寸1三次")
    private Double springDimension1Three;

    @Column(name = "spring_dimension2_one")
    @ApiModelProperty(value = "弹簧尺寸2一次")
    private Double springDimension2One;

    @Column(name = "spring_dimension2_two")
    @ApiModelProperty(value = "弹簧尺寸2二次")
    private Double springDimension2Two;

    @Column(name = "spring_dimension2_three")
    @ApiModelProperty(value = "弹簧尺寸2三次")
    private Double springDimension2Three;

    @Column(name = "spring_dimension3_one")
    @ApiModelProperty(value = "弹簧尺寸3一次")
    private Double springDimension3One;

    @Column(name = "spring_dimension3_two")
    @ApiModelProperty(value = "弹簧尺寸3二次")
    private Double springDimension3Two;

    @Column(name = "spring_dimension3_three")
    @ApiModelProperty(value = "弹簧尺寸3三次")
    private Double springDimension3Three;

    @Column(name = "relief_pressure")
    @ApiModelProperty(value = "泄压压力")
    private Double reliefPressure;

    @Column(name = "end_paper_thickness")
    @ApiModelProperty(value = "端板碳纸厚度")
    private Double endPaperThickness;

    @Column(name = "assembly_personnel")
    @ApiModelProperty(value = "组堆人员")
    private String assemblyPersonnel;

    @Column(name = "assembly_date")
    @ApiModelProperty(value = "组堆日期")
    private Date assemblyDate;

    @Column(name = "water_hydrogen_one")
    @ApiModelProperty(value = "水串氢一次实际数据")
    private String waterHydrogenOne;

    @Column(name = "water_hydrogen_two")
    @ApiModelProperty(value = "水串氢二次实际数据")
    private String waterHydrogenTwo;

    @Column(name = "water_oxygen_one")
    @ApiModelProperty(value = "水串氧一次实际数据")
    private String waterOxygenOne;

    @Column(name = "water_oxygen_two")
    @ApiModelProperty(value = "水串氧二次实际数据")
    private String waterOxygenTwo;

    @Column(name = "hydrogen_water_one")
    @ApiModelProperty(value = "氢串水一次实际数据")
    private String hydrogenWaterOne;

    @Column(name = "hydrogen_water_two")
    @ApiModelProperty(value = "氢串水二次实际数据")
    private String hydrogenWaterTwo;

    @Column(name = "hydrogen_oxygen_mix_one")
    @ApiModelProperty(value = "氢氧互串一次实际数据")
    private String hydrogenOxygenMixOne;

    @Column(name = "hydrogen_oxygen_mix_two")
    @ApiModelProperty(value = "氢氧互串二次实际数据")
    private String hydrogenOxygenMixTwo;

    @Column(name = "oxygen_water_one")
    @ApiModelProperty(value = "氧串水一次实际数据")
    private String oxygenWaterOne;

    @Column(name = "oxygen_water_two")
    @ApiModelProperty(value = "氧串水二次实际数据")
    private String oxygenWaterTwo;

    @Column(name = "oxygen_hydrogen_mix_onw")
    @ApiModelProperty(value = "氧氢互串一次实际数据")
    private String oxygenHydrogenMixOne;

    @Column(name = "oxygen_hydrogen_mix_two")
    @ApiModelProperty(value = "氧氢互串二次实际数据")
    private String oxygenHydrogenMixTwo;

    @Column(name = "hydrogen_oxygen_water_one")
    @ApiModelProperty(value = "氢氧串水一次数据数据")
    private String hydrogenOxygenWaterOne;

    @Column(name = "hydrogen_oxygen_water_two")
    @ApiModelProperty(value = "氢氧串水二次实际数据")
    private String hydrogenOxygenWaterTwo;

    @Column(name = "leakage_detection_one")
    @ApiModelProperty(value = "外露检测一次实际数据")
    private String leakageDetectionOne;

    @Column(name = "leakage_detection_two")
    @ApiModelProperty(value = "外露检测二次实际数据")
    private String leakageDetectionTwo;

    @Column(name = "first_total_voltage_current_density600")
    @ApiModelProperty(value = "第一次活化总电压600电密")
    private String firstTotalVoltageCurrentDensity600;

    @Column(name = "first_total_voltage_current_density700")
    @ApiModelProperty(value = "第一次活化总电压700电密")
    private String firstTotalVoltageCurrentDensity700;

    @Column(name = "first_total_voltage_current_density800")
    @ApiModelProperty(value = "第一次活化总电压800电密")
    private String firstTotalVoltageCurrentDensity800;

    @Column(name = "first_average_voltage_current_density600")
    @ApiModelProperty(value = "第一次活化平均电压600电密")
    private String firstAverageVoltageCurrentDensity600;

    @Column(name = "first_average_voltage_current_density700")
    @ApiModelProperty(value = "第一次活化平均电压700电密")
    private String firstAverageVoltageCurrentDensity700;

    @Column(name = "first_average_voltage_current_density800")
    @ApiModelProperty(value = "第一次活化平均电压800电密")
    private String firstAverageVoltageCurrentDensity800;

    @Column(name = "first_head_voltage_current_density600")
    @ApiModelProperty(value = "第一次活化首节电压600电密")
    private String firstHeadVoltageCurrentDensity600;

    @Column(name = "first_head_voltage_current_density700")
    @ApiModelProperty(value = "第一次活化首节电压700电密")
    private String firstHeadVoltageCurrentDensity700;

    @Column(name = "first_head_voltage_current_density800")
    @ApiModelProperty(value = "第一次活化首节电压800电密")
    private String firstHeadVoltageCurrentDensity800;

    @Column(name = "first_terminal_voltage_current_density600")
    @ApiModelProperty(value = "第一次活化末节电压600电密")
    private String firstTerminalVoltageCurrentDensity600;

    @Column(name = "first_terminal_voltage_current_density700")
    @ApiModelProperty(value = "第一次活化末节电压700电密")
    private String firstTerminalVoltageCurrentDensity700;

    @Column(name = "first_terminal_voltage_current_density800")
    @ApiModelProperty(value = "第一次活化末节电压800电密")
    private String firstTerminalVoltageCurrentDensity800;

    @Column(name = "second_total_voltage_current_density600")
    @ApiModelProperty(value = "第二次活化总电压600电密")
    private String secondTotalVoltageCurrentDensity600;

    @Column(name = "second_total_voltage_current_density700")
    @ApiModelProperty(value = "第二次活化总电压700电密")
    private String secondTotalVoltageCurrentDensity700;

    @Column(name = "second_total_voltage_current_density800")
    @ApiModelProperty(value = "第二次活化总电压800电密")
    private String secondTotalVoltageCurrentDensity800;

    @Column(name = "second_average_voltage_current_density600")
    @ApiModelProperty(value = "第二次活化平均电压600电密")
    private String secondAverageVoltageCurrentDensity600;

    @Column(name = "second_average_voltage_current_density700")
    @ApiModelProperty(value = "第二次活化平均电压700电密")
    private String secondAverageVoltageCurrentDensity700;

    @Column(name = "second_average_voltage_current_density800")
    @ApiModelProperty(value = "第二次活化平均电压800电密")
    private String secondAverageVoltageCurrentDensity800;

    @Column(name = "second_head_voltage_current_density600")
    @ApiModelProperty(value = "第二次活化首节电压600电密")
    private String secondHeadVoltageCurrentDensity600;

    @Column(name = "second_head_voltage_current_density700")
    @ApiModelProperty(value = "第二次活化首节电压700电密")
    private String secondHeadVoltageCurrentDensity700;

    @Column(name = "second_head_voltage_current_density800")
    @ApiModelProperty(value = "第二次活化首节电压800电密")
    private String secondHeadVoltageCurrentDensity800;

    @Column(name = "second_terminal_voltage_current_density600")
    @ApiModelProperty(value = "第二次活化末节电压600电密")
    private String secondTerminalVoltageCurrentDensity600;

    @Column(name = "second_terminal_voltage_current_density700")
    @ApiModelProperty(value = "第二次活化末节电压700电密")
    private String secondTerminalVoltageCurrentDensity700;

    @Column(name = "second_terminal_voltage_current_density800")
    @ApiModelProperty(value = "第二次活化末节电压800电密")
    private String secondTerminalVoltageCurrentDensity800;

    @Column(name = "third_total_voltage_current_density600")
    @ApiModelProperty(value = "第三次活化总电压600电密")
    private String thirdTotalVoltageCurrentDensity600;

    @Column(name = "third_total_voltage_current_density700")
    @ApiModelProperty(value = "第三次活化总电压700电密")
    private String thirdTotalVoltageCurrentDensity700;

    @Column(name = "third_total_voltage_current_density800")
    @ApiModelProperty(value = "第三次活化总电压800电密")
    private String thirdTotalVoltageCurrentDensity800;

    @Column(name = "third_average_voltage_current_density600")
    @ApiModelProperty(value = "第三次活化平均电压600电密")
    private String thirdAverageVoltageCurrentDensity600;

    @Column(name = "third_average_voltage_current_density700")
    @ApiModelProperty(value = "第三次活化平均电压700电密")
    private String thirdAverageVoltageCurrentDensity700;

    @Column(name = "third_average_voltage_current_density800")
    @ApiModelProperty(value = "第三次活化平均电压800电密")
    private String thirdAverageVoltageCurrentDensity800;

    @Column(name = "third_head_voltage_current_density600")
    @ApiModelProperty(value = "第三次活化首节电压600电密")
    private String thirdHeadVoltageCurrentDensity600;

    @Column(name = "third_head_voltage_current_density700")
    @ApiModelProperty(value = "第三次活化首节电压700电密")
    private String thirdHeadVoltageCurrentDensity700;

    @Column(name = "third_head_voltage_current_density800")
    @ApiModelProperty(value = "第三次活化首节电压800电密")
    private String thirdHeadVoltageCurrentDensity800;

    @Column(name = "third_terminal_voltage_current_density600")
    @ApiModelProperty(value = "第三次活化末节电压600电密")
    private String thirdTerminalVoltageCurrentDensity600;

    @Column(name = "third_terminal_voltage_current_density700")
    @ApiModelProperty(value = "第三次活化末节电压700电密")
    private String thirdTerminalVoltageCurrentDensity700;

    @Column(name = "third_terminal_voltage_current_density800")
    @ApiModelProperty(value = "第三次活化末节电压800电密")
    private String thirdTerminalVoltageCurrentDensity800;

    public void copy(ManufactureOrder source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(false));
    }
}
