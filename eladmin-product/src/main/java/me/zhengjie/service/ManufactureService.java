package me.zhengjie.service;

import me.zhengjie.domain.Manufacture;
import me.zhengjie.service.dto.ManufactureDto;
import me.zhengjie.service.dto.ManufactureQueryCriteria;
import me.zhengjie.service.dto.ManufactureSummaryQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author HL
 * @create 2021/4/13 18:23
 */
public interface ManufactureService {

    /**
     * 创建
     * @param resources /
     * @return ManufactureDto
     */
    ManufactureDto create(Manufacture resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(Manufacture resources);

    Map<String,Object> queryManufacture(ManufactureQueryCriteria criteria, Pageable pageable);

    Map<String,Object> queryManufactureSummary(ManufactureSummaryQueryCriteria criteria, Pageable pageable);

    void summary(String planNumber);

    void queryManufacture(HttpServletResponse response, ManufactureQueryCriteria criteria);

    void queryManufactureSummary(HttpServletResponse response, ManufactureQueryCriteria criteria);
}
