package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author HL
 * @create 2022/3/15 15:12
 */
@Entity
@Data
@Table(name = "sys_dingding_user")
public class DingUser {

    @Id
    @Column(name = "user_id")
    @ApiModelProperty(value = "ID")
    private String userid;

    @Column(name = "user_name")
    @ApiModelProperty(value = "用户名")
    private String name;

    @Column(name = "user_status")
    @ApiModelProperty(value = "在职状态")
    private Boolean userStatus;

    @Column(name = "department_id")
    @ApiModelProperty(value = "部门Id")
    private Long deptId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DingUser dingUser = (DingUser) o;
        return Objects.equals(userid, dingUser.userid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userid, name, userStatus, deptId);
    }

    //
//    @JoinColumn(name = "department_id")
//    @ManyToOne(fetch = FetchType.EAGER)
//    private DingDepartment dingDepartment;
}
