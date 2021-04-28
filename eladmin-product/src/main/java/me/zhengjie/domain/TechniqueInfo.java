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
import java.util.Date;

/**
/**
 * @author HL
 * @create 2021/4/13 12:17
 */
@Entity
@Data
@Table(name="report_technique_info")
public class TechniqueInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Integer id;

//    @Column(name = "category_id")
//    @ApiModelProperty(value = "分类id")
//    private String categoryId;

    @JoinColumn(name = "category_id")
    @ManyToOne(fetch=FetchType.EAGER)
    private Category category;

    @Column(name = "workpiece_number")
    @ApiModelProperty(value = "工序代号")
    private String workpieceNumber;

    @Column(name = "product_code",unique = true)
    @ApiModelProperty(value = "产品编号")
    private String productCode;

    @Column(name = "instructor_number")
    @ApiModelProperty(value = "工艺指导书编号")
    private String instructorNumber;

    @Column(name = "instructor_version")
    @ApiModelProperty(value = "工艺指导书版本")
    private String instructorVersion;

    @Column(name = "technologist")
    @ApiModelProperty(value = "工艺人员")
    private String technologist;

    @Column(name = "process_changer")
    @ApiModelProperty(value = "工艺变更人")
    private String processChanger;

    @Column(name = "change_date")
    @UpdateTimestamp
    @ApiModelProperty(value = "工艺变更日期")
    private Timestamp changeDate;

    @Column(name = "equipment_max_capacity")
    @ApiModelProperty(value = "设备最大制造能力（小时）")
    private Double equipmentMaxCapacity;

    @Column(name = "current_max_capacity")
    @ApiModelProperty(value = "当前最大产能( 小时）")
    private Double currentMaxCapacity;

    @Column(name = "hour_norm")
    @ApiModelProperty(value = "工时定额")
    private Double hourNorm;

    @Column(name = "material1_quota")
    @ApiModelProperty(value = "材料1定额")
    private Double material1Quota;

    @Column(name = "material2_quota")
    @ApiModelProperty(value = "材料2定额")
    private Double material2Quota;

    @Column(name = "material3_quota")
    @ApiModelProperty(value = "材料3定额")
    private Double material3Quota;

//    @Column(name = "change_content")
//    @ApiModelProperty(value = "工艺变更内容")
//    private String changeContent;

    /**用户Id**/
    @Column(name = "user_id")
    @ApiModelProperty(value = "用户Id")
    private Long userId;

    /**报工名称**/
    @Column(name = "manufacture_name")
    @ApiModelProperty(value = "报工名称")
    private String manufactureName;

    public void copy(TechniqueInfo source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
