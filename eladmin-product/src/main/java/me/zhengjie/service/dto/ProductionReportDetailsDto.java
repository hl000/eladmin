package me.zhengjie.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * @author HL
 * @create 2021/12/6 20:00
 */
@Data
public class ProductionReportDetailsDto implements Serializable {

    private String vSubmitDate;

    private String vArcName;

    private String vSpec;

    private String vPerName;

    private String vOrderNo;

    private String vDocNo;

    private String vInvCode;

    private String vInvName;

    private String vArcCode;

    private String vDevice;

    private Double vOrderQty;

    private String vUnutTime;

    private Double vWorkTime;

    private Double vActualHour;

    private String vPackageNo;

    private Double vCompleteQty;

    private Double vUnQuaQty;
}
