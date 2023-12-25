package me.zhengjie.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.taobao.api.ApiException;
import lombok.RequiredArgsConstructor;
import me.zhengjie.Utils.HttpClientUtil;
import me.zhengjie.domain.PurchaseDetail;
import me.zhengjie.mapper.PurchaseDetailMapper;
import me.zhengjie.service.PurchaseDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HL
 * @create 2022/11/28 17:47
 */
@Service
@RequiredArgsConstructor
public class PurchaseDetailServiceImpl implements PurchaseDetailService {

    @Resource
    private final PurchaseDetailMapper purchaseDetailMapper;

    public static final long cacheTime = 1000 * 60 * 55 * 2;

    public static long tokenTime = 0, ticketTime = 0;

    public static String token, ticket;

    public String getCorpToken() {
        Long now = System.currentTimeMillis();
        if (now - tokenTime > cacheTime) { //过期
            String url = "https://oapi.dingtalk.com/gettoken?appkey=dinghwdcpna5bzpgfbjp&appsecret=ZgDIImJxhQdKiRqoi_Bkv0RsT7CByUTw2gvS1VL_MKFYaTNSVI5gAwiaxP5IdLMB";
            String result = HttpClientUtil.doGet(url);
            JSONObject jsonObject = JSONObject.parseObject(result);
            token = jsonObject.getString("access_token");
            tokenTime = now;
        }
        return token;
    }

    @Override
    public void savePurchaseDetail() {
        Long processNumber = purchaseDetailMapper.getProcessNumber();
        List<PurchaseDetail> purchaseDetails = purchaseDetailMapper.queryPurchaseDetail(processNumber);
        purchaseDetails.forEach(a -> purchaseDetailMapper.insertPurchase(a));

        if (purchaseDetails != null && purchaseDetails.size() > 0) {
            purchaseDetails = purchaseDetails.stream().sorted(Comparator.comparing(PurchaseDetail::getAutoId, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
            purchaseDetailMapper.updateProcessNumber(purchaseDetails.get(0).getAutoId());
        }

    }

    @Override
    public void sendMessage() throws ApiException {
        List<PurchaseDetail> purchaseDetails = purchaseDetailMapper.findPurchaseDetail();
        for (PurchaseDetail purchaseDetail : purchaseDetails) {
            //send message
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2");
            OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
            request.setAgentId(1210354280L);
            request.setUseridList(purchaseDetail.getUserId());
            request.setToAllUser(false);

            OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
            msg.setMsgtype("text");
            msg.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
            msg.getText().setContent("你请购的" + purchaseDetail.getCInvName() + "已经到货，请及时领取");
            request.setMsg(msg);
            OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(request, getCorpToken());
        }

        purchaseDetails.forEach(a -> purchaseDetailMapper.updatePurchaseDetail(a.getId()));
    }
}
