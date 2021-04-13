package me.zhengjie.mapper;


import me.zhengjie.Doc.PlmFile;
import me.zhengjie.domain.ExpStackInfo;
import me.zhengjie.dto.*;
import me.zhengjie.dto.car.CarInfo;
import me.zhengjie.request.KvDto;
import me.zhengjie.base.ResKv;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;


public interface PlmMapper {
    List<MainDto> selectRecordByKv (KvDto kvDto);

    int getCountByKV (final KvDto kvDto);

    List<ResKv> getSelectItem (final KvDto kvDto);

    List<String> getSelectItemValue (final KvDto kvDto);

    List<MeaDto> getMeaDetail (final KvDto kvDto);

    List<GongzhuangPaperDto> getGongZhuangDetail(final KvDto kvDto);

    List<GongzhuangPaperDto> getNewGongZhuangDetail(final KvDto kvDto);

    List<ShouHouDto> getShouHouDetail(final KvDto kvDto);

    List<SheBeiPaperDto> getSheBeiPaperDetail(final KvDto kvDto);

    int insertDoc(final PlmFile uploadFile);

    List<PlmFile> queryPaper2(final KvDto kvDto);

    List<PlmFile> queryPaper(final PaperDto paperDto);

    int insertFileLog(final FileLogDto fileLogDto);

    List<String> queryAllowedPart(@Param("table") String table, @Param("name") String name,@Param("kind") String kind);

    List<ExpStackInfo> getStackExpDetail(final KvDto kvDto);

    List<StackReplaceDto> getStackNewestShouHou(@Param("table") String table, @Param("code") Set<String> code);

    List<CarInfo> getCarMileage(final KvDto kvDto);

    List<CarInfo> getCarConsume(final KvDto kvDto);

}
