package me.zhengjie.service;

import me.zhengjie.domain.Manufacture;
import me.zhengjie.service.dto.StockDto;
import me.zhengjie.service.dto.StockQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author HL
 * @create 2021/5/19 13:59
 */
public interface StockService {

    Map<String, Object> queryAll(StockQueryCriteria criteria, Pageable pageable);

    List<StockDto> queryAll(StockQueryCriteria criteria);

    void updateStock(Manufacture manufacture);

    void download(HttpServletResponse response, StockQueryCriteria criteria);
}
