package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author HL
 * @create 2022/8/31 20:53
 */
@Entity
@Data
@Table(name = "voltagectl_item")
public class VoltagectlItem {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键")
    private Integer id;

    @Column(name = "voltagectl_code")
    @ApiModelProperty(value = "系统电压控制器编号")
    private String voltagectlCode;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @ApiModelProperty(value = "系统", hidden = true)
    private FuelCellComponents fuelCellComponents;
}
