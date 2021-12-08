package me.zhengjie.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author HL
 * @create 2021/12/6 16:17
 */
@Entity
@Data
@Table(name = "production_report_details_view")
public class ProductionReportDetailsView {
    @Id
    @Column(name = "uuid")
    @ApiModelProperty(value = "随机唯一id")
    private String uuid;

    @Column(name = "v_submit_date")
    @ApiModelProperty(value = "报工日期")
    private String vSubmitDate;

    @Column(name = "v_arc_name")
    @ApiModelProperty(value = "工序名称")
    private String vArcName;

    @Column(name = "v_spec")
    @ApiModelProperty(value = "产品规格")
    private String vSpec;

    @Column(name = "v_per_name")
    @ApiModelProperty(value = "报工人")
    private String vPerName;

    @Column(name = "v_order_no")
    @ApiModelProperty(value = "生产订单号")
    private String vOrderNo;

    @Column(name = "v_noc_no")
    @ApiModelProperty(value = "流程卡号")
    private String vDocNo;

    @Column(name = "v_inv_code")
    @ApiModelProperty(value = "产品编码")
    private String vInvCode;

    @Column(name = "v_inv_name")
    @ApiModelProperty(value = "产品名称")
    private String vInvName;

    @Column(name = "v_arc_code")
    @ApiModelProperty(value = "工站")
    private String vArcCode;

    @Column(name = "v_device")
    @ApiModelProperty(value = "设备名称")
    private String vDevice;

    @Column(name = "v_order_qty")
    @ApiModelProperty(value = "任务数量")
    private Double vOrderQty;

    @Column(name = "v_unut_time")
    @ApiModelProperty(value = "工时单位")
    private String vUnutTime;

    @Column(name = "v_work_time")
    @ApiModelProperty(value = "标准工时")
    private Double vWorkTime;

    @Column(name = "v_actual_hour")
    @ApiModelProperty(value = "实际工时")
    private Double vActualHour;

    @Column(name = "v_package_no")
    @ApiModelProperty(value = "生产批号")
    private String vPackageNo;

    @Column(name = "v_complete_qty")
    @ApiModelProperty(value = "报工数量")
    private Double vCompleteQty;

    @Column(name = "v_un_qua_qty")
    @ApiModelProperty(value = "不良数量")
    private Double vUnQuaQty;


}
