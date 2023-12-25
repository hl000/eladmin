package me.zhengjie.rest;

import com.taobao.api.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.service.PurchaseDetailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HL
 * @create 2022/12/5 13:50
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "物料详情")
@RequestMapping("/api/purchase")
public class PurchaseController {

    private final PurchaseDetailService purchaseDetailService;

    @GetMapping("/savePurchaseDetail")
    @Log("同步物料申购信息")
    @ApiOperation("同步物料申购信息")
    public void savePurchaseDetail() {
        purchaseDetailService.savePurchaseDetail();
    }

    @GetMapping("/sendMessage")
    @Log("发送申领信息")
    @ApiOperation("发送申领信息")
    public void sendMessage() throws ApiException {
        purchaseDetailService.sendMessage();
    }
}
