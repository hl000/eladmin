package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.domain.UpdateStack;

import java.util.List;

/**
 * @author HL
 * @create 2021/5/11 15:42
 */
@Data
public class UpdateStackDto {
    private List<UpdateStack> updateStackList;

    public int totalPages;
    public int currentPage;
    public int size;
    public int totalElements;

    private String stackNumber;
    private String vehicleType;
}
