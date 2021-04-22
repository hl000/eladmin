package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.domain.TechniqueInfo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author HL
 * @create 2021/4/14 9:25
 */

@Data
public class BatchPlanDto implements Serializable {

    private Integer id;

    /**生产批次号**/
    private String batchNumber;

//    private String productCode;

    private Timestamp startDate;

    private Timestamp endDate;

    private Integer batchPlanQuantity;

//    private Integer techniqueInfoId;

    private TechniqueInfo techniqueInfo;

    private Long userId;

}
