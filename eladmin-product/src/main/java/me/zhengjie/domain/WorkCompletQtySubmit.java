package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author HL
 * @create 2021/12/29 11:33
 */
@Entity
@Data
@Table(name = "work_complet_qty_submit")
public class WorkCompletQtySubmit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @ApiModelProperty(value = "ID")
    private Integer id;

    @Column(name = "FSub_Date")
    @ApiModelProperty(value = "提交日期")
    private String fSubDate;

//    @Column(name = "FArc_Name")
//    @ApiModelProperty(value = "工序名称")
//    private String fArcName;

    @JoinColumn(name = "FArc_ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private WorkWorkingProcedure workWorkingProcedure;

    @Column(name = "FWork_Order")
    @ApiModelProperty(value = "工单")
    private String fWorkOrder;

    @Column(name = "FInv_Code")
    @ApiModelProperty(value = "存货编码")
    private String fInvCode;

    @Column(name = "FInv_Name")
    @ApiModelProperty(value = "存货名称")
    private String fInvName;

    @Column(name = "FInv_Std")
    @ApiModelProperty(value = "存货规格")
    private String fInvStd;

    @JoinColumn(name = "FComplete_Time_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private WorkSubmitTimeList workSubmitTimeList;

//    @Column(name = "FComplete_Time")
//    @ApiModelProperty(value = "报工时间")
//    private String fCompleteTime;

    @Column(name = "FComplete_Qty")
    @ApiModelProperty(value = "报工数量")
    private Integer fCompleteQty;

    @Column(name = "FCreate_Date")
    @ApiModelProperty(value = "创建时间")
    @CreationTimestamp
    private Timestamp fCreateDate;

    @Column(name = "FIs_Deleted")
    @ApiModelProperty(value = "是否删除")
    private Integer fIsDeleted = 0;


}
