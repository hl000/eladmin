package me.zhengjie.service;

import me.zhengjie.service.dto.ManufactureDtoQueryCriteria;
import me.zhengjie.service.dto.OutputDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author HL
 * @create 2021/5/26 14:20
 */
public interface OutputService {
    List<OutputDto> queryManufacture(ManufactureDtoQueryCriteria criteria,Timestamp startDate,Timestamp endDate);

    void download(List<OutputDto> outputDtoList, Timestamp startDate, Timestamp endDate, HttpServletResponse response);
}
