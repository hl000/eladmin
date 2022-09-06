package me.zhengjie.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.constant.PlmConstant;
import me.zhengjie.domain.ExpStackInfo;
import me.zhengjie.domain.StackReplaceInfo;
import me.zhengjie.dto.*;
import me.zhengjie.dto.car.CarInfo;
import me.zhengjie.enums.MergeEnum;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.mybatis.DbManager;
import me.zhengjie.request.KvDto;
import me.zhengjie.base.MergeResult;
import me.zhengjie.base.ResKv;
import me.zhengjie.service.FixHistoryService;
import me.zhengjie.utils.DownUtils;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static me.zhengjie.utils.ResultUtils.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plm")
@Api(tags = "系统：PLM产品生命周期管理")
public class PlmController {

    @Autowired
    DbManager dbManager;

    @Autowired
    FixHistoryService fixHistoryService;


    @GetMapping(value = "/selectList")
    @Log("查询下拉列表")
    @ApiOperation("查询下拉列表")
    public Object queryList(
            KvDto kvDto
    ) {
        kvDto.table = PlmConstant.get_K_SELETITEM_TABLE();
        return dbManager.getSelectItem(kvDto);

    }


    @GetMapping(value = "/kv/set")
    @Log("查询某个键的所有值集合")
    @ApiOperation("查询某个键的所有值集合")
    public Object queryKvSet(KvDto kvDto) {
        try {
            if (PlmConstant.K_MAIN.equals(kvDto.type) || StringUtils.isEmpty(kvDto.type)) {
                kvDto.table = PlmConstant.get_K_MAIN_TABLE();
            } else if (PlmConstant.K_GZ_PAPER.equals(kvDto.type)) {
                kvDto.table = PlmConstant.get_K_GZ_PAPER_TABLE();
                kvDto.extra = "WHERE FBANBEN = \'" + kvDto.input + '\'';
            } else if (PlmConstant.K_CHANPIN.equals(kvDto.type)) {
                kvDto.table = PlmConstant.get_K_CHANPIN_TABLE();
                kvDto.extra = "WHERE FBANBEN = \'" + kvDto.input + '\'';
            } else if (PlmConstant.K_SB_PAPER.equals(kvDto.type)) {
                kvDto.table = PlmConstant.get_K_SB_PAPER_TABLE();
            }
            List<String> result = dbManager.getSelectItemValue(kvDto);
            List<ResKv> obj = new ArrayList<>();
            result.stream().filter(str -> StringUtils.isNotEmpty(str)).collect(Collectors.toList()).forEach(str -> obj.add(new ResKv(kvDto.key, str)));
            return new ResKv(kvDto.key, obj);
        } catch (Exception e) {
            throw new BadRequestException("/kv/set发生错误");
        }
    }


    @GetMapping(value = "/record/bykv")
    @Log("按kv查询符合结果")
    @ApiOperation("按kv查询符合结果")
    public MergeResult queryByKV(
            KvDto kvDto,
            Pageable pageable
    ) {
        try {
           /* KvDto k = new KvDto("user", SecurityUtils.getCurrentUser().getUsername());
            k.table = PlmConstant.get_K_PLM_USER_ALLOWED();*/
            //ADMPLM_MG
            List<String> allowedPart = dbManager.queryUserAllowedPart(PlmConstant.get_K_PLM_USER_ALLOWED(), SecurityUtils.getCurrentUser().getUsername(), "plm");
            //ADMPLM
            kvDto.table = PlmConstant.get_K_MAIN_TABLE();
            List<MainDto> ret = dbManager.selectRecordByKv(kvDto);

            /*Set<String> allDianDui = ret.stream().map(s -> s.DIANDUI).collect(Collectors.toSet());
            List<StackReplaceDto> replaceRecord = new ArrayList<>();
            if (allDianDui !=null &&  allDianDui.size() >0) {
                replaceRecord = dbManager.getShouHouDetailByStack(allDianDui);
            }*/

            //ADMXTGH
            List<StackReplaceDto> replaceRecord = dbManager.getShouHouDetailByStack(new HashSet<>());
            // System.out.println(allDianDui);
            //TODO 对于存在更换电堆的先进行手动替换
            fixProcess(ret, replaceRecord);
            return mergeFromPart(ret.size(), PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), ret), pageable, MergeEnum.MAIN, allowedPart);
        } catch (Exception e) {
            throw new BadRequestException("/record/bykv发生错误" + e);
        }
    }

    private void fixProcess(List<MainDto> ret, List<StackReplaceDto> replaceRecord) {
        if (replaceRecord.size() > 0) {
//            Map<String, String> map = replaceRecord.stream().collect(Collectors.toMap(s -> s.FGBIANHAO, s -> s.FCHEPAI));
            Map<String, String> map = replaceRecord.stream().collect(Collectors.toMap(StackReplaceDto::getFGBIANHAO, StackReplaceDto::getFCHEPAI, (v1, v2) -> v2));

            for (MainDto mainDto : ret) {
                if (map.containsKey(mainDto.getDIANDUI())) {
                    mainDto.setCHEPAI(map.get(mainDto.getDIANDUI()));
                }
            }
        }
    }


    @GetMapping(value = "/record/bykv/download")
    @Log("下载导出")
    @ApiOperation("下载导出")
    public Object downloadPagesByKV(
            HttpServletResponse response,
            KvDto kvDto) throws IOException {
        kvDto.table = PlmConstant.get_K_MAIN_TABLE();
        List<MainDto> ret = dbManager.selectRecordByKv(kvDto);
        DownUtils.downloadMain(ret, response);
        return null;
    }


    @GetMapping(value = "/detail/kv")
    @Log("按kv查询子表详细结果")
    @ApiOperation("按kv查询子表详细结果")
    public Object queryDetailByKV(
            @Validated KvDto kvDto,
            Pageable pageable
    ) {
        try {
           /* KvDto k = new KvDto("name", SecurityUtils.getCurrentUser().getUsername());
            k.table = PlmConstant.get_K_PLM_USER_ALLOWED();*/
            List<String> allowedPart = dbManager.queryUserAllowedPart(PlmConstant.get_K_PLM_USER_ALLOWED(), SecurityUtils.getCurrentUser().getUsername(), "plm");
            if (PlmConstant.K_MEA.equals(kvDto.type)) {
                kvDto.table = PlmConstant.get_K_MEA_TABLE();
                return fillMeaDetail(dbManager.getMeaDetail(kvDto), allowedPart);
            } else if (PlmConstant.K_GZ_PAPER.equals(kvDto.type) || PlmConstant.K_CHANPIN.equals(kvDto.type)) {
                kvDto.table = PlmConstant.K_GZ_PAPER.equals(kvDto.type) ? PlmConstant.get_K_GZ_PAPER_TABLE() : PlmConstant.get_K_CHANPIN_TABLE();
                List<KvDto> list = new ArrayList<>();
                KvDto kvDto1 = new KvDto(kvDto.key, kvDto.value);
                list.add(kvDto1);
                if (StringUtils.isNotEmpty(kvDto.subKey) && StringUtils.isNotEmpty(kvDto.subValue)) {
                    KvDto kvDto2 = new KvDto(kvDto.subKey, kvDto.subValue);
                    list.add(kvDto2);
                }
                kvDto.subList = list;
                //TODO：老的图纸内容
                List<GongzhuangPaperDto> ret = dbManager.getGongZhuangDetail(kvDto);
                //第二步，获取所有的知识库内的文件
                List<KvDto> newKvDtoList = new ArrayList<>();
                KvDto newKvDto1 = new KvDto("firstDir", PlmConstant.K_GZ_PAPER.equals(kvDto.type) ? "工装" : "产品");
                KvDto newKvDto2 = new KvDto("secondDir", kvDto.value);
                newKvDtoList.add(newKvDto1);
                newKvDtoList.add(newKvDto2);
                if (StringUtils.isNotEmpty(kvDto.subKey) && StringUtils.isNotEmpty(kvDto.subValue)) {
                    if (kvDto.subKey.equals("FFUZEREN")) {
                        KvDto newKvDto3 = new KvDto("writer", kvDto.subValue);
                        newKvDtoList.add(newKvDto3);
                    } else if (kvDto.subKey.equals("FGONGXU")) {
                        KvDto newKvDto3 = new KvDto("thirdDir", kvDto.subValue);
                        newKvDtoList.add(newKvDto3);
                    }
                }
                kvDto.table = PlmConstant.get_K_KNOWLEDGE_FILE();
                kvDto.subList = newKvDtoList;
                List<GongzhuangPaperDto> ret2 = dbManager.getNewGongZhuangDetail(kvDto);
                ret2.addAll(ret);
                return mergeFromPart(ret2.size(), PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), ret2), pageable,
                        PlmConstant.K_GZ_PAPER.equals(kvDto.type) ? MergeEnum.GZ_PAPER : MergeEnum.CP_PAPER, allowedPart);

            } else if (PlmConstant.K_SHOUHOU.equals(kvDto.type)) {
                kvDto.table = PlmConstant.get_K_SHOUHOU_TABLE();
                List<ShouHouDto> ret = dbManager.getShouHouDetail(kvDto);
                return mergeFromPart(ret.size(), PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), ret), pageable, MergeEnum.SHOUHOU, allowedPart);
            } else if (PlmConstant.K_XITONG.equals(kvDto.type)
                    || PlmConstant.K_BIP.equals(kvDto.type)) {
                kvDto.table = PlmConstant.get_K_MAIN_TABLE();
                List<MainDto> ret = dbManager.selectRecordByKv(kvDto);
                return fillSystemDetail(ret, allowedPart, kvDto.type);
            } else if (PlmConstant.K_CESHI.equals(kvDto.type)) {
                kvDto.table = PlmConstant.get_K_PLM_STACK_EXP();
                List<ExpStackInfo> ret = dbManager.getStackExpDetail(kvDto);
                return mergeFromPart(ret.size(), PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), ret), pageable, MergeEnum.CESHI, allowedPart);
            } else if (PlmConstant.K_SB_PAPER.equals(kvDto.type)) {
                kvDto.table = PlmConstant.get_K_SB_PAPER_TABLE();
                List<KvDto> list = new ArrayList<>();
                KvDto kvDto1 = new KvDto(kvDto.key, kvDto.value);
                list.add(kvDto1);
                if (StringUtils.isNotEmpty(kvDto.subKey) && StringUtils.isNotEmpty(kvDto.subValue)) {
                    KvDto kvDto2 = new KvDto(kvDto.subKey, kvDto.subValue);
                    list.add(kvDto2);
                }
                kvDto.subList = list;
                int totalCnt = dbManager.getCountByKV(kvDto);
                kvDto.fromIndex = pageable.getPageSize() * pageable.getPageNumber() + 1;
                kvDto.endIndex = pageable.getPageSize() * pageable.getPageNumber() + pageable.getPageSize();
                List<SheBeiPaperDto> ret = dbManager.getSheBeiPaperDetail(kvDto);
                return mergeFromPart(totalCnt, ret, pageable, MergeEnum.SB_PAPER, allowedPart);
            } else if (PlmConstant.K_STACK_REPLACE.equals(kvDto.type)) {
                List<StackReplaceInfo> ret = fixHistoryService.queryByStack(kvDto.value);
                return mergeFromPart(ret.size(), PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), ret), pageable, MergeEnum.STACK_REPLACE, allowedPart);

            } else if (PlmConstant.K_CAR_MILEAGE.equals(kvDto.type)) {
                kvDto.table = PlmConstant.get_K_PLM_CAR_MILEAGE();
                List<CarInfo> ret = dbManager.getCarMileage(kvDto);
                return mergeFromPart(ret.size(), PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), ret), pageable, MergeEnum.CAR_MILEAGE, allowedPart);
            } else if (PlmConstant.K_CAR_CONSUME.equals(kvDto.type)) {
                kvDto.table = PlmConstant.get_K_PLM_CAR_CONSUME();
                List<CarInfo> ret = dbManager.getCarConsume(kvDto);
                return mergeFromPart(ret.size(), PageUtil.toPage(pageable.getPageNumber(), pageable.getPageSize(), ret), pageable, MergeEnum.CAR_CONSUME, allowedPart);

            } else {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            throw new BadRequestException("/detail/bykv发生错误");
        }
    }


}
