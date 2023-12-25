package me.zhengjie.schedule;

import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.service.PurchaseDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author HL
 * @create 2022/12/6 9:42
 */
@Slf4j
@Component
public class PurchaseTimer {
    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Scheduled(cron = "${scheduled.purchase-timer}")
    public void createPurchaseTimer() {
        purchaseDetailService.savePurchaseDetail();
    }

    @Scheduled(cron = "${scheduled.purchase-send-timer}")
    public void createPurchaseSendTimer() throws ApiException {
        purchaseDetailService.sendMessage();
    }
}
