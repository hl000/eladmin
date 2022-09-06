package me.zhengjie.service;

import me.zhengjie.domain.FuelCellComponents;
import me.zhengjie.service.dto.FuelCellComponentsQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author HL
 * @create 2022/7/5 14:19
 */
public interface FuelCellComponentsService {
    Object create(FuelCellComponents resources);

    Object update(FuelCellComponents resources);

    Map<String, Object> queryAll(FuelCellComponentsQueryCriteria criteria, Pageable pageable);

    List<FuelCellComponents> queryAll(FuelCellComponentsQueryCriteria criteria);

    void downloadPartNumber(HttpServletResponse response, FuelCellComponentsQueryCriteria criteria);
}
