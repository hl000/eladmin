package me.zhengjie.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.util.Map;

/**
 * @author HL
 * @create 2022/1/4 10:03
 */
@Data
public class WorkListDto {
    private String fSubDate;

//    private String fWorkShop;

    private String fArcName;

    private String fWorkOrder;

    private String fInvCode;

    private String fInvName;

    private String fInvStd;

    private Map<String, Integer> map;

    private Integer fCompleteTime9;

    private Integer fCompleteTime10;

    private Integer fCompleteTime11;

    private Integer fCompleteTime12;

    private Integer fCompleteTime14;

    private Integer fCompleteTime15;

    private Integer fCompleteTime16;

    private Integer fCompleteTime17;

    private Integer fCompleteTime19;

    private Integer total;
}
