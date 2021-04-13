package me.zhengjie.Utils;

import me.zhengjie.base.MergeResult;
import me.zhengjie.base.ResKv;
import me.zhengjie.constant.ProductConstant;
import me.zhengjie.domain.FixRecordInfo;
import me.zhengjie.domain.StackReplaceInfo;
import me.zhengjie.enums.MergeEnums;
import me.zhengjie.res.ShouHouRes;
import me.zhengjie.res.StackReplaceRes;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;

import java.util.*;

public class ResultMergeUtils {



    public static MergeResult mergeFromPart(int totalCnt, List list, Pageable pageable, MergeEnums mergeEnum, List<String> allowedPart) {
        MergeResult mergeResult = new MergeResult();
        mergeResult.totalElements = totalCnt;
        mergeResult.totalPages = totalCnt % pageable.getPageSize() == 0 ? totalCnt / pageable.getPageSize() : totalCnt / pageable.getPageSize() + 1;
        mergeResult.currentPage = pageable.getPageNumber();
        mergeResult.size = pageable.getPageSize();
        //开始进行组合
        List<Object> all = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (mergeEnum == MergeEnums.SHOUHOU) {
                fillShouHou(all, list.get(i), allowedPart);
            }else if (mergeEnum == MergeEnums.STACK_REPLACE) {
                fillStackReplace (all, list.get(i), allowedPart);
            }
        }

        mergeResult.content = all;
        return mergeResult;

    }

    private static void fillStackReplace(List<Object> all, Object o, List<String> allowedPart) {
        StackReplaceInfo stackReplaceInfo = (StackReplaceInfo)o;
        StackReplaceRes stackReplaceRes = new StackReplaceRes();
        boolean isAllowed = true;
        stackReplaceRes.FDATE = Collections.singletonList(new ResKv("日期", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFDATE()));
        //车牌号
        stackReplaceRes.FCHEPAI = Collections.singletonList(new ResKv("车牌", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFCHEPAI()));
        //基地
        stackReplaceRes.Fjidi = Collections.singletonList(new ResKv("基地", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFjidi()));
        //车辆类型
        stackReplaceRes.Fleixing = Collections.singletonList(new ResKv("车辆类型", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFleixing()));
        //故障类别
        stackReplaceRes.FCATE =  Collections.singletonList(new ResKv("故障类别", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFCATE()));
        //故障码
        stackReplaceRes.Fdaima =  Collections.singletonList(new ResKv("故障码", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFdaima()));
        //故障等级
        stackReplaceRes.Fjibie =  Collections.singletonList(new ResKv("故障等级", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFjibie()));
        //故障原因
        stackReplaceRes.FYUANYIN = Collections.singletonList(new ResKv("故障原因", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFYUANYIN()));
        //原电堆编号
        stackReplaceRes.FYBIANHAO = Collections.singletonList(new ResKv("原电堆编号", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFYBIANHAO()));
        //原系统编号
        stackReplaceRes.Fyuanxitong = Collections.singletonList(new ResKv("原系统编号", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFyuanxitong()));
        //原电堆行驶里程数
        stackReplaceRes.FGONGLI  = Collections.singletonList(new ResKv("原电堆行驶里程数(KM)", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFGONGLI()));
        //新电堆编号
        stackReplaceRes.FGBIANHAO = Collections.singletonList(new ResKv("新电堆编号", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFGBIANHAO()));
        //新系统编号
        stackReplaceRes.Fxinxitong = Collections.singletonList(new ResKv("新系统编号", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFxinxitong()));
        //维修耗时（小时）
        stackReplaceRes.FGONGSHI = Collections.singletonList(new ResKv("维修耗时(小时)", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFGONGSHI()));
        //维修人员
        stackReplaceRes.FRENYUAN  = Collections.singletonList(new ResKv("维修人员", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFRENYUAN()));


        all.add(stackReplaceRes);
    }


    private static void fillShouHou(List<Object> all, Object o, List<String> allowedPart) {
        FixRecordInfo shouHouDto = (FixRecordInfo) o;
        ShouHouRes shouHouRes = new ShouHouRes();

       /* boolean isAllowed = (allowedPart != null && allowedPart.contains("SHOUHOU")) || SecurityUtils.getCurrentUser().getUsername().equals("admin");
        ;*/
        boolean isAllowed = true;

        shouHouRes.Flicensenumber = Collections.singletonList(new ResKv("车牌号", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : shouHouDto.getFlicensenumber()));
        shouHouRes.Faddress = Collections.singletonList(new ResKv("基地", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : shouHouDto.getFaddress()));
        shouHouRes.Fmaintenancedate = Collections.singletonList(new ResKv("报修日期", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : shouHouDto.getFmaintenancedate()));
        shouHouRes.FCLEIXING = Collections.singletonList(new ResKv("车辆类型", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : shouHouDto.getFCLEIXING()));
        shouHouRes.Fplace = Collections.singletonList(new ResKv("地点", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : shouHouDto.getFplace()));
        shouHouRes.Ffaultcode = Collections.singletonList(new ResKv("故障码", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : shouHouDto.getFfaultcode()));
        shouHouRes.Fdengji = Collections.singletonList(new ResKv("故障等级", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : shouHouDto.getFdengji()));
        shouHouRes.Ffailure = Collections.singletonList(new ResKv("故障分类", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO: shouHouDto.getFfailure()));
        shouHouRes.Ffailure1= Collections.singletonList(new ResKv("故障描述", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : shouHouDto.getFfailure1()));
        shouHouRes.Fsolution = Collections.singletonList(new ResKv("解决方案", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : shouHouDto.getFsolution()));
        shouHouRes.Fsubstitutepart = Collections.singletonList(new ResKv("是否更换部件", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : shouHouDto.getFsubstitutepart()));
        shouHouRes.Fspareparts = Collections.singletonList(new ResKv("更换部件", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : shouHouDto.getFspareparts()));
        shouHouRes.Frepair= Collections.singletonList(new ResKv("是否维修完毕", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : shouHouDto.getFrepair()));
        shouHouRes.Fcause= Collections.singletonList(new ResKv("未维修完成原因", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : shouHouDto.getFcause()));
        shouHouRes.Fduration = Collections.singletonList(new ResKv("维修耗时(h)", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : shouHouDto.getFduration()));
        shouHouRes.Fpersonnel = Collections.singletonList(new ResKv("维修人员", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : shouHouDto.getFpersonnel()));
        shouHouRes.FNote = Collections.singleton(new ResKv("备注", !isAllowed ? ProductConstant.K_NO_READ_WARM_INFO : shouHouDto.getFNote()));

        all.add(shouHouRes);
    }





}
