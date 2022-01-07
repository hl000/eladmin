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
@Table(name = "work_working_procedure")
public class WorkWorkingProcedure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @ApiModelProperty(value = "ID")
    private Integer id;

    @Column(name = "FArc_Code")
    @ApiModelProperty(value = "工序编码")
    private String fArcCode;

    @Column(name = "FArc_Name")
    @ApiModelProperty(value = "工序名称")
    private String fArcName;

    @Column(name = "FCreate_Date")
    @ApiModelProperty(value = "创建时间")
    @CreationTimestamp
    private Timestamp fCreateDate;

    @Column(name = "FIs_Deleted")
    @ApiModelProperty(value = "是否删除")
    private Integer fIsDeleted;
}
