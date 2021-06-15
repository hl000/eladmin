package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author HL
 * @create 2021/6/2 22:56
 */
@Entity
@Data
@Table(name = "report_balance_log")
public class BalanceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "主键id")
    private Integer id;

    @JoinColumn(name = "report_balance_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Balance balance;

    @JoinColumn(name = "manufacture")
    @ApiModelProperty(value = "报工")
    private String manufacture;

    @JoinColumn(name = "operate")
    @ApiModelProperty(value = "报工操作")
    private String operate;

    @JoinColumn(name = "created_time")
    @ApiModelProperty(value = "创建时间")
    @CreationTimestamp
    private Timestamp createdTime;

    @JoinColumn(name = "user_id")
    @ApiModelProperty(value = "操作用户")
    private Long userId;

    @JoinColumn(name = "balance_quantity")
    @ApiModelProperty(value = "库存数量")
    private Integer balanceQuantity;

    @JoinColumn(name = "original_quantity")
    @ApiModelProperty(value = "库存数量")
    private Integer originalQuantity;
}
