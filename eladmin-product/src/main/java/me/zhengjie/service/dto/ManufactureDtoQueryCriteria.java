package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author HL
 * @create 2021/4/20 14:16
 */
@Data
public class ManufactureDtoQueryCriteria {
    @Query
    private String planNumber;

    @Query
    private Long userId;

    @Query
    private String manufactureName;

//    @Query(type = Query.Type.BETWEEN)
//    private List<Timestamp> fillDate;
    
    @Query
    private String manufactureAddress;

    @Query
    private String processName;

    @Query
    private Integer serialNumber;

}
