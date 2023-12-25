package me.zhengjie.mapper;

import me.zhengjie.annotation.DatabaseType;
import me.zhengjie.annotation.DynamicDao;
import me.zhengjie.domain.PurchaseDetail;

import java.util.List;


/**
 * @author HL
 * @create 2022/2/14 17:40
 */
@DynamicDao(type = DatabaseType.second)
public interface PurchaseDetailMapper {
    List<PurchaseDetail> queryPurchaseDetail(Long processNumber);

    Long getProcessNumber();

    void updateProcessNumber(Long processNumber);

    int insertPurchase(PurchaseDetail purchaseDetail);

    List<PurchaseDetail> findPurchaseDetail();

    void updatePurchaseDetail(Integer id);
}
