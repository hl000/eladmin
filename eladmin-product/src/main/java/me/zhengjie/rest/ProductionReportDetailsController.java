package me.zhengjie.rest;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import me.zhengjie.service.ProductionReportDetailsService;
import me.zhengjie.service.dto.ProductionReportDetailsCriteria;
import me.zhengjie.utils.PageUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

/**
 * @author HL
 * @create 2021/12/6 19:11
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "生产日报工明细")
@RequestMapping("/api/productionReport")
public class ProductionReportDetailsController {


    private final ProductionReportDetailsService productionReportDetailsService;

    /**
     * 生产日报明细查询
     */
    @GetMapping("/getProductionReport")
    public ResponseEntity<Object> getProductionReport(ProductionReportDetailsCriteria criteria, Pageable pageable) {
        Map<String, Object> resultList = productionReportDetailsService.getProductionReport(criteria, pageable);
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }


    /**
     * 下载生产日报明细
     *
     * @param response
     * @param criteria
     * @throws ParseException
     * @throws IOException
     */
    @GetMapping("/getProductionReport/download")
    public void downloadProductionReport(HttpServletResponse response, ProductionReportDetailsCriteria criteria) {
        productionReportDetailsService.download(response, criteria);
    }

    /**
     * 生产日报明细查询
     */
    @GetMapping("/getAllProductionReport")
    public ResponseEntity<Object> getAllProductionReport(ProductionReportDetailsCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(productionReportDetailsService.queryAll(criteria), HttpStatus.OK);
    }

}
