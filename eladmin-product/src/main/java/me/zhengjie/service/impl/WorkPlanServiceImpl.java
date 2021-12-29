package me.zhengjie.service.impl;

import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import me.zhengjie.Utils.DownExcelUtil;
import me.zhengjie.base.MergeResult;
import me.zhengjie.domain.*;
import me.zhengjie.mapper.WorkPlanDetailBatisMapper;
import me.zhengjie.repository.WorkPlanDetailOutputRepository;
import me.zhengjie.repository.WorkPlanDetailRepository;
import me.zhengjie.repository.WorkPlanRepository;
import me.zhengjie.service.WorkPlanService;
import me.zhengjie.service.dto.WorkPlanDetailQueryCriteria;
import me.zhengjie.service.dto.WorkPlanGroupDto;
import me.zhengjie.service.dto.WorkPlanQueryCriteria;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HL
 * @create 2021/12/13 10:28
 */
@Service
@RequiredArgsConstructor
public class WorkPlanServiceImpl implements WorkPlanService {

    private final WorkPlanRepository workPlanRepository;

    private final WorkPlanDetailRepository workPlanDetailRepository;

    private final WorkPlanDetailOutputRepository workPlanDetailOutputRepository;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<WorkPlan> queryAllWorkPlan() {
        List<WorkPlan> workPlans = workPlanRepository.findAll();
        workPlans = workPlans.stream().sorted(Comparator.comparing(a -> a.getId(), Comparator.reverseOrder())).collect(Collectors.toList());

        return workPlans;
    }

    @Override
    public List<WorkPlanDetailOutputType> queryAllOutPut() {
        return workPlanDetailOutputRepository.findAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createWorkPlan(WorkPlanGroupDto resources) {
        WorkPlan workPlan = new WorkPlan();
        workPlan.setPlanName(resources.getPlanName());
        workPlan.setPlanCode(resources.getPlanCode());
        WorkPlan workPlan1 = workPlanRepository.save(workPlan);

        List<WorkPlanDetail> workPlanDetailList = resources.getWorkPlanDetails();
        workPlanDetailList.stream().forEach(a -> {
            a.setWorkPlan(workPlan1);
        });

        workPlanDetailRepository.saveAll(workPlanDetailList);
        return workPlan1.getId();
    }

    @Override
    public WorkPlanGroupDto isExist(WorkPlanGroupDto resources) {
        WorkPlanQueryCriteria workPlanQueryCriteria = new WorkPlanQueryCriteria();
        workPlanQueryCriteria.setPlanName(resources.getPlanName());
        List<WorkPlan> workPlans = workPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, workPlanQueryCriteria, criteriaBuilder));
        if (workPlans != null && workPlans.size() > 0) {
            WorkPlanGroupDto workPlanGroup = new WorkPlanGroupDto();
            workPlanGroup.setExist("NAME_EXIST");
            return workPlanGroup;
        }

        WorkPlanQueryCriteria criteria1 = new WorkPlanQueryCriteria();
        criteria1.setPlanCode(resources.getPlanCode());
        List<WorkPlan> workPlans1 = workPlanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria1, criteriaBuilder));
        if (workPlans1 != null && workPlans1.size() > 0) {
            WorkPlanGroupDto workPlanGroup = new WorkPlanGroupDto();
            workPlanGroup.setExist("CODE_EXIST");
            return workPlanGroup;
        }

        return null;
    }

    @Override
    public WorkPlanGroupDto getWorkPlanDetailByPlanId(Integer id) {
        WorkPlanDetailQueryCriteria criteria = new WorkPlanDetailQueryCriteria();
        criteria.setWorkPlanId(id);
        List<WorkPlanDetail> workPlanDetails = getWorkPlanDetails(criteria);
        List<WorkPlanGroupDto> workPlanGroupDtos = detailToGroupDto(workPlanDetails);
        return workPlanGroupDtos != null && workPlanGroupDtos.size() >= 0 ? workPlanGroupDtos.get(0) : null;
    }

    private List<WorkPlanGroupDto> detailToGroupDto(List<WorkPlanDetail> workPlanDetails) {
        Map<String, List<WorkPlanDetail>> groupByMap = workPlanDetails.stream().collect(Collectors.groupingBy(a -> a.getWorkPlan().getPlanName()));
        List<WorkPlanGroupDto> workPlanGroupDtos = groupByMap.entrySet().stream().map(e -> new WorkPlanGroupDto(e.getKey(), e.getValue().get(0).getWorkPlan().getPlanCode(), e.getValue(), null)).collect(Collectors.toList());
        workPlanGroupDtos.stream().forEach(a -> a.setWorkPlanDetails(a.getWorkPlanDetails().stream().sorted(Comparator.comparing(b -> b.getDetailCode())).collect(Collectors.toList())));
        return workPlanGroupDtos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateWorkPlan(WorkPlanGroupDto resources) {
        WorkPlan workPlan = new WorkPlan();
        List<WorkPlanDetail> workPlanDetailList = new ArrayList<>();
        List<WorkPlanDetail> workPlanDetails = resources.getWorkPlanDetails();
        for (WorkPlanDetail workPlanDetail : workPlanDetails) {
            if (workPlanDetail.getId() != null && !"".equals(workPlanDetail.getId())) {
                workPlan = workPlanDetail.getWorkPlan();
                WorkPlanDetail workPlanDetail1 = workPlanDetailRepository.findById(workPlanDetail.getId()).orElseGet(WorkPlanDetail::new);
                if (!workPlanDetail.equals(workPlanDetail1)) {
                    workPlanDetail.setId(null);
                    workPlanDetailRepository.save(workPlanDetail);
                }
            } else {
                workPlanDetailList.add(workPlanDetail);
            }
        }
        WorkPlan finalWorkPlan = workPlan;
        workPlanDetails.stream().forEach(a -> {
            a.setWorkPlan(finalWorkPlan);
        });
        workPlanDetailRepository.saveAll(workPlanDetailList);
        return workPlan.getId();
    }

    @Override
    public List<WorkPlanDetail> getWorkPlanDetails(WorkPlanDetailQueryCriteria criteria) {
        List<WorkPlanDetail> workPlanDetailList = workPlanDetailRepository.findAll();

        Map<String, WorkPlanDetail> map = workPlanDetailList.stream().collect(Collectors.groupingBy(a -> a.getWorkPlan().getId().toString() + a.getDetailCode(), Collectors.collectingAndThen(Collectors.reducing((o1, o2) ->
                Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId())) > 0 ? o1 : o2), Optional::get)));

        List<WorkPlanDetail> workPlanDetailList1 = new ArrayList<>();
        for (String key : map.keySet()) {
            workPlanDetailList1.add(map.get(key));
        }

        if (criteria.getWorkPlanId() != null && !"".equals(criteria.getWorkPlanId())) {
            workPlanDetailList1 = workPlanDetailList1.stream().filter(a -> a.getWorkPlan().getId() == criteria.getWorkPlanId()).collect(Collectors.toList());
        }

        if (criteria.getDetailName() != null && !"".equals(criteria.getDetailName())) {
            workPlanDetailList1 = workPlanDetailList1.stream().filter(a -> a.getDetailName().contains(criteria.getDetailName())).collect(Collectors.toList());
        }

        if (criteria.getPlanStartDate() != null && criteria.getPlanStartDate().size() > 1) {
            workPlanDetailList1 = workPlanDetailList1.stream().filter(a -> a.getPlanStartDate() != null && a.getPlanStartDate().compareTo(criteria.getPlanStartDate().get(0)) >= 0 && a.getPlanStartDate().compareTo(criteria.getPlanStartDate().get(1)) <= 0).collect(Collectors.toList());
        }

        if (criteria.getPlanFinishDate() != null && criteria.getPlanFinishDate().size() > 1) {
            workPlanDetailList1 = workPlanDetailList1.stream().filter(a -> a.getPlanFinishDate() != null && a.getPlanFinishDate().compareTo(criteria.getPlanFinishDate().get(0)) >= 0 && a.getPlanFinishDate().compareTo(criteria.getPlanFinishDate().get(1)) <= 0).collect(Collectors.toList());
        }
        if (criteria.getStatus() != null && !"".equals(criteria.getStatus())) {
            if (criteria.getStatus().equals("finish")) {
                workPlanDetailList1 = workPlanDetailList1.stream().filter(a -> a.getActFinishDate() != null && a.getActFinishDate().compareTo(dateFormat.format(new Date())) <= 0).collect(Collectors.toList());
            } else if (criteria.getStatus().equals("unfinish")) {
                workPlanDetailList1 = workPlanDetailList1.stream().filter(a -> a.getActFinishDate() == null || a.getActFinishDate().compareTo(dateFormat.format(new Date())) > 0).collect(Collectors.toList());
            }
        }
        return workPlanDetailList1;
    }

    @Override
    public Object findWorkPlanDetails(WorkPlanDetailQueryCriteria criteria, Pageable pageable) {
        List<WorkPlanDetail> workPlanDetailList1 = getWorkPlanDetails(criteria);
        workPlanDetailList1 = workPlanDetailList1.stream().sorted(Comparator.comparing(a -> a.getWorkPlan().getId(), Comparator.reverseOrder())).collect(Collectors.toList());
        if (workPlanDetailList1 != null && workPlanDetailList1.size() > 0) {

            Map<Integer, List<WorkPlanDetail>> groupByMap = workPlanDetailList1.stream().collect(Collectors.groupingBy(a -> a.getWorkPlan().getPlanCode()));
            Map<Integer, List<WorkPlanDetail>> map = sortByKey(groupByMap);
            List<WorkPlanGroupDto> workPlanGroupDtos = map.entrySet().stream().map(e -> new WorkPlanGroupDto(e.getValue().get(0).getWorkPlan().getPlanName(), e.getValue().get(0).getWorkPlan().getPlanCode(), e.getValue(), null)).collect(Collectors.toList());

            workPlanGroupDtos.stream().forEach(a -> a.setWorkPlanDetails(a.getWorkPlanDetails().stream().sorted(Comparator.comparing(b -> b.getDetailCode())).collect(Collectors.toList())));
            List<WorkPlanGroupDto> workPlanDetails = new ArrayList<>();
            if (workPlanGroupDtos.size() < pageable.getPageNumber() * pageable.getPageSize()) {
                workPlanDetails = workPlanGroupDtos;
            } else if (workPlanGroupDtos.size() > pageable.getPageNumber() * pageable.getPageSize() && workPlanGroupDtos.size() < (pageable.getPageNumber() + 1) * pageable.getPageSize()) {
                workPlanDetails = workPlanGroupDtos.subList(pageable.getPageNumber() * pageable.getPageSize(), workPlanGroupDtos.size());
            } else if (workPlanGroupDtos.size() > pageable.getPageNumber() * pageable.getPageSize() && workPlanGroupDtos.size() >= (pageable.getPageNumber() + 1) * pageable.getPageSize()) {
                workPlanDetails = workPlanGroupDtos.subList(pageable.getPageNumber() * pageable.getPageSize(), (pageable.getPageNumber() + 1) * pageable.getPageSize());
            }

            MergeResult mergeResult = new MergeResult();
            mergeResult.totalElements = workPlanGroupDtos.size();
            mergeResult.totalPages = workPlanGroupDtos.size() % pageable.getPageSize() == 0 ? workPlanGroupDtos.size() / pageable.getPageSize() : workPlanGroupDtos.size() / pageable.getPageSize() + 1;
            mergeResult.currentPage = pageable.getPageNumber();
            mergeResult.size = pageable.getPageSize();
            mergeResult.content = PageUtil.toPage(0, pageable.getPageSize(), workPlanDetails);
            return mergeResult;
        }
        MergeResult mergeResult = new MergeResult();
        return mergeResult;
    }

    @Override
    public void downloadWorkPlanDetails(HttpServletResponse response, WorkPlanDetailQueryCriteria criteria) throws IOException {
        List<WorkPlanDetail> workPlanDetailList1 = getWorkPlanDetails(criteria);
        Map<Integer, List<WorkPlanDetail>> groupByMap = workPlanDetailList1.stream().collect(Collectors.groupingBy(a -> a.getWorkPlan().getPlanCode()));
        Map<Integer, List<WorkPlanDetail>> map1 = sortByKey(groupByMap);
        List<WorkPlanGroupDto> workPlanGroupDtos = map1.entrySet().stream().map(e -> new WorkPlanGroupDto(e.getValue().get(0).getWorkPlan().getPlanName(), e.getValue().get(0).getWorkPlan().getPlanCode(), e.getValue(), null)).collect(Collectors.toList());
        workPlanGroupDtos.stream().forEach(a -> a.setWorkPlanDetails(a.getWorkPlanDetails().stream().sorted(Comparator.comparing(b -> b.getDetailCode())).collect(Collectors.toList())));


        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, Object>> layoutList = new ArrayList<>();
        List<Integer> cellList = new ArrayList<>();
        int size = 0;
        List<Integer> integerList = new ArrayList<>();
        for (WorkPlanGroupDto workPlanGroupDto : workPlanGroupDtos) {
            List<WorkPlanDetail> workPlanDetails = workPlanGroupDto.getWorkPlanDetails();
            integerList.add(size + 1);
            size = size + workPlanDetails.size();
            integerList.add(size);
            for (WorkPlanDetail workPlanDetail : workPlanDetails) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("任务名称", workPlanGroupDto.getPlanName());
                map.put("版本号", workPlanGroupDto.getPlanCode() + "." + workPlanDetail.getDetailCode());
                map.put("任务描述", workPlanDetail.getDetailName());
                map.put("负责人", workPlanDetail.getDutyPerson());
                map.put("结果出形式", workPlanDetail.getWorkPlanDetailOutputType().getOutputResult());
                map.put("计划开始时间", workPlanDetail.getPlanStartDate());
                map.put("计划完成时间", workPlanDetail.getPlanFinishDate());
                map.put("实际完成时间", workPlanDetail.getActFinishDate());
                map.put("备注", workPlanDetail.getRemark());
                list.add(map);
            }
        }
        for (int i = 0; i < integerList.size() - 1; i++) {
            if (integerList.get(i + 1) > integerList.get(i)) {
                Map<String, Object> layoutMap2 = new LinkedHashMap<>();
                layoutMap2.put("firstRow", integerList.get(i));
                layoutMap2.put("lastRow", integerList.get(i + 1));
                layoutMap2.put("firstColumn", 0);
                layoutMap2.put("lastColumn", 0);
                layoutList.add(layoutMap2);
            }
            i++;

        }
        DownExcelUtil.downloadExcelByNum(list, layoutList, cellList, response);

    }

    private Map<Integer, List<WorkPlanDetail>> sortByKey(Map<Integer, List<WorkPlanDetail>> map) {
        Map<Integer, List<WorkPlanDetail>> result = new LinkedHashMap<>(map.size());
        map.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered(e -> {
            result.put(e.getKey(), e.getValue());
        });
        return result;
    }

    @Override
    public void deleteWorkPlanDetail(Integer id) {
        workPlanDetailRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteWorkPlan(Integer id) {
        WorkPlanDetailQueryCriteria workPlanDetailQueryCriteria = new WorkPlanDetailQueryCriteria();
        workPlanDetailQueryCriteria.setWorkPlanId(id);
        List<WorkPlanDetail> workPlanDetails = workPlanDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, workPlanDetailQueryCriteria, criteriaBuilder));
        workPlanDetailRepository.deleteAll(workPlanDetails);
        workPlanRepository.deleteById(id);
    }

    @Override
    public Object getWorkPlanDetailHistory(Integer id) {
        WorkPlanDetail workPlanDetail = workPlanDetailRepository.findById(id).orElseGet(WorkPlanDetail::new);
        WorkPlanGroupDto workPlanGroupDto = new WorkPlanGroupDto();
        if (workPlanDetail != null) {
            WorkPlanDetailQueryCriteria workPlanDetailQueryCriteria = new WorkPlanDetailQueryCriteria();
            workPlanDetailQueryCriteria.setWorkPlanId(workPlanDetail.getWorkPlan().getId());
            workPlanDetailQueryCriteria.setDetailCode(workPlanDetail.getDetailCode());
            List<WorkPlanDetail> workPlanDetails = workPlanDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, workPlanDetailQueryCriteria, criteriaBuilder));
            if (workPlanDetails != null && workPlanDetails.size() > 0) {
                workPlanGroupDto.setPlanName(workPlanDetails.get(0).getWorkPlan().getPlanName());
                workPlanDetails = workPlanDetails.stream().sorted(Comparator.comparing(WorkPlanDetail::getId, Comparator.reverseOrder())).collect(Collectors.toList());
                workPlanGroupDto.setWorkPlanDetails(workPlanDetails);
            }
        }
        return workPlanGroupDto;
    }

}
