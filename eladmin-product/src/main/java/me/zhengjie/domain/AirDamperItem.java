package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author HL
 * @create 2022/8/31 14:10
 */
@Entity
@Data
@Table(name = "air_damper_item")
public class AirDamperItem {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键")
    private Integer id;

    @Column(name = "air_damper_code")
    @ApiModelProperty(value = "系统节气门编号")
    private String airDamperCode;

    @Column(name = "create_date")
    @ApiModelProperty(value = "创建时间")
    @CreationTimestamp
    private Timestamp createDate;

    @Column(name = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    @Column(name = "is_active")
    @ApiModelProperty(value = "是否启用")
    private Integer isActive;

    @JoinColumn(name = "fuel_cell_components_id")
    @ManyToOne(fetch=FetchType.LAZY)
    @ApiModelProperty(value = "系统", hidden = true)
    private FuelCellComponents fuelCellComponents;

}

