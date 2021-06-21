package me.zhengjie.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.base.MergeResult;
import me.zhengjie.domain.Balance;
import me.zhengjie.domain.BalanceView;
import me.zhengjie.service.StockService;
import me.zhengjie.service.dto.BalanceQueryCriteria;
import me.zhengjie.utils.PageUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author HL
 * @create 2021/5/19 17:45
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "存结管理")
@RequestMapping("/api/stock")
public class StockController {
    private final StockService stockService;

    @GetMapping("/getBalance/download")
    @Log("导出库存信息")
    @ApiOperation("导出库存信息")
    public void download(HttpServletResponse response, BalanceQueryCriteria criteria) {
        stockService.download(response, criteria);
    }


    @GetMapping("/getBalance")
    @Log("查看结存信息")
    @ApiOperation("查看结存信息")
    public Object getBalance(BalanceQueryCriteria criteria, Pageable pageable) {

        List<BalanceView> balanceList = stockService.queryBalance(criteria);
        if (balanceList == null || balanceList.size() == 0)
            return null;
        MergeResult mergeResult = new MergeResult();
        mergeResult.totalElements = balanceList.size();
        mergeResult.totalPages = balanceList.size() % pageable.getPageSize() == 0 ? balanceList.size() / pageable.getPageSize() : balanceList.size() / pageable.getPageSize() + 1;
        mergeResult.currentPage = pageable.getPageNumber();
        mergeResult.size = pageable.getPageSize();
        mergeResult.content = PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), balanceList);
        return mergeResult;
    }

}