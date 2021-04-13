package me.zhengjie.mybatis;


import lombok.RequiredArgsConstructor;
import me.zhengjie.Doc.PlmFile;
import me.zhengjie.domain.ExpStackInfo;
import me.zhengjie.dto.*;
import me.zhengjie.dto.car.CarInfo;
import me.zhengjie.mapper.PlmMapper;
import me.zhengjie.request.KvDto;
import me.zhengjie.base.ResKv;
import me.zhengjie.utils.RetryT;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static me.zhengjie.constant.PlmConstant.get_K_SHOUHOU_GENHUAN;

@Component
@RequiredArgsConstructor
public class DbManager {
    @Resource
    private PlmMapper plmMapper;

    public List<MainDto> selectRecordByKv (KvDto kvDto) {
        try{
            return  new RetryT<List<MainDto>>(){
                @Override
                protected  List<MainDto> doAction() throws Exception {
                    return plmMapper.selectRecordByKv(kvDto);
                }
            }.execute();
        }catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public int getCountByKV (final KvDto kvDto) {
        try{
            return  new RetryT<Integer>() {
                @Override
                protected Integer doAction() throws Exception {
                    return plmMapper.getCountByKV(kvDto);
                }
            }.execute();
        }catch (Exception e) {
            return 0;
        }
    }

    public List<ResKv> getSelectItem (final KvDto kvDto) {
        try{
            return  new RetryT<List<ResKv>>(){
                @Override
                protected List<ResKv> doAction() throws Exception {
                    return plmMapper.getSelectItem(kvDto);
                }
            }.execute();
        }catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<String> getSelectItemValue (final KvDto kvDto) {
        try{
            return  new RetryT<List<String>>(){
                @Override
                protected List<String> doAction() throws Exception {
                    return plmMapper.getSelectItemValue(kvDto);
                }
            }.execute();
        }catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<MeaDto> getMeaDetail (final KvDto kvDto) {
        try{
            return  new RetryT<List<MeaDto>>(){
                @Override
                protected List<MeaDto> doAction() throws Exception {
                    return plmMapper.getMeaDetail(kvDto);
                }
            }.execute();
        }catch (Exception e) {
            return new ArrayList<>();
        }
    }


    public List<GongzhuangPaperDto> getGongZhuangDetail(final KvDto kvDto) {
        try{
            return  new RetryT<List<GongzhuangPaperDto>>(){
                @Override
                protected List<GongzhuangPaperDto> doAction() throws Exception {
                    return plmMapper.getGongZhuangDetail(kvDto);
                }
            }.execute();
        }catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<GongzhuangPaperDto> getNewGongZhuangDetail(final KvDto kvDto) {
        try{
            return  new RetryT<List<GongzhuangPaperDto>>(){
                @Override
                protected List<GongzhuangPaperDto> doAction() throws Exception {
                    return plmMapper.getNewGongZhuangDetail(kvDto);
                }
            }.execute();
        }catch (Exception e) {
            return new ArrayList<>();
        }
    }



    public List<ShouHouDto> getShouHouDetail(final KvDto kvDto) {
        try{
            return  new RetryT<List<ShouHouDto>>(){
                @Override
                protected List<ShouHouDto> doAction() throws Exception {
                   return plmMapper.getShouHouDetail(kvDto);
                }
            }.execute();
        }catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<SheBeiPaperDto> getSheBeiPaperDetail(final KvDto kvDto) {
        try{
            return  new RetryT<List<SheBeiPaperDto>>(){
                @Override
                protected List<SheBeiPaperDto> doAction() throws Exception {
                    return plmMapper.getSheBeiPaperDetail(kvDto);
                }
            }.execute();
        }catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public int insertDoc(final PlmFile uploadFile) {
        try {
            int count = new RetryT<Integer>() {
                @Override
                protected Integer doAction() throws Exception {
                    return plmMapper.insertDoc(uploadFile);
                }
            }.execute();
            return  count;
        } catch (Exception e) {
            return -1;
        }
    }


    public List<PlmFile> queryPaper2(final KvDto kvDto) {
        try{
            return  new RetryT<List<PlmFile>>(){
                @Override
                protected List<PlmFile> doAction() throws Exception {
                    return plmMapper.queryPaper2(kvDto);
                }
            }.execute();
        }catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<PlmFile> queryPaper(final PaperDto paperDto) {
        try{
            return  new RetryT<List<PlmFile>>(){
                @Override
                protected List<PlmFile> doAction() throws Exception {
                  return plmMapper.queryPaper(paperDto);
                }
            }.execute();
        }catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public int insertFileLog(final FileLogDto fileLogDto) {
        try {
            int count = new RetryT<Integer>() {
                @Override
                protected Integer doAction() throws Exception {
                    return plmMapper.insertFileLog(fileLogDto);
                }
            }.execute();
            return  count;
        } catch (Exception e) {
            return -1;
        }
    }

    public List<String> queryUserAllowedPart(final String table ,final String name, final String kind) {
        try{
            return  new RetryT<List<String>>(){
                @Override
                protected List<String> doAction() throws Exception {
                    return plmMapper.queryAllowedPart(table, name,kind);
                }
            }.execute();
        }catch (Exception e) {
            return new ArrayList<>();
        }

    }

    public List<ExpStackInfo> getStackExpDetail(final KvDto kvDto) {
        try{
            return  new RetryT<List<ExpStackInfo>>(){
                @Override
                protected List<ExpStackInfo> doAction() throws Exception {
                    return plmMapper.getStackExpDetail(kvDto);
                }
            }.execute();
        }catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<StackReplaceDto> getShouHouDetailByStack(Set<String> allDianDui) {
        try{
            return  new RetryT<List<StackReplaceDto>>(){
                @Override
                protected List<StackReplaceDto> doAction() throws Exception {
                    return plmMapper.getStackNewestShouHou(get_K_SHOUHOU_GENHUAN(),  allDianDui);
                }
            }.execute();
        }catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<CarInfo> getCarMileage(final KvDto kvDto) {
        try{
            return  new RetryT<List<CarInfo>>(){
                @Override
                protected List<CarInfo> doAction() throws Exception {
                    return plmMapper.getCarMileage(kvDto);
                }
            }.execute();
        }catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<CarInfo> getCarConsume(KvDto kvDto) {
        try{
            return  new RetryT<List<CarInfo>>(){
                @Override
                protected List<CarInfo> doAction() throws Exception {
                    return plmMapper.getCarConsume(kvDto);
                }
            }.execute();
        }catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
