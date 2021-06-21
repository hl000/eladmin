package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * @author HL
 * @create 2021/6/16 10:26
 */
@Entity
@Data
@Table(name = "view_balance")
public class BalanceView {
    @Id
    @Column(name = "id")
    @ApiModelProperty(value = "主键id")
    private Integer id;

    @Column(name = "manufacture_address")
    @ApiModelProperty(value = "基地")
    private String manufactureAddress;

    @JoinColumn(name = "report_product_parameter_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ProductParameter productParameter;

    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    @JoinColumn(name = "remain_quantity")
    @ApiModelProperty(value = "结存")
    private Integer remainQuantity;
}
