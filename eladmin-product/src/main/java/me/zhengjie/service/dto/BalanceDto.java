package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.domain.Category;
import me.zhengjie.domain.ProductParameter;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author HL
 * @create 2021/5/29 10:58
 */
@Data
public class BalanceDto implements Serializable {

    private Integer id;

    private String manufactureAddress;

    private ProductParameter productParameter;

    private Category category;

    private Integer remainQuantity;

    private Timestamp updateTime;
}
