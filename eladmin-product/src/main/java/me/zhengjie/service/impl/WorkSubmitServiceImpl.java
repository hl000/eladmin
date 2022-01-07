package me.zhengjie.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.base.MergeResult;
import me.zhengjie.domain.WorkCompletQtySubmit;
import me.zhengjie.domain.WorkSubmitTimeList;
import me.zhengjie.domain.WorkWorkingProcedure;
import me.zhengjie.mapper.InventoryMapper;
import me.zhengjie.mapper.WorkListMapper;
import me.zhengjie.repository.WorkCompletQtySubmitRepository;
import me.zhengjie.repository.WorkSubmitTimeListRepository;
import me.zhengjie.repository.WorkWorkingProcedureRepository;
import me.zhengjie.service.WorkSubmitService;
import me.zhengjie.service.dto.InventoryDto;
import me.zhengjie.service.dto.WorkCompletQtySubmitQueryCriteria;
import me.zhengjie.service.dto.WorkListMapDto;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HL
 * @create 2021/12/29 15:17
 */
@Service
@RequiredArgsConstructor
public class WorkSubmitServiceImpl implements WorkSubmitService {

    private final WorkSubmitTimeListRepository workSubmitTimeListRepository;

    private final WorkWorkingProcedureRepository workWorkingProcedureRepository;

    private final WorkCompletQtySubmitRepository workCompletQtySubmitRepository;

    @Resource
    private final WorkListMapper workListMapper;

    @Resource
    private final InventoryMapper inventoryMapper;

    @Override
    public List<WorkSubmitTimeList> queryAllTimeList() {
        List<WorkSubmitTimeList> workSubmitTimeLists = workSubmitTimeListRepository.findAll();
        workSubmitTimeLists = workSubmitTimeLists.stream().filter(a -> a.getFIsDeleted() == 0).collect(Collectors.toList());
        return workSubmitTimeLists;
    }

    @Override
    public List<WorkWorkingProcedure> queryAllProcedure() {
        List<WorkWorkingProcedure> workWorkingProcedures = workWorkingProcedureRepository.findAll();
        workWorkingProcedures = workWorkingProcedures.stream().filter(a -> a.getFIsDeleted() == 0).collect(Collectors.toList());
        return workWorkingProcedures;
    }

    @Override
    public Object getWorkList(String workShop, String fArcName, String startDate, String endDate, String workOrder, Pageable pageable) {
        List<WorkListMapDto> workListDtoList1 = findWorkListMapDto(workShop, fArcName, startDate, endDate, workOrder);

        if (workListDtoList1 != null && workListDtoList1.size() > 0) {
            workListDtoList1 = workListDtoList1.stream().sorted(Comparator.comparing(WorkListMapDto::getFSubDate, Comparator.reverseOrder()).thenComparing(a -> a.getFWorkOrder())).collect(Collectors.toList());

            List<WorkListMapDto> workListDtoList = new ArrayList<>();
            if (workListDtoList1.size() < pageable.getPageNumber() * pageable.getPageSize()) {
                workListDtoList = workListDtoList1;
            } else if (workListDtoList1.size() > pageable.getPageNumber() * pageable.getPageSize() && workListDtoList1.size() < (pageable.getPageNumber() + 1) * pageable.getPageSize()) {
                workListDtoList = workListDtoList1.subList(pageable.getPageNumber() * pageable.getPageSize(), workListDtoList1.size());
            } else if (workListDtoList1.size() > pageable.getPageNumber() * pageable.getPageSize() && workListDtoList1.size() >= (pageable.getPageNumber() + 1) * pageable.getPageSize()) {
                workListDtoList = workListDtoList1.subList(pageable.getPageNumber() * pageable.getPageSize(), (pageable.getPageNumber() + 1) * pageable.getPageSize());
            }

            MergeResult mergeResult = new MergeResult();
            mergeResult.totalElements = workListDtoList1.size();
            mergeResult.totalPages = workListDtoList1.size() % pageable.getPageSize() == 0 ? workListDtoList1.size() / pageable.getPageSize() : workListDtoList1.size() / pageable.getPageSize() + 1;
            mergeResult.currentPage = pageable.getPageNumber();
            mergeResult.size = pageable.getPageSize();
            mergeResult.content = PageUtil.toPage(0, pageable.getPageSize(), workListDtoList);
            return mergeResult;
        }
        MergeResult mergeResult = new MergeResult();
        return mergeResult;
    }

    private List<WorkListMapDto> findWorkListMapDto(String workShop, String fArcName, String startDate, String endDate, String workOrder) {
        List<WorkListMapDto> workListDtoList1 = new ArrayList<>();
        List<Map<String, Object>> mapList = workListMapper.findWorkList(workShop, fArcName, startDate, endDate, workOrder);
        for (Map<String, Object> map : mapList) {
            WorkListMapDto workListDto = new WorkListMapDto();
            Map<String, Integer> map1 = new HashMap<>();
            Integer total = 0;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if ("FSub_Date".equals(entry.getKey())) {
                    workListDto.setFSubDate(entry.getValue().toString());
                } else if ("FArc_Name".equals(entry.getKey())) {
                    workListDto.setFArcName(entry.getValue().toString());
                } else if ("FWork_Order".equals(entry.getKey())) {
                    workListDto.setFWorkOrder(entry.getValue().toString());
                } else if ("FInv_Code".equals(entry.getKey())) {
                    workListDto.setFInvCode(entry.getValue().toString());
                } else if ("FInv_Name".equals(entry.getKey())) {
                    workListDto.setFInvName(entry.getValue().toString());
                } else if ("FInv_Std".equals(entry.getKey())) {
                    workListDto.setFInvStd(entry.getValue().toString());
                } else {
                    map1.put(entry.getKey(), Integer.valueOf(entry.getValue().toString()));
                    total = total + Integer.valueOf(entry.getValue().toString());
                }
            }
            workListDto.setMap(map1);
            workListDto.setTotal(total);
            workListDtoList1.add(workListDto);
        }
        return workListDtoList1;
    }

    @Override
    public Object createWorkSubmit(WorkCompletQtySubmit resources) {
        WorkCompletQtySubmitQueryCriteria criteria = new WorkCompletQtySubmitQueryCriteria();
        criteria.setFCompleteTimeId(resources.getWorkSubmitTimeList().getId());
        criteria.setFArcID(resources.getWorkWorkingProcedure().getId());
        criteria.setFInvCode(resources.getFInvCode());
        criteria.setFInvName(resources.getFInvName());
        criteria.setFInvStd(resources.getFInvStd());
        criteria.setFWorkOrder(resources.getFWorkOrder());
        criteria.setFSubDate(resources.getFSubDate());
        List<WorkCompletQtySubmit> workCompletQtySubmits = workCompletQtySubmitRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        if (workCompletQtySubmits == null || workCompletQtySubmits.size() == 0) {
            return workCompletQtySubmitRepository.save(resources);
        }
        return "DATA_EXIST";
    }

    @Override
    public Object getAllWorkSubmit(WorkCompletQtySubmitQueryCriteria criteria, Pageable pageable) {
        Page<WorkCompletQtySubmit> page = workCompletQtySubmitRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return page;
    }

    @Override
    public Object updateWorkSubmit(WorkCompletQtySubmit resources) {
        WorkCompletQtySubmit workCompletQtySubmit = workCompletQtySubmitRepository.findById(resources.getId()).orElseGet(WorkCompletQtySubmit::new);
        if (workCompletQtySubmit == null || workCompletQtySubmit.getId() == null) {
            return "DATA_NOT_FOUND";
        }

        WorkCompletQtySubmitQueryCriteria criteria = new WorkCompletQtySubmitQueryCriteria();
        criteria.setFCompleteTimeId(resources.getWorkSubmitTimeList().getId());
        criteria.setFArcID(resources.getWorkWorkingProcedure().getId());
        criteria.setFInvCode(resources.getFInvCode());
        criteria.setFInvName(resources.getFInvName());
        criteria.setFInvStd(resources.getFInvStd());
        criteria.setFWorkOrder(resources.getFWorkOrder());
        criteria.setFSubDate(resources.getFSubDate());
        List<WorkCompletQtySubmit> workCompletQtySubmits = workCompletQtySubmitRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        if (workCompletQtySubmits != null && workCompletQtySubmits.size() > 0 && workCompletQtySubmits.get(0).getId() != workCompletQtySubmit.getId()) {
            return "DATA_EXIST";
        }

        workCompletQtySubmit.setFCompleteQty(resources.getFCompleteQty());
        workCompletQtySubmit.setFInvCode(resources.getFInvCode());
        workCompletQtySubmit.setFInvName(resources.getFInvName());
        workCompletQtySubmit.setFInvStd(resources.getFInvStd());
        workCompletQtySubmit.setFWorkOrder(resources.getFWorkOrder());
        workCompletQtySubmit.setWorkSubmitTimeList(resources.getWorkSubmitTimeList());
        workCompletQtySubmit.setWorkWorkingProcedure(resources.getWorkWorkingProcedure());
        workCompletQtySubmit.setFSubDate(resources.getFSubDate());
        return workCompletQtySubmitRepository.save(workCompletQtySubmit);
    }

    @Override
    public Object getInventoryByWorkOrder(String workOrder) {
        List<InventoryDto> inventoryDtos = inventoryMapper.getInventoryByWorkOrder(workOrder);
        return inventoryDtos;
    }

    @Override
    public Object getWorkSubmitByArcId(Integer arcId) {
        WorkCompletQtySubmitQueryCriteria criteria = new WorkCompletQtySubmitQueryCriteria();
        criteria.setFArcID(arcId);
        List<WorkCompletQtySubmit> workCompletQtySubmits = workCompletQtySubmitRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        if (workCompletQtySubmits != null && workCompletQtySubmits.size() > 0) {
            WorkCompletQtySubmit workCompletQtySubmit = workCompletQtySubmits.stream().sorted(Comparator.comparing(WorkCompletQtySubmit::getFCreateDate)).collect(Collectors.toList()).get(0);
            return workCompletQtySubmit;
        }
        return new WorkCompletQtySubmit();
    }

    @Override
    public void getWorkListDownload(String workShop, String fArcName, String startDate, String endDate, String workOrder, HttpServletResponse response) {
        List<WorkListMapDto> workListDtoList1 = findWorkListMapDto(workShop, fArcName, startDate, endDate, workOrder);
        workListDtoList1 = workListDtoList1.stream().sorted(Comparator.comparing(WorkListMapDto::getFSubDate, Comparator.reverseOrder()).thenComparing(a -> a.getFWorkOrder())).collect(Collectors.toList());
        List<WorkSubmitTimeList> workSubmitTimeLists = queryAllTimeList();
        List<Map<String, Object>> list = new ArrayList<>();
        for (WorkListMapDto workListDto : workListDtoList1) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("日期", workListDto.getFSubDate());
            map.put("工单", workListDto.getFWorkOrder());
            map.put("物料名称", workListDto.getFInvName());
            map.put("物料规格", workListDto.getFInvStd());
            map.put("工序", workListDto.getFArcName());
            for (WorkSubmitTimeList workSubmitTimeList : workSubmitTimeLists) {
                map.put(workSubmitTimeList.getFTimePeriods(), workListDto.getMap().get(workSubmitTimeList.getFTimePeriods()));
            }
            map.put("当日累计", workListDto.getTotal());
            list.add(map);
        }
        try {
            FileUtil.downloadExcel(list, response);
        } catch (Exception e) {
        }
    }

    @Override
    public void deleteWorkSubmit(Integer id) {
        WorkCompletQtySubmit workCompletQtySubmit = workCompletQtySubmitRepository.findById(id).orElseGet(WorkCompletQtySubmit::new);
        if (workCompletQtySubmit != null && workCompletQtySubmit.getId() != null) {
            workCompletQtySubmit.setFIsDeleted(1);
            workCompletQtySubmitRepository.save(workCompletQtySubmit);
        }
    }

}
