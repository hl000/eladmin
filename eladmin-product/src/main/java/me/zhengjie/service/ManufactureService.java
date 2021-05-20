package me.zhengjie.service;

import me.zhengjie.domain.Manufacture;
import me.zhengjie.service.dto.ManufactureDto;
import me.zhengjie.service.dto.ManufactureQueryCriteria;
import me.zhengjie.service.dto.ManufactureSummaryQueryCriteria;
import me.zhengjie.service.dto.SummaryViewQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
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

    void summary(String planNumber) throws ParseException;

    void queryManufacture(HttpServletResponse response, ManufactureQueryCriteria criteria);

    void queryManufactureSummary(HttpServletResponse response, ManufactureQueryCriteria criteria);

    void createManufacture();

//    Map<String,Object> getSummaryView(SummaryViewQueryCriteria criteria, Pageable pageable);

//    void querySummaryView(HttpServletResponse response, SummaryViewQueryCriteria criteria);
}
