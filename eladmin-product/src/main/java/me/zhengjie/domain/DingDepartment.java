package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

/**
 * @author HL
 * @create 2022/3/15 15:10
 */
@Entity
@Data
@Table(name = "sys_dingding_department")
public class DingDepartment {

    @Id
    @Column(name = "department_id")
    @ApiModelProperty(value = "ID")
    private Long deptId;

    @Column(name = "department_name")
    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DingDepartment that = (DingDepartment) o;
        return Objects.equals(deptId, that.deptId) && Objects.equals(deptName, that.deptName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deptId, deptName);
    }
}
