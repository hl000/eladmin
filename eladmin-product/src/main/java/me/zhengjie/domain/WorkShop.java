package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author HL
 * @create 2022/1/10 9:06
 */
@Entity
@Data
@Table(name = "work_work_shop")
public class WorkShop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Integer id;

    @Column(name = "FWork_Shop")
    @ApiModelProperty(value = "时间段")
    private String fWorkShop;

    @Column(name = "FArc_ID")
    @ApiModelProperty(value = "时间段")
    private String fArcID;

    @Column(name = "FCreate_Date")
    @ApiModelProperty(value = "创建时间")
    @CreationTimestamp
    private Timestamp dCreateDate;

    @Column(name = "FIs_Deleted")
    @ApiModelProperty(value = "是否删除")
    private Integer fIsDeleted;
}
