package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author HL
 * @create 2021/6/7 9:37
 */
@Entity
@Data
@Table(name = "Vehicle_Mileage")
public class VehicleMileage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @ApiModelProperty(value = "ID")
    private Integer id;

    @Column(name = "Licence_Plate")
    @ApiModelProperty(value = "车牌号")
    private String licencePlate;

    @Column(name = "Daily_Mileage")
    @ApiModelProperty(value = "当日里程")
    private double dailyMileage;

    @Column(name = "Total_Mileage")
    @ApiModelProperty(value = "总里程")
    private double totalMileage;

    @Column(name = "Drive_Date")
    @ApiModelProperty(value = "日期")
    private Date driveDate;

    @Column(name = "Created_Date")
    @ApiModelProperty(value = "创建时间")
    @CreationTimestamp
    private Timestamp createDate;
}

