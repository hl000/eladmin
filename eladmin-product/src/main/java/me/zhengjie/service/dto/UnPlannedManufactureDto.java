package me.zhengjie.service.dto;

import lombok.Data;
import me.zhengjie.domain.Manufacture;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author HL
 * @create 2021/5/27 14:02
 */
@Data
public class UnPlannedManufactureDto
{

    private String fAddress;

    private Timestamp fDate;

    private Integer fUseId;

    private List<Manufacture> manufactureList;

}
