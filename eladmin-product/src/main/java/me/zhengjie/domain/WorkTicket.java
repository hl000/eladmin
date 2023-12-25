package me.zhengjie.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateTime;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author HL
 * @create 2023/12/13 16:01
 */
@Entity
@Data
@Table(name = "work_ticket")
public class WorkTicket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "order_number")
    @ApiModelProperty(value = "单据号")
    private String orderNumber;

    @Column(name = "date")
    @ApiModelProperty(value = "日期")
    private String date;

    @Column(name = "leader")
    @ApiModelProperty(value = "负责人")
    private String leader;

    @Column(name = "department")
    @ApiModelProperty(value = "部门")
    private String department;

    @Column(name = "process")
    @ApiModelProperty(value = "工序")
    private String process;

    @Column(name = "worker")
    @ApiModelProperty(value = "人数")
    private Float worker;

    @Column(name = "output")
    @ApiModelProperty(value = "产量")
    private Float output;

    @Column(name = "rejects_quantity")
    @ApiModelProperty(value = "不良品数量")
    private Float rejectsQuantity;

    @Column(name = "batch_number")
    @ApiModelProperty(value = "批次号")
    private String batchNumber;

    @Column(name = "note")
    @ApiModelProperty(value = "备注")
    private String note;

    @Column(name = "created_by")
    @ApiModelProperty(value = "报工人")
    private String createdBy;

    @Column(name = "created_time")
    @ApiModelProperty(value = "工序")
    @CreationTimestamp
    private Timestamp createdTime;


    @Column(name = "updated_by")
    @ApiModelProperty(value = "更新人")
    private String updatedBy;


    @Column(name = "updated_time")
    @ApiModelProperty(value = "更新时间")
    @UpdateTimestamp
    private Timestamp updatedTime;

    public void copy(WorkTicket source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
