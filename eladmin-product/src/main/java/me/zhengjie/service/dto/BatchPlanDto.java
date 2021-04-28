package me.zhengjie.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.domain.TechniqueInfo;

import javax.persistence.Column;
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

    private String startDate;

    private String endDate;

    private Integer batchPlanQuantity;

    private Long userId;

    private String productName;

    private String batchPlanName;
}
