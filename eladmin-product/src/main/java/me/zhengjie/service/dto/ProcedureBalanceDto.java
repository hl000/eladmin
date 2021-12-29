package me.zhengjie.service.dto;

import lombok.Data;

/**
 * @author HL
 * @create 2021/12/21 15:01
 */
@Data
public class ProcedureBalanceDto {

    private String arcCode;

    private String arcName;

    private String invCode;

    private String invName;

    private Integer balanceQty;
}
