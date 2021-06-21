package me.zhengjie.service;

import me.zhengjie.domain.Balance;
import me.zhengjie.domain.BalanceView;
import me.zhengjie.domain.Manufacture;
import me.zhengjie.service.dto.BalanceQueryCriteria;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author HL
 * @create 2021/5/19 13:59
 */
public interface StockService {

    void download(HttpServletResponse response, BalanceQueryCriteria criteria);

    void updateBalance(Manufacture manufacture2, String create, Manufacture manufacture);
    List<BalanceView> queryBalance(BalanceQueryCriteria criteria);
}
