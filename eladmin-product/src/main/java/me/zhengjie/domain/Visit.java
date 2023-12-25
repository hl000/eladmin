package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author HL
 * @create 2023/3/28 11:55
 */
@Entity
@Data
@Table(name = "visit")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Integer id;

    @Column(name = "name")
    @ApiModelProperty(value = "姓名")
    private String name;

    @Column(name = "organization")
    @ApiModelProperty(value = "单位")
    private String organization;

    @Column(name = "phone")
    @ApiModelProperty(value = "电话")
    private String phone;

    @Column(name = "visit_reason")
    @ApiModelProperty(value = "到访原因")
    private String visitReason;

    @Column(name = "receptionist")
    @ApiModelProperty(value = "接待人员")
    private String receptionist;

    @Column(name = "peer_quantity")
    @ApiModelProperty(value = "同行人数")
    private Integer peerQuantity;

    @Column(name = "Licence_Plate")
    @ApiModelProperty(value = "车牌号")
    private String licencePlate;


    @Column(name = "create_time")
    @ApiModelProperty(value = "到访时间")
    @CreationTimestamp
    private Timestamp createTime;

}
