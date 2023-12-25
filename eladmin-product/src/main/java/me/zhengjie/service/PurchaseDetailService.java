package me.zhengjie.service;

import com.taobao.api.ApiException;

/**
 * @author HL
 * @create 2022/11/28 17:46
 */
public interface PurchaseDetailService {

    void savePurchaseDetail();

    void sendMessage() throws ApiException;
}
