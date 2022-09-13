package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author HL
 * @create 2022/9/13 17:44
 */
@Entity
@Data
@Table(name = "work_group")
public class WorkGroup {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键")
    private Integer id;

    @Column(name = "group_code")
    @ApiModelProperty(value = "组编码")
    private String groupCode;

    @Column(name = "group_number")
    @ApiModelProperty(value = "组成员")
    private String groupNumber;

    @Column(name = "group_name")
    @ApiModelProperty(value = "组名称")
    private String groupName;

    @Column(name = "is_active")
    @ApiModelProperty(value = "是否启用")
    private Integer isActive = 1;

    @Column(name = "create_date")
    @ApiModelProperty(value = "创建时间")
    @CreationTimestamp
    private Timestamp createDate;

    @Column(name = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;


}
