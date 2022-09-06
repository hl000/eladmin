package me.zhengjie.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author HL
 * @create 2021/12/8 15:58
 */
@Entity
@Data
@Table(name = "fuel_cell_components")
public class FuelCellComponents {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键")
    private Integer id;

    @Column(name = "bracket_code")
    @ApiModelProperty(value = "系统支架编号")
    private String bracketCode;

    @Column(name = "order_code")
    @ApiModelProperty(value = "生产工单号")
    private String orderCode;

    @Column(name = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    @Column(name = "electric_pile_code")
    @ApiModelProperty(value = "系统电堆编号")
    private String electricPileCode;

    @Column(name = "compressor_code")
    @ApiModelProperty(value = "系统压缩机编号")
    private String compressorCode;

    @Column(name = "compressorctl_code")
    @ApiModelProperty(value = "压缩机控制器编号")
    private String compressorctlCode;

    @Column(name = "hydrogen_pump_code")
    @ApiModelProperty(value = "系统氢循环泵编号")
    private String hydrogenPumpCode;

    @Column(name = "pumpctl_code")
    @ApiModelProperty(value = "氢循环泵控制器编号")
    private String pumpctlCode;


//    @JoinColumn(name = "hydrogen_injector_one")
//    @ApiModelProperty(value = "系统氢喷编号①")
//    private String hydrogenInjectorOne;
//
//    @JoinColumn(name = "hydrogen_injector_two")
//    @ApiModelProperty(value = "系统氢喷编号②")
//    private String hydrogenInjectorTwo;
//
//
//    @JoinColumn(name = "hydrogen_injector_three")
//    @ApiModelProperty(value = "系统氢喷编号③")
//    private String hydrogenInjectorThree;


    @OneToMany(mappedBy = "fuelCellComponents", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<HydrogenInjectorItem> hydrogenInjectorItems;

    @OneToMany(mappedBy = "fuelCellComponents", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<AirDamperItem> airDamperItems;

    @OneToMany(mappedBy = "fuelCellComponents", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<VoltagectlItem> voltagectlItems;

//    @JoinColumn(name = "air_damper_one")
//    @ApiModelProperty(value = "系统节气门①")
//    private String airDamperOne;
//
//    @JoinColumn(name = "air_damper_two")
//    @ApiModelProperty(value = "系统节气门②")
//    private String airDamperTwo;

    @Column(name = "pilectl_code")
    @ApiModelProperty(value = "系统电池堆控制器")
    private String pilectlCode;

//    @JoinColumn(name = "voltagectl_one")
//    @ApiModelProperty(value = "系统电压控制器①")
//    private String voltagectlOne;
//
//    @JoinColumn(name = "voltagectl_two")
//    @ApiModelProperty(value = "系统电压控制器②")
//    private String voltagectlTwo;

    @Column(name = "ptc_code")
    @ApiModelProperty(value = "PTC加热模块编号")
    private String ptcCode;

    @Column(name = "ptc_radiotube")
    @ApiModelProperty(value = "加热尾排电磁阀编号")
    private String ptcRadiotube;

    @Column(name = "three_way_valve")
    @ApiModelProperty(value = "三通阀编号")
    private String threeWayValve;

    @Column(name = "dcdc_code")
    @ApiModelProperty(value = "DCDC编号")
    private String dcdcCode;

    @Column(name = "product_code")
    @ApiModelProperty(value = "生产编码")
    private String productCode;

    @Column(name = "humidifier_code")
    @ApiModelProperty(value = "增湿器")
    private String humidifierCode;

    @Column(name = "water_pump")
    @ApiModelProperty(value = "水泵")
    private String waterPump;

    @Column(name = "create_date")
    @ApiModelProperty(value = "创建时间")
    @CreationTimestamp
    private Timestamp createDate;

    public void copy(FuelCellComponents source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
