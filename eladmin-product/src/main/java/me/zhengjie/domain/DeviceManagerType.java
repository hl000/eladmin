package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * @author HL
 * @create 2022/3/16 9:42
 */
@Entity
@Data
@Table(name = "device_manager_type")
public class DeviceManagerType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_type_id")
    @ApiModelProperty(value = "ID")
    private Integer deviceTypeId;

    @Column(name = "device_type_name")
    @ApiModelProperty(value = "类型名称")
    private String deviceTypeName;
}
