package me.zhengjie.rest;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import me.zhengjie.service.StackWorkService;
import me.zhengjie.service.dto.StackWorkQueryCriteria;
import me.zhengjie.utils.PageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author HL
 * @create 2021/6/7 9:52
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "电堆汇总视图查询")
@RequestMapping("/api/stackWork")
public class StackWorkController {

    private final StackWorkService stackWorkService;

    /**
     * 获取地区电堆生产统计汇总
     *
     * @param criteria
     * @return
     * @throws ParseException
     */
    @GetMapping("/getProductByAddress")
    public ResponseEntity<Object> getStackWork(StackWorkQueryCriteria criteria) throws ParseException {
        List<Map<String, Object>> resultList = stackWorkService.getStackWork(criteria);
        return new ResponseEntity<>(PageUtil.toPage(resultList, resultList.size()), HttpStatus.OK);
    }

    /**
     * 下载地区电推生产数据汇总
     *
     * @param response
     * @param criteria
     * @throws ParseException
     * @throws IOException
     */
    @GetMapping("/getProductByAddress/download")
    public void downloadStackWork(HttpServletResponse response, StackWorkQueryCriteria criteria) throws IOException {
        stackWorkService.stackWorkDownload(criteria, response);
    }
}
