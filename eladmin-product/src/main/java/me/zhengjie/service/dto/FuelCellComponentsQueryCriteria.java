package me.zhengjie.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.annotation.Query;

import javax.persistence.JoinColumn;

/**
 * @author HL
 * @create 2022/7/5 14:27
 */
@Data
public class FuelCellComponentsQueryCriteria {
    @Query
    private String bracketCode;

    @Query
    private String electricPileCode;

    @Query
    private String compressorCode;

    @Query
    private String compressorctlCode;

    @Query
    private String hydrogenPumpCode;

    @Query
    private String pumpctlCode;


    @Query
    private String hydrogenInjectorOne;

    @Query
    private String hydrogenInjectorTwo;


    @Query
    private String hydrogenInjectorThree;

    @Query
    private String airDamperOne;

    @Query
    private String airDamperTwo;

    @Query
    private String pilectlCode;

    @Query
    private String voltagectlOne;

    @Query
    private String voltagectlTwo;

    @Query
    private String ptcCode;

    @Query
    private String ptcRadiotube;

    @Query
    private String threeWayValve;

    @Query
    private String dcdcCode;

    @Query
    private String productCode;

    @Query
    private String humidifierCode;

    @Query
    private String waterPump;

}
