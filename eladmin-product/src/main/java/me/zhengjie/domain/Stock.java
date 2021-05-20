package me.zhengjie.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author HL
 * @create 2021/5/19 13:49
 */
@Entity
@Data
@Table(name="report_stock")
public class Stock implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id")
    private Integer id;

    @Column(name = "manufacture_address")
    @ApiModelProperty(value = "生产基地")
    private String manufactureAddress;

    @Column(name = "process_name")
    @ApiModelProperty(value = "工序名称")
    private String processName;

    @Column(name = "manufacture_name")
    @ApiModelProperty(value = "工序半成品名称")
    private String manufactureName;

    @Column(name = "quantity")
    @ApiModelProperty(value = "数量")
    private Integer quantity;

    /**修改时间**/
    @Column(name = "create_time")
    @ApiModelProperty(value = "创建时间")
    @CreationTimestamp
    private Timestamp createTime;

    /**修改时间**/
    @Column(name = "update_time")
    @ApiModelProperty(value = "修改时间")
    @UpdateTimestamp
    private Timestamp updateTime;


    public void copy(Stock source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
