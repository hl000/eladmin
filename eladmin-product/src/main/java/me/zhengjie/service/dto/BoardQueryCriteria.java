package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
 * @author HL
 * @create 2021/6/15 7:51
 */
@Data
public class BoardQueryCriteria {
    @Query
    private String secondaryType;

    @Query
    private String manufactureAddress;

    @Query
    private String manufactureName;
}
