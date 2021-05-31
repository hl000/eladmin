package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author HL
 * @create 2021/5/28 9:06
 */
@Entity
@Data
@Table(name = "report_balance")
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @JoinColumn(name = "update_time")
    @ApiModelProperty(value = "更新时间")
    @UpdateTimestamp
    private Timestamp updateTime;
}
