package me.zhengjie.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author HL
 * @create 2022/11/25 15:08
 */
@Data
public class PurchaseDetail {
    private Integer id;

    private String cCode;

    private String cInvCode;

    private String cInvName;

    private String cInvStd;

    private Integer iQuantity;

    private String cOrderCode;

    private String cDefine8;

    private Long autoId;

    private String status;

    private String userId;
}
