package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * @author HL
 * @create 2021/5/29 9:02
 */
@Entity
@Data
@Table(name = "report_material_ratio")
public class MaterialRatio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "主键id")
    private Integer id;

    @JoinColumn(name = "report_product_parameter_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ProductParameter productParameter;

    @JoinColumn(name = "material_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ProductParameter material;

    @Column(name = "consumed_quantity")
    @ApiModelProperty(value = "单件消耗")
    private Integer consumedQuantity;
}
