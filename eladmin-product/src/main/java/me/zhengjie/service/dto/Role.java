package me.zhengjie.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HL
 * @create 2021/5/17 11:21
 */
@Data
public class Role implements Serializable {

    private Long id;

    private String name;

    private Integer level;

    private String dataScope;
}