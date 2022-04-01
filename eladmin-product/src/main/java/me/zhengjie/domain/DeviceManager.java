package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author HL
 * @create 2022/3/15 20:50
 */
@Entity
@Data
@Table(name = "device_manager")
public class DeviceManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id")
    @ApiModelProperty(value = "ID")
    private Integer deviceId;

    @Column(name = "device_code")
    @ApiModelProperty(value = "设备编码")
    private String deviceCode;

    @Column(name = "device_site")
    @ApiModelProperty(value = "存放地点")
    private String deviceSite;

    @Column(name = "user_id")
    @ApiModelProperty(value = "使用用户")
    private String userId;

    @Column(name = "asset_code")
    @ApiModelProperty(value = "固定资产编码")
    private String assetCode;

    @Column(name = "lan_address_one")
    @ApiModelProperty(value = "MAC地址1")
    private String lanAddressOne;

    @Column(name = "lan_address_two")
    @ApiModelProperty(value = "MAC地址2")
    private String lanAddressTwo;

    @Column(name = "lan_address_three")
    @ApiModelProperty(value = "MAC地址3")
    private String lanAddressThree;

    @Column(name = "lan_address_four")
    @ApiModelProperty(value = "MAC地址4")
    private String lanAddressFour;

    @Column(name = "device_type_id")
    @ApiModelProperty(value = "设备类型ID")
    private Integer deviceTypeId;

    @Column(name = "purchase_date")
    @ApiModelProperty(value = "购买日期")
    private String purchaseDate;

    @Column(name = "picture_address")
    @ApiModelProperty(value = "资产图片")
    private String pictureAddress;

    @Column(name = "create_date")
    @ApiModelProperty(value = "创建时间")
    @CreationTimestamp
    private Timestamp createDate;

}
