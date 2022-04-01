package me.zhengjie.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

/**
 * @author HL
 * @create 2022/3/17 14:10
 */
@Data
public class DingUserDto {

    private String userid;

    private String name;

    private Long deptId;

    private String deptName;
}
