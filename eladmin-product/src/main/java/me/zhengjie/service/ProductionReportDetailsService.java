package me.zhengjie.service;

import me.zhengjie.domain.ProductionReportDetailsView;
import me.zhengjie.service.dto.ProductionReportDetailsCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author HL
 * @create 2021/12/6 19:19
 */
public interface ProductionReportDetailsService {

    /**
     * 查询数据分页
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String,Object>
     */
    Map<String,Object> getProductionReport(ProductionReportDetailsCriteria criteria, Pageable pageable);

    List<ProductionReportDetailsView> queryAll(ProductionReportDetailsCriteria criteria);


    void download(HttpServletResponse response, ProductionReportDetailsCriteria criteria);

}
