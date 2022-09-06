package me.zhengjie.service.dto;

import lombok.Data;

import java.sql.Date;

/**
 * @author HL
 * @create 2022/6/29 10:31
 */
@Data
public class VehicleMileageDto {

    //    @Column(name = "Licence_Plate")
//    @ApiModelProperty(value = "车牌号")
    private String licencePlate;

    //    @Column(name = "Daily_Mileage")
//    @ApiModelProperty(value = "当日里程")
    private double dailyMileage;

    //    @Column(name = "Total_Mileage")
//    @ApiModelProperty(value = "总里程")
    private double totalMileage;

    //    @Column(name = "Drive_Date")
//    @ApiModelProperty(value = "日期")
    private String driveDate;

    private String FYBIANHAO;

    private String FGBIANHAO;

    private boolean updateStack;

}
