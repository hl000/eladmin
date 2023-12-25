package me.zhengjie.service.dto;

import lombok.Data;

import java.util.List;

/**
 * @author HL
 * @create 2023/12/14 9:56
 */
@Data
public class UserMatchDto {

    private Integer id;

    private String department;

    private List<String> leader;

    private List<String> processList;

}
