package me.zhengjie.utils;

import me.zhengjie.Doc.PlmFile;
import me.zhengjie.base.MergeResult;
import me.zhengjie.base.ResKv;
import me.zhengjie.constant.PlmConstant;
import me.zhengjie.domain.ExpStackInfo;
import me.zhengjie.domain.StackReplaceInfo;
import me.zhengjie.dto.*;
import me.zhengjie.dto.car.CarInfo;
import me.zhengjie.enums.MergeEnum;
import me.zhengjie.res.StackReplaceRes;
import me.zhengjie.resp.*;
import me.zhengjie.resp.car.CarConsumeRes;
import me.zhengjie.resp.car.CarMileageRes;
import org.springframework.data.domain.Pageable;

import java.util.*;

public class ResultUtils {
    public static Object fillMeaDetail(List<MeaDto> meaDetailList, List<String> allowedPart) {

        List<SupplyInfo> result = new ArrayList<>();
        boolean isAllowed = (allowedPart != null && allowedPart.contains("MEA")) || SecurityUtils.getCurrentUser().getUsername().equals("admin");
        for (MeaDto meaDetail : meaDetailList) {
            SupplyInfo supplyInfo = new SupplyInfo();
            supplyInfo.YUANLIAONAME = new ResKv("原材料名称", isAllowed ? meaDetail.YUANLIAONAME : PlmConstant.K_NO_READ_WARM_INFO);
            supplyInfo.YUANLIAOCODE = new ResKv("原材料编码", isAllowed ? meaDetail.YUANLIAOCODE : PlmConstant.K_NO_READ_WARM_INFO);
            supplyInfo.YUANLIAOGUIGE = new ResKv("原材料规格", isAllowed ? meaDetail.YUANLIAOGUIGE : PlmConstant.K_NO_READ_WARM_INFO);
            supplyInfo.YUANLIAOGYSNAME = new ResKv("原材料供应商", isAllowed ? meaDetail.YUANLIAOGYSNAME : PlmConstant.K_NO_READ_WARM_INFO);
            supplyInfo.YUANLIAOGYSCODE = new ResKv("原材料供应商编码", isAllowed ? meaDetail.YUANLIAOGYSCODE : PlmConstant.K_NO_READ_WARM_INFO);
            result.add(supplyInfo);
        }
        return result;

    }


    public static Object fillSystemDetail(List<MainDto> mainDtoList, List<String> allowedPart, String type) {
        List<ResKv> result = new ArrayList<>();
        if (type.equals(PlmConstant.K_XITONG)) {
            if (mainDtoList.size() > 0) {
                boolean isAllowed = (allowedPart != null && allowedPart.contains("SYSTEM")) || SecurityUtils.getCurrentUser().getUsername().equals("admin");
                MainDto mainDto = mainDtoList.get(0);
                result.add(new ResKv("增湿器", Arrays.asList(
                        new ResKv("编号", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getZENGSHIQI()),
                        new ResKv("编码", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getZSQBIANMA()),
                        new ResKv("名称", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getZSQNAME()),
                        new ResKv("规格", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getZSQGUIGE()),
                        new ResKv("供应商", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getZSQGYSNAME()),
                        new ResKv("供应商编码", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getZSQGYSCODE()))));

                result.add(new ResKv("氢循环泵", Arrays.asList(
                        new ResKv("编号", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getXUNHUANBENG()),
                        new ResKv("编码", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getXHBCODE()),
                        new ResKv("控制器", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getXHBCONTROLLER()),
                        new ResKv("名称", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getXHBNAME()),
                        new ResKv("规格", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getXHBGUIGE()),
                        new ResKv("供应商", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getXHBGYSNAME()),
                        new ResKv("供应商编码", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getXHBGYSCODE())

                )));
                result.add(new ResKv("空气泵", Arrays.asList(
                        new ResKv("编号", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getKONGQIBENG()),
                        new ResKv("编码", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getKQBCODE()),
                        new ResKv("控制器", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getKQBCONTROLLER()),
                        new ResKv("名称", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getKQBNAME()),
                        new ResKv("规格", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getKQBGUIGE()),
                        new ResKv("供应商", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getKQBGYSNAME()),
                        new ResKv("供应商编码", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getKQBGYSCODE())
                )));
            }


        } else if (type.equals(PlmConstant.K_BIP)) {
            if (mainDtoList.size() > 0) {
                boolean isAllowed = (allowedPart != null && allowedPart.contains("BIP")) || SecurityUtils.getCurrentUser().getUsername().equals("admin");
                ;
                MainDto mainDto = mainDtoList.get(0);
                result.add(new ResKv("胶线", Arrays.asList(
                        new ResKv("编码", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getJXCODE()),
                        new ResKv("名称", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getJXNAME()),
                        new ResKv("规格", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getJXGUIGE()),
                        new ResKv("供应商", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getJXGYSNAME()),
                        new ResKv("供应商编码", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getJXGYSCODE())
                )));
                result.add(new ResKv("钢板", Arrays.asList(
                        new ResKv("编号", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getGBCODE()),
                        new ResKv("名称", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getGBNAME()),
                        new ResKv("规格", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getGBGUIGE()),
                        new ResKv("供应商", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getGBGYSNAME()),
                        new ResKv("供应商编码", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : mainDto.getGBGYSCODE())
                )));
            }
        }
        return result;

    }

    public static MergeResult mergeFromPart(int totalCnt, List list, Pageable pageable, MergeEnum mergeEnum, List<String> allowedPart) {
        MergeResult mergeResult = new MergeResult();
        mergeResult.totalElements = totalCnt;
        mergeResult.totalPages = totalCnt % pageable.getPageSize() == 0 ? totalCnt / pageable.getPageSize() : totalCnt / pageable.getPageSize() + 1;
        mergeResult.currentPage = pageable.getPageNumber();
        mergeResult.size = pageable.getPageSize();
        //开始进行组合
        List<Object> all = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (mergeEnum == MergeEnum.MAIN) {
                fillMain(all, list.get(i), allowedPart);
            } else if (mergeEnum == MergeEnum.GZ_PAPER || mergeEnum == MergeEnum.CP_PAPER) {
                fillGZOrCPPAPER(all, list.get(i), mergeEnum, allowedPart);
            } else if (mergeEnum == MergeEnum.SHOUHOU) {
                fillShouHou(all, list.get(i), allowedPart);
            } else if (mergeEnum == MergeEnum.SB_PAPER) {
                fillSBPAPER(all, list.get(i));
            } else if (mergeEnum == MergeEnum.PAPER) {
                fillPAPER(all, list.get(i), allowedPart);
            } else if (mergeEnum == MergeEnum.CESHI) {
                fillCESHI(all, list.get(i), allowedPart);
            } else if (mergeEnum == MergeEnum.STACK_REPLACE) {
                fillStackReplace(all, list.get(i), allowedPart);
            } else if (mergeEnum == MergeEnum.CAR_MILEAGE) {
                fillCarMileage(all, list.get(i), allowedPart);
            } else if (mergeEnum == MergeEnum.CAR_CONSUME) {
                fillCarConsume(all, list.get(i), allowedPart);
            }
        }

        mergeResult.content = all;
        return mergeResult;

    }

    private static void fillCarMileage(List<Object> all, Object o, List<String> allowedPart) {
        CarInfo carInfo = (CarInfo) o;
        CarMileageRes carMileageRes = new CarMileageRes();
        boolean isAllowed = (allowedPart != null && allowedPart.contains("CAR_INFO")) || SecurityUtils.getCurrentUser().getUsername().equals("admin");
        ;
        carMileageRes.fdate = Collections.singletonList(new ResKv("统计日期", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : carInfo.getFdate()));
        carMileageRes.number = Collections.singletonList(new ResKv("车牌号", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : carInfo.getNumber()));
        carMileageRes.base = Collections.singletonList(new ResKv("基地", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : carInfo.getBase()));
        carMileageRes.carType = Collections.singletonList(new ResKv("车辆类型", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : carInfo.getCarType()));
        carMileageRes.mileage = Collections.singletonList(new ResKv("里程数(KM)", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : carInfo.getMileage()));
        all.add(carMileageRes);
    }

    private static void fillCarConsume(List<Object> all, Object o, List<String> allowedPart) {
        CarInfo carInfo = (CarInfo) o;
        CarConsumeRes carConsumeRes = new CarConsumeRes();
        boolean isAllowed = (allowedPart != null && allowedPart.contains("CAR_INFO")) || SecurityUtils.getCurrentUser().getUsername().equals("admin");
        ;
        carConsumeRes.fdate = Collections.singletonList(new ResKv("统计日期", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : carInfo.getFdate()));
        carConsumeRes.number = Collections.singletonList(new ResKv("车牌号", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : carInfo.getNumber()));
        carConsumeRes.route = Collections.singletonList(new ResKv("路线", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : carInfo.getRoute()));
        carConsumeRes.mileage = Collections.singletonList(new ResKv("里程数(KM)", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : carInfo.getMileage()));
        carConsumeRes.jiaQing = Collections.singletonList(new ResKv("加氢量(KG)", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : carInfo.getJiaQing()));
        carConsumeRes.qinghao= Collections.singletonList(new ResKv("百公里耗氢量(KG)", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : carInfo.getQinghao()));

        all.add(carConsumeRes);
    }

    private static void fillStackReplace(List<Object> all, Object o, List<String> allowedPart) {
        StackReplaceInfo stackReplaceInfo = (StackReplaceInfo) o;
        StackReplaceRes stackReplaceRes = new StackReplaceRes();
        boolean isAllowed = (allowedPart != null && allowedPart.contains("STACK_REPLACE")) || SecurityUtils.getCurrentUser().getUsername().equals("admin");
        ;

        stackReplaceRes.FDATE = Collections.singletonList(new ResKv("更换日期", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFDATE()));
        stackReplaceRes.FYBIANHAO = Collections.singletonList(new ResKv("原电堆编号", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFYBIANHAO()));
        stackReplaceRes.FGBIANHAO = Collections.singletonList(new ResKv("新电堆编号", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO :stackReplaceInfo.getFGBIANHAO()));
        stackReplaceRes.FCHEPAI = Collections.singletonList(new ResKv("车牌号", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFCHEPAI()));
        stackReplaceRes.FGONGLI = Collections.singletonList(new ResKv("公里数", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFGONGLI()));
        stackReplaceRes.Fjidi = Collections.singletonList(new ResKv("基地", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFjidi()));
        stackReplaceRes.Fleixing = Collections.singletonList(new ResKv("类型", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFleixing()));
        stackReplaceRes.FCATE = Collections.singletonList(new ResKv("故障类别", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFCATE()));
        stackReplaceRes.Fdaima = Collections.singletonList(new ResKv("故障码", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFdaima()));
        stackReplaceRes.Fjibie = Collections.singletonList(new ResKv("故障等级", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFjibie()));
        stackReplaceRes.FYUANYIN = Collections.singletonList(new ResKv("故障原因", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO :stackReplaceInfo.getFYUANYIN()));
        stackReplaceRes.Fyuanxitong = Collections.singletonList(new ResKv("原系统编号", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFyuanxitong()));
        stackReplaceRes.Fxinxitong = Collections.singletonList(new ResKv("新系统编号", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFxinxitong()));
        stackReplaceRes.FGONGSHI = Collections.singletonList(new ResKv("维修工时(h)", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFGONGSHI()));
        stackReplaceRes.FRENYUAN = Collections.singletonList(new ResKv("维修人员", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : stackReplaceInfo.getFRENYUAN()));

        all.add(stackReplaceRes);

    }

    private static void fillSBPAPER(List<Object> all, Object o) {
        SheBeiPaperDto sheBeiPaperDto = (SheBeiPaperDto) o;
        SheBeiPaperRes sheBeiPaperRes = new SheBeiPaperRes();
        sheBeiPaperRes.Fname = Collections.singletonList(new ResKv("机器", sheBeiPaperDto.Fname));
        sheBeiPaperRes.Fgoucheng = Collections.singletonList(new ResKv("单元", sheBeiPaperDto.Fgoucheng));
        sheBeiPaperRes.Fshebei = Collections.singletonList(new ResKv("类别", sheBeiPaperDto.Fshebei));
        sheBeiPaperRes.Flujing = Collections.singletonList(new ResKv("路径", sheBeiPaperDto.Flujing));
        sheBeiPaperRes.Fmingxi = Collections.singletonList(new ResKv("文件", sheBeiPaperDto.Fmingxi));
        sheBeiPaperRes.Ffuzeren = Collections.singletonList(new ResKv("负责人", sheBeiPaperDto.Ffuzeren));
        all.add(sheBeiPaperRes);

    }

    private static void fillPAPER(List<Object> all, Object o, List<String> allowedPart) {
        PlmFile plmFile = (PlmFile) o;
        PaperRes paperRes = new PaperRes();
        boolean isAllowed = (allowedPart != null && allowedPart.contains("PAPER")) || SecurityUtils.getCurrentUser().getUsername().equals("admin");
        ;
        paperRes.FILENAME = Collections.singletonList(new ResKv("文件存储名", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : plmFile.getFileName()));
        paperRes.ORIGINFILENAME = Collections.singletonList(new ResKv("文件名", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : plmFile.getOriginalFileName()));
        paperRes.FILEDESC = Collections.singletonList(new ResKv("文件描述", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : plmFile.getFileDesc()));
        paperRes.SUFFIX = Collections.singletonList(new ResKv("文件类型", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : plmFile.getSuffix()));
        paperRes.WRITER = Collections.singletonList(new ResKv("提交者", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : plmFile.getWriter()));
        paperRes.VERSION = Collections.singletonList(new ResKv("版本号", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : plmFile.getVersion()));
        paperRes.TAG = Collections.singletonList(new ResKv("标签", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : plmFile.getTag()));
        paperRes.STOREPATH = Collections.singletonList(new ResKv("存储路径", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : plmFile.getStorePath()));
        paperRes.FIRSTDIR = Collections.singletonList(new ResKv("一级目录", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : plmFile.getFirstDir()));
        paperRes.SECONDDIR = Collections.singletonList(new ResKv("二级目录", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : plmFile.getSecondDir()));
        paperRes.THIRDDIR = Collections.singletonList(new ResKv("三级目录", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : plmFile.getThirdDir()));
        paperRes.FORTHDIR = Collections.singletonList(new ResKv("四级目录", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : plmFile.getForthDir()));
        paperRes.SAMECOUNT = Collections.singletonList(new ResKv("相同文件个数", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : plmFile.getSameCount()));

        all.add(paperRes);

    }

    private static void fillCESHI(List<Object> all, Object o, List<String> allowedPart) {
        ExpStackInfo expStackDto = (ExpStackInfo) o;
        StackExpRes stackExpRes = new StackExpRes();
        boolean isAllowed = (allowedPart != null && allowedPart.contains("STACK_EXP")) || SecurityUtils.getCurrentUser().getUsername().equals("admin");
        ;

        stackExpRes.FSubmitDate = Collections.singletonList(new ResKv("提交日期", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getFSubmitDate()));
        stackExpRes.FBase = Collections.singletonList(new ResKv("基地", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getFBase()));
        stackExpRes.FSubmitter = Collections.singletonList(new ResKv("提交者", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getFSubmitter()));
        stackExpRes.FNumber = Collections.singletonList(new ResKv("电堆编号", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getFNumber()));
        stackExpRes.Fjieshu = Collections.singletonList(new ResKv("节数", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getFjieshu()));
        stackExpRes.FBIP = Collections.singletonList(new ResKv("BIP编号", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getFBIP()));
        stackExpRes.FMEA = Collections.singletonList(new ResKv("MEA型号", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getFMEA()));
        stackExpRes.FMEADate = Collections.singletonList(new ResKv("MEA日期", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getFMEADate()));
        stackExpRes.FTanzhi = Collections.singletonList(new ResKv("碳纸类型", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getFTanzhi()));
        stackExpRes.FDate = Collections.singletonList(new ResKv("活化日期", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getFDate()));
        stackExpRes.FCishu = Collections.singletonList(new ResKv("活化次数", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getFCishu()));
        stackExpRes.FEmp = Collections.singletonList(new ResKv("活化人员", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getFEmp()));
        stackExpRes.FJiaya = Collections.singletonList(new ResKv("是否加压", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getFJiaya()));
        stackExpRes.FZuzhaung = Collections.singletonList(new ResKv("组装力", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getFZuzhaung()));
        stackExpRes.FZuzhaungEmp = Collections.singletonList(new ResKv("组装人员", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getFZuzhaungEmp()));
        stackExpRes.F500V = Collections.singletonList(new ResKv("500电密总电压", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getF500V()));
        stackExpRes.F500KW = Collections.singletonList(new ResKv("500电密总功率", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getF500KW()));
        stackExpRes.F600V = Collections.singletonList(new ResKv("600电密总电压", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getF600V()));
        stackExpRes.F600KW = Collections.singletonList(new ResKv("600电密总功率", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getF600KW()));
        stackExpRes.F700V = Collections.singletonList(new ResKv("700电密总电压", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getF700V()));
        stackExpRes.F700KW = Collections.singletonList(new ResKv("700电密总功率", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getF700KW()));
        stackExpRes.F800V = Collections.singletonList(new ResKv("800电密总电压", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getF800V()));
        stackExpRes.F800KW = Collections.singletonList(new ResKv("800电密总功率", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getF800KW()));
        stackExpRes.F900V = Collections.singletonList(new ResKv("900电密总电压", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getF900V()));
        stackExpRes.F900KW = Collections.singletonList(new ResKv("900电密总功率", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getF900KW()));
        stackExpRes.F1000V = Collections.singletonList(new ResKv("1000电密总电压", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getF1000V()));
        stackExpRes.F1000KW = Collections.singletonList(new ResKv("1000电密总功率", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getF1000KW()));
        stackExpRes.FPingjunV = Collections.singletonList(new ResKv("平均电压", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getFPingjunV()));
        stackExpRes.FZuidi1 = Collections.singletonList(new ResKv("最低节数 最低电压1", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getFZuidi1()));
        stackExpRes.FZuidi2 = Collections.singletonList(new ResKv("最低节数 最低电压2", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getFZuidi2()));
        stackExpRes.FZuidi3 = Collections.singletonList(new ResKv("最低节数 最低电压3", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getFZuidi3()));
        stackExpRes.FNote = Collections.singletonList(new ResKv("备注", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : expStackDto.getFNote()));
        all.add(stackExpRes);

    }


    private static void fillShouHou(List<Object> all, Object o, List<String> allowedPart) {
        ShouHouDto shouHouDto = (ShouHouDto) o;
        ShouHouRes shouHouRes = new ShouHouRes();

        boolean isAllowed = (allowedPart != null && allowedPart.contains("SHOUHOU")) || SecurityUtils.getCurrentUser().getUsername().equals("admin");
        ;

        shouHouRes.FLicensenumber = Collections.singletonList(new ResKv("车牌号", shouHouDto.FLicensenumber));
        shouHouRes.FADDRESS = Collections.singletonList(new ResKv("基地", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : shouHouDto.getFADDRESS()));
        shouHouRes.FMAINTENANCEDATE = Collections.singletonList(new ResKv("报修日期", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : shouHouDto.FMAINTENANCEDATE));
        shouHouRes.FPLACE = Collections.singletonList(new ResKv("地点", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : shouHouDto.FPlace));
        shouHouRes.FFCODE = Collections.singletonList(new ResKv("故障码", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : shouHouDto.ffaultcode));
        shouHouRes.FFAILURE = Collections.singletonList(new ResKv("故障分类", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : shouHouDto.getFFAILURE()));
        shouHouRes.FCAUSE = Collections.singletonList(new ResKv("故障描述", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : shouHouDto.getFFAILURE1()));
        shouHouRes.FSOLUTION = Collections.singletonList(new ResKv("解决方案", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : shouHouDto.getFSOLUTION()));
        shouHouRes.FSPAREPARTS = Collections.singletonList(new ResKv("更换部件", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : shouHouDto.getFSPAREPARTS()));
        shouHouRes.FREPAIR = Collections.singletonList(new ResKv("是否维修完毕", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : shouHouDto.getFREPAIR()));
        all.add(shouHouRes);
    }

    private static void fillGZOrCPPAPER(List<Object> all, Object o, MergeEnum mergeEnum, List<String> allowedPart) {
        GongzhuangPaperDto gongzhuangPaperDto = (GongzhuangPaperDto) o;
        GongzhuangRes gongzhuangRes = new GongzhuangRes();

        boolean isAllowed = (allowedPart != null &&
                mergeEnum == MergeEnum.CP_PAPER ? allowedPart.contains("CP_PAPER") : allowedPart.contains("GZ_PAPER"))
                || SecurityUtils.getCurrentUser().getUsername().equals("admin");
        ;

        gongzhuangRes.FBANBEN = Collections.singletonList(new ResKv("版本", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : gongzhuangPaperDto.getFBANBEN()));
        gongzhuangRes.FGONGXU = Collections.singletonList(new ResKv(mergeEnum == MergeEnum.GZ_PAPER ? "工序" : "产品类型", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : gongzhuangPaperDto.getFGONGXU()));
        gongzhuangRes.FGONGZHUANG = Collections.singletonList(new ResKv("文件种类", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : gongzhuangPaperDto.getFGONGZHUANG()));
        gongzhuangRes.FWENDANG = Collections.singletonList(new ResKv("文档路径", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : gongzhuangPaperDto.getFWENDANG()));
        gongzhuangRes.FWENJIAN = Collections.singletonList(new ResKv("文件", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : gongzhuangPaperDto.getFWENJIAN()));
        gongzhuangRes.FFUZEREN = Collections.singletonList(new ResKv("负责人", !isAllowed ? PlmConstant.K_NO_READ_WARM_INFO : gongzhuangPaperDto.getFFUZEREN()));

        all.add(gongzhuangRes);
    }

    private static void fillMain(List<Object> all, Object o, List<String> allowedPart) {
        MainDto mainDto = (MainDto) o;
        MRecord mRecord = new MRecord();
        mRecord.DIANDUI = Collections.singletonList(new ResKv("电堆编号", mainDto.getDIANDUI()));
        mRecord.XITONG = Collections.singletonList(new ResKv("系统编号", mainDto.getXITONG()));
        mRecord.CHEPAI = Collections.singletonList(new ResKv("车牌号", mainDto.getCHEPAI()));
        mRecord.BIP = Collections.singletonList(new ResKv("双极板", mainDto.getBIP()));
        mRecord.MEA = Collections.singletonList(new ResKv("MEA", mainDto.getMEA()));
        all.add(mRecord);
    }


    public static List<PlmFile> mergeSame(List<PlmFile> list, boolean isMergeSame) {
        List<PlmFile> result = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        for (PlmFile plmFile : list) {
            String unionKey = plmFile.getOriginalFileName() + plmFile.getSuffix();
            if (!map.containsKey(unionKey)) {
                map.put(unionKey, 0);
            }
            map.put(unionKey, map.get(unionKey) + 1);
            if (!isMergeSame) {
                result.add(plmFile);
            } else if (map.get(unionKey) == 1) {
                result.add(plmFile);
            }
        }
        result.forEach(plmFile -> plmFile.setSameCount(map.get(plmFile.getOriginalFileName() + plmFile.getSuffix())));
        return result;
    }


}
