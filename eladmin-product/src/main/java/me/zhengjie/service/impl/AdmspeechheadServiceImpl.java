/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import me.zhengjie.Utils.DownExcelUtil;
import me.zhengjie.base.PageRequest;
import me.zhengjie.base.PageResult;
import me.zhengjie.domain.Admspeechhead;
import me.zhengjie.domain.Product;
import me.zhengjie.domain.ProductInfo;
import me.zhengjie.domain.ProductNum;
import me.zhengjie.mapper.AdmspeechheadBatisMapper;
import me.zhengjie.utils.*;
import lombok.RequiredArgsConstructor;
import me.zhengjie.repository.AdmspeechheadRepository;
import me.zhengjie.service.AdmspeechheadService;
import me.zhengjie.service.dto.AdmspeechheadDto;
import me.zhengjie.service.dto.AdmspeechheadQueryCriteria;
import me.zhengjie.service.mapstruct.AdmspeechheadMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author t_k_c
 * @website https://el-admin.vip
 * @description 服务实现
 * @date 2020-11-10
 **/
@Service
@RequiredArgsConstructor
public class AdmspeechheadServiceImpl implements AdmspeechheadService {

    @Resource
    private AdmspeechheadBatisMapper admspeechheadBatisMapper;

    private final AdmspeechheadRepository admspeechheadRepository;
    private final AdmspeechheadMapper admspeechheadMapper;

    @Override
    public Map<String, Object> queryAll(AdmspeechheadQueryCriteria criteria, Pageable pageable) {
        Page<Admspeechhead> page = admspeechheadRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(admspeechheadMapper::toDto));
    }

    @Override
    public List<AdmspeechheadDto> queryAll(AdmspeechheadQueryCriteria criteria) {
        return admspeechheadBatisMapper.getAdmspeechhead(criteria);
    }

    @Override
    public List<AdmspeechheadDto> queryByAdress(AdmspeechheadQueryCriteria criteria) {
        return admspeechheadBatisMapper.getAdmspeechheadByAddress(criteria);
    }

    @Override
    public PageResult findAdmspeechheadByPage(AdmspeechheadQueryCriteria record, PageRequest pageRequest) {
        return PageMyBatisUtil.getPageResult(pageRequest, getPageInfo(record, pageRequest));
    }

    /**
     * 调用分页插件完成分页
     *
     * @param pageRequest
     * @return
     */
    private PageInfo<AdmspeechheadDto> getPageInfo(AdmspeechheadQueryCriteria record, PageRequest pageRequest) {
        int pageNum = pageRequest.getPage();
        int pageSize = pageRequest.getSize();
        PageHelper.startPage(pageNum, pageSize);
        List<AdmspeechheadDto> list = admspeechheadBatisMapper.getAdmspeechheadByAddress(record);
        return new PageInfo<AdmspeechheadDto>(list);
    }

    @Override
    @Transactional
    public AdmspeechheadDto findById(Integer fId) {
        Admspeechhead admspeechhead = admspeechheadRepository.findById(fId).orElseGet(Admspeechhead::new);
        ValidationUtil.isNull(admspeechhead.getFId(), "Admspeechhead", "fId", fId);
        return admspeechheadMapper.toDto(admspeechhead);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int create(AdmspeechheadDto resources) {
        return admspeechheadBatisMapper.insertSelective(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AdmspeechheadDto resources) {
        admspeechheadBatisMapper.updateByPrimaryKeySelective(resources);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer fId : ids) {
            admspeechheadRepository.deleteById(fId);
        }
    }

    @Override
    public void download(List<AdmspeechheadDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AdmspeechheadDto admspeechhead : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户id", admspeechhead.getFUseId());
            map.put("创建时间", admspeechhead.getFDate());
            map.put("归属地", admspeechhead.getFAddress());
            map.put("今日完成内容", admspeechhead.getFToday());
            map.put("明日计划内容", admspeechhead.getFTomorrow());
            map.put("生产日期", admspeechhead.getFProduceDate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void downloadProduct(List<Product> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, Object>> layoutList = new ArrayList<>();
        int i = 0;
        int[] columnArray = {0, 1, 2, 8, 9};
        List<Integer> cellList = new ArrayList<>();
        for (Product product : all) {
            List<ProductInfo> productInfoList = product.getProductList();
            for (int column : columnArray) {
                if (productInfoList.size() > 1) {
                    Map<String, Object> layoutMap = new LinkedHashMap<>();
                    layoutMap.put("firstRow", i + 1);
                    layoutMap.put("lastRow", i + productInfoList.size());
                    layoutMap.put("firstColumn", column);
                    layoutMap.put("lastColumn", column);
                    layoutList.add(layoutMap);
                }
            }
            for (ProductInfo productInfo : productInfoList) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("部门", product.getDeptName());
                map.put("汇报人", product.getUserName());
                map.put("厂址", product.getfAddress());
                map.put("工作内容", productInfo.getfName());
                map.put("当日产量", productInfo.getfOutputquantity().intValue());
                map.put("不良品", productInfo.getfRejectsquantity().intValue());
                map.put("结存", productInfo.getfBalancequantity().intValue());
                map.put("备注", productInfo.getfNote());
                map.put("今日完成内容", product.getfToday());
                map.put("明日计划内容", product.getfTomorrow());
                list.add(map);
            }
            cellList.add(i + 1);
            i = i + productInfoList.size();

        }

        DownExcelUtil.downloadExcel(list, layoutList, cellList, response);
    }

    @Override
    public void downloadProductNum(List<Map<String, Object>> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, Object>> layoutList = new ArrayList<>();
        List<Integer> cellList = new ArrayList<>();
        for (Map<String, Object> product : all) {
            List<ProductNum> productInfoList = (List<ProductNum>) product.get("productInfoList");
            for (ProductNum productInfo : productInfoList) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("日期", product.get("fDate"));
                map.put("地区", product.get("address"));
                map.put("产品型号", productInfo.getModelName());
                map.put("今日组装", productInfo.getAssemblyNum() > 0 ? productInfo.getAssemblyNum() : null);
                map.put("组装结存", productInfo.getAssemblyBalance() > 0 ? productInfo.getAssemblyBalance() : null);
                map.put("今日活化", productInfo.getActivationNum() > 0 ? productInfo.getActivationNum() : null);
                map.put("活化结存", productInfo.getActivationBalance() > 0 ? productInfo.getActivationBalance() : null);
                list.add(map);
            }
        }
        Map<String, Object> layoutMap = new LinkedHashMap<>();
        layoutMap.put("firstRow", 1);
        layoutMap.put("lastRow", 13);
        layoutMap.put("firstColumn", 0);
        layoutMap.put("lastColumn", 0);
        layoutList.add(layoutMap);
        for (int i = 1; i < 13; i++) {
            if (i % 3 != 0) {
                Map<String, Object> layoutMap2 = new LinkedHashMap<>();
                layoutMap2.put("firstRow", i);
                layoutMap2.put("lastRow", i < 9 ? i + 2 : i + 3);
                layoutMap2.put("firstColumn", 1);
                layoutMap2.put("lastColumn", 1);
                i++;
                layoutList.add(layoutMap2);
            }
        }
        DownExcelUtil.downloadExcelByNum(list, layoutList, cellList, response);
    }
}
