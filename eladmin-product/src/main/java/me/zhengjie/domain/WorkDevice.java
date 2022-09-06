package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author HL
 * @create 2022/9/1 19:38
 */
@Entity
@Data
@Table(name = "work_device")
public class WorkDevice {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键")
    private Integer id;

    @Column(name = "device_code")
    @ApiModelProperty(value = "设备编码")
    private String deviceCode;

    @Column(name = "device_name")
    @ApiModelProperty(value = "设备名字")
    private String deviceName;

    @Column(name = "device_type")
    @ApiModelProperty(value = "设备类型")
    private String deviceType;

    @Column(name = "is_active")
    @ApiModelProperty(value = "是否启用")
    private Integer isActive = 1;

    @Column(name = "create_date")
    @ApiModelProperty(value = "创建日期")
    @CreationTimestamp
    private Timestamp createDate;

    @Column(name = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;


}
