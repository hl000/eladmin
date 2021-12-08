package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author HL
 * @create 2021/9/7 22:19
 */
@Data
public class VehicleInfo {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    @ApiModelProperty(value = "主键id")
    private Integer id;

//    @Column(name = "FplateNo")
//    @ApiModelProperty(value = "车牌号")
    private String FplateNo;

//    @Column(name = "FCEVSys")
//    @ApiModelProperty(value = "系统编号")
    private String FCEVSys;

//    @Column(name = "ElectricPile")
//    @ApiModelProperty(value = "电堆编号")
    private String ElectricPile;

//    @Column(name = "Ftotal4H2")
//    @ApiModelProperty(value = "加氢量")
    private String Ftotal4H2;

//    @Column(name = "Frange")
//    @ApiModelProperty(value = "里程数")
    private String Frange;

//    @Column(name = "Fdate")
//    @ApiModelProperty(value = "上次维护日期")
    private String Fdate;

//    @Column(name = "objectid")
//    @ApiModelProperty(value = "氚云id")
    private String objectid;
}
