package me.zhengjie.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.DailyPlan;
import me.zhengjie.domain.TechniqueInfo;
import me.zhengjie.mapper.TechniqueInfoMapper;
import me.zhengjie.repository.TechniqueInfoRepository;
import me.zhengjie.service.TechniqueInfoService;
import me.zhengjie.service.dto.TechniqueInfoDto;
import me.zhengjie.service.dto.TechniqueInfoQueryCriteria;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author HL
 * @create 2021/4/13 11:48
 */
@Service
@RequiredArgsConstructor
public class TechniqueInfoServiceImpl implements TechniqueInfoService {
    @Resource(name = "techniqueInfoMapperImpl")
    private TechniqueInfoMapper techniqueInfoMapper;

    private final TechniqueInfoRepository techniqueInfoRepository;

    @Override
    public Map<String, Object> queryAll(TechniqueInfoQueryCriteria criteria, Pageable pageable) {
        Page<TechniqueInfo> page = techniqueInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(techniqueInfoMapper::toDto));
    }

    @Override
    public List<TechniqueInfoDto> queryAll(TechniqueInfoQueryCriteria criteria) {
        return techniqueInfoMapper.toDto(techniqueInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));

    }

    @Override
    public TechniqueInfoDto create(TechniqueInfo resources) {
        return techniqueInfoMapper.toDto(techniqueInfoRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TechniqueInfo resources) {
        TechniqueInfo techniqueInfo = techniqueInfoRepository.findById(resources.getId()).orElseGet(TechniqueInfo::new);
        ValidationUtil.isNull(techniqueInfo.getId(), "techniqueInfo", "id", resources.getId());
        techniqueInfo.copy(resources);
        techniqueInfoRepository.save(techniqueInfo);
    }

    @Override
    public void download(HttpServletResponse response, TechniqueInfoQueryCriteria criteria) {
        List<TechniqueInfo> techniqueInfoList = techniqueInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));

        List<Map<String, Object>> list = new ArrayList<>();
        for (TechniqueInfo techniqueInfo : techniqueInfoList) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("一级分类", techniqueInfo.getCategory().getPrimaryType());
            map.put("二级分类", techniqueInfo.getCategory().getSecondaryType());
            map.put("工序代号", techniqueInfo.getWorkpieceNumber());
            map.put("产品代号", techniqueInfo.getProductCode());
            map.put("指导书编号", techniqueInfo.getInstructorNumber());
            map.put("指导书版本", techniqueInfo.getInstructorVersion());
            map.put("工艺人员", techniqueInfo.getTechnologist());
            map.put("工艺变更人员", techniqueInfo.getProcessChanger());
            map.put("工艺变更日期", techniqueInfo.getChangeDate());
            map.put("设备最大制造能力（小时）", techniqueInfo.getEquipmentMaxCapacity());
            map.put("当前最大产能（小时）", techniqueInfo.getCurrentMaxCapacity());
            map.put("工时定额", techniqueInfo.getHourNorm());
            map.put("材料1定额", techniqueInfo.getMaterial1Quota());
            map.put("材料2定额", techniqueInfo.getMaterial2Quota());
            map.put("材料3定额", techniqueInfo.getMaterial3Quota());
            map.put("报工名称", techniqueInfo.getMaterial3Quota());
            list.add(map);
        }
        try {
            FileUtil.downloadExcel(list, response);
        } catch (Exception e) {
        }
    }

    @Override
    public List<TechniqueInfo> getTechniqueInfoByUser(String deptId) {
        List<TechniqueInfo> techniqueInfoList = techniqueInfoRepository.findAll();
        List<TechniqueInfo> techniqueInfos = techniqueInfoList.stream().filter(p -> {
            String deptIds = p.getCategory().getDeptIds();
            String[] deptIdList = deptIds.split(",");
            for(int i=0;i<deptIdList.length;i++){
                if(deptIdList[i].equals(deptId))
                    return true;
            }
            return false;
        }).collect(Collectors.toList());
        return techniqueInfos;
    }
}