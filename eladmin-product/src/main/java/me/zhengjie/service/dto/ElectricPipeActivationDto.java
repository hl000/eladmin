package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.domain.ElectricPipeActivation;
import me.zhengjie.domain.ElectricPipeActivationDetail;

import java.util.List;

/**
 * @author HL
 * @create 2022/9/5 21:29
 */
@Data
public class ElectricPipeActivationDto {
    private ElectricPipeActivation electricPipeActivation;
    private List<ElectricPipeActivationDetail> electricPipeActivationDetailList;
}
