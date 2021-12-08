package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author HL
 * @create 2021/9/7 15:00
 */
@Data
public class Maintenance4CarItem {

    //    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    @ApiModelProperty(value = "主键id")
    private Integer id;

    //    @Column(name = "Fconductance")
//    @ApiModelProperty(value = "电导率")
    private Integer Fconductance;

    //    @Column(name = "FitemCheck1")
//    @ApiModelProperty(value = "物理过滤器检查")
    private Boolean FitemCheck1;

    //    @Column(name = "FitemCheck2")
//    @ApiModelProperty(value = "离子过滤器检查")
    private Boolean FitemCheck2;

    //    @Column(name = "Fitem2ChangePart")
//    @ApiModelProperty(value = "离子过滤器更换")
    private String Fitem2ChangePart;

    //    @Column(name = "Fitem2PartCode")
//    @ApiModelProperty(value = "离子过滤器编码")
    private String Fitem2PartCode;

    //    @Column(name = "Fitem3ChangePart")
//    @ApiModelProperty(value = "空气滤芯检查")
    private Boolean FitemCheck3;

    //    @Column(name = "Fitem3PartCode")
//    @ApiModelProperty(value = "空气滤芯更换")
    private String Fitem3ChangePart;

    //    @Column(name = "FitemCheck4")
//    @ApiModelProperty(value = "空气泵检查")
    private Boolean FitemCheck4;

    //    @Column(name = "FitemCheck5")
//    @ApiModelProperty(value = "消音器检查")
    private Boolean FitemCheck5;

    //    @Column(name = "FitemCheck6")
//    @ApiModelProperty(value = "氢气检查")
    private Boolean FitemCheck6;

    //    @Column(name = "FitemCheck7")
//    @ApiModelProperty(value = "循环泵检查")
    private Boolean FitemCheck7;

    //    @Column(name = "FitemCheck8")
//    @ApiModelProperty(value = "尾排检查")
    private Boolean FitemCheck8;

    //    @Column(name = "FitemCheck9")
//    @ApiModelProperty(value = "软管及接头检查")
    private Boolean FitemCheck9;

    //    @Column(name = "FitemCheck10")
//    @ApiModelProperty(value = "传感器检查")
    private Boolean FitemCheck10;

    //    @Column(name = "FitemCheck11")
//    @ApiModelProperty(value = "系统检查")
    private Boolean FitemCheck11;

    //    @Column(name = "FitemCheck12")
//    @ApiModelProperty(value = "氢气测漏检查")
    private Boolean FitemCheck12;

    //    @Column(name = "objectid")
//    @ApiModelProperty(value = "氚云id")
    private String objectid;

    //    @Column(name = "parentobjectid")
//    @ApiModelProperty(value = "氚云父id")
    private String parentobjectid;

    //    @Column(name = "FDate")
//    @ApiModelProperty(value = "维护日期")
    private String FDate;

    //    @Column(name = "FplateNo")
//    @ApiModelProperty(value = "车牌号")
    private String FplateNo;

    //    @Column(name = "Frange")
//    @ApiModelProperty(value = "行驶里程")
    private Float Frange;

    private String Fremark;


}
