package me.zhengjie.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.StackInfo;
import me.zhengjie.domain.UpdateStack;
import me.zhengjie.repository.StackInfoRepository;
import me.zhengjie.repository.UpdateStackRepository;
import me.zhengjie.service.UpdateStackService;
import me.zhengjie.service.dto.StackInfoQueryCriteria;
import me.zhengjie.service.dto.UpdateStackDto;
import me.zhengjie.service.dto.UpdateStackQueryCriteria;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HL
 * @create 2021/5/10 9:42
 */
@Service
@RequiredArgsConstructor
public class UpdateStackServiceImpl implements UpdateStackService {
    private final UpdateStackRepository updateStackRepository;

    private final StackInfoRepository stackInfoRepository;

    public UpdateStackDto queryAll(UpdateStackQueryCriteria criteria, Integer page, int pageSize) {
        UpdateStackDto updateStackDto = new UpdateStackDto();
        //查找换堆记录
        List<UpdateStack> updateStacks = updateStackRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));

        //查找初始电堆和车辆类型
        StackInfoQueryCriteria stackInfoQueryCriteria = new StackInfoQueryCriteria();
        stackInfoQueryCriteria.setFNumber(criteria.getFCHEPAI());
        List<StackInfo> stackInfos = stackInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, stackInfoQueryCriteria, criteriaBuilder));
        if (stackInfos != null && stackInfos.size() > 0) {
            StackInfo stackInfo = stackInfos.get(0);
            updateStackDto.setVehicleType(stackInfo.getFleixing());
            updateStackDto.setStackNumber(stackInfo.getFdiandui());
        }

        //查找最新电堆
        UpdateStackQueryCriteria updateStackQueryCriteria = new UpdateStackQueryCriteria();
        updateStackQueryCriteria.setFCHEPAI(criteria.getFCHEPAI());
        List<UpdateStack> updateStackList = updateStackRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, updateStackQueryCriteria, criteriaBuilder));
        if (updateStackList != null && updateStackList.size() > 0) {
            updateStackList = updateStackList.stream().sorted(Comparator.comparing(UpdateStack::getFDATE).reversed()).collect(Collectors.toList());
//            Collections.reverse(updateStackList);
            UpdateStack updateStack = updateStackList.get(0);
            updateStackDto.setStackNumber(updateStack.getFGBIANHAO());
        }

        updateStacks = updateStacks.stream().sorted(Comparator.comparing(UpdateStack::getFDATE, Comparator.reverseOrder())).collect(Collectors.toList());

        int totalPages = updateStacks.size() % pageSize == 0 ? updateStacks.size() / pageSize : updateStacks.size() / pageSize + 1;
        updateStackDto.setCurrentPage(page + 1);
        updateStackDto.setTotalElements(updateStacks.size());
        updateStackDto.setTotalPages(totalPages);
        updateStackDto.setSize(pageSize);
        updateStackDto.setUpdateStackList(PageUtil.toPage(page, pageSize, updateStacks));

        return updateStackDto;
    }

    @Override
    public List<UpdateStack> findUpdateStackByDate(UpdateStackQueryCriteria criteria) {
        List<UpdateStack> updateStacks = updateStackRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return updateStacks;
    }

}
