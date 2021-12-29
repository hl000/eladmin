package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * @author HL
 * @create 2021/12/21 14:38
 */
@Data
//@Entity
//@Table(name = "mwork_step_adjust_quantity")
public class WorkStepAdjust {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "ID")
//    @ApiModelProperty(value = "ID")
    private Integer id;

//    @Column(name = "_arc_code")
//    @ApiModelProperty(value = "工序编码")
    private String arcCode;
//
//    @Column(name = "_inv_code")
//    @ApiModelProperty(value = "存货编码")
    private String invCode;

//    @Column(name = "_adjust_Qty")
//    @ApiModelProperty(value = "调整数量")
    private Integer adjustQty;

}
