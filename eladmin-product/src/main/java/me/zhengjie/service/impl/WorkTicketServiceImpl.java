package me.zhengjie.service.impl;

import cn.hutool.json.JSONObject;
import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.ManufactureSummary;
import me.zhengjie.domain.UserMatch;
import me.zhengjie.domain.WorkTicket;
import me.zhengjie.domain.WorkTicketManager;
import me.zhengjie.repository.UserMatchRepository;
import me.zhengjie.repository.WorkTicketManagerRepository;
import me.zhengjie.repository.WorkTicketRepository;
import me.zhengjie.service.WorkTicketService;
import me.zhengjie.service.dto.*;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author HL
 * @create 2023/12/13 16:20
 */
@Service
@RequiredArgsConstructor
public class WorkTicketServiceImpl implements WorkTicketService {

    private final WorkTicketRepository workTicketRepository;

    private final UserMatchRepository userMatchRepository;

    private final WorkTicketManagerRepository workTicketManagerRepository;

    @Override
    public Object createWorkReport(WorkTicket resources) {
        return workTicketRepository.save(resources);
    }

    @Override
    public WorkTicket updateWorkReport(WorkTicket resources) {
        WorkTicket workTicket = workTicketRepository.findById(resources.getId()).orElseGet(WorkTicket::new);
        ValidationUtil.isNull(workTicket.getId(), "workTicket", "id", resources.getId());
        workTicket.copy(resources);
        return workTicketRepository.save(workTicket);
    }

    @Override
    public List<UserMatchDto> queryUserMatch() {
        List<UserMatch> userMatches = userMatchRepository.findAll();
        return getUserMatchDtos(userMatches);
    }

    @Override
    public List<UserMatchDto> queryUserMatch(UserMatchQueryCriteria criteria) {
        List<UserMatch> userMatches = userMatchRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        if (userMatches == null || userMatches.size() == 0) {
            return queryUserMatch();
        } else {
            return getUserMatchDtos(userMatches);
        }
    }

    private List<UserMatchDto> getUserMatchDtos(List<UserMatch> userMatches) {
        List<UserMatchDto> userMatchDtoList = new ArrayList<>();
        for (UserMatch userMatch : userMatches) {
            UserMatchDto userMatchDto = new UserMatchDto();
            userMatchDto.setId(userMatch.getId());
            userMatchDto.setDepartment(userMatch.getDepartment());
            userMatchDto.setLeader(Arrays.asList(userMatch.getLeader().split("/")));
            userMatchDto.setProcessList(Arrays.asList(userMatch.getProcess().split("、")));
            userMatchDtoList.add(userMatchDto);
        }
        return userMatchDtoList;
    }

    @Override
    public List<WorkTicket> queryAllWorkTicket(WorkTicketDingQueryCriteria criteria) {
        return workTicketRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
    }

    @Override
    public Map<String, Object> queryAll(WorkTicketDingQueryCriteria criteria, Pageable pageable) {
        Page<WorkTicket> page = workTicketRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page);
    }

    @Override
    public List<WorkTicket> queryAll(WorkTicketDingQueryCriteria criteria) {
        return workTicketRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
    }

    @Override
    public List<WorkTicket> queryAll(WorkTicketQueryCriteria criteria) {
        return workTicketRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
    }

    @Override
    public List<WorkTicketManager> queryAllWorkTicketManager(WorkTicketManagerQueryCriteria criteria) {
        return workTicketManagerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
    }

    @Override
    public List<WorkTicketDto> queryWorkReport(WorkTicketDingQueryCriteria workTicketQueryCriteria) {
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        String username = (String) new JSONObject(new JSONObject(userDetails).get("user")).get("username");
        WorkTicketManagerQueryCriteria criteria = new WorkTicketManagerQueryCriteria();
        criteria.setUsername(username);
        List<WorkTicketManager> list = queryAllWorkTicketManager(criteria);
        List<WorkTicket> workTicketList = new ArrayList<>();
        List<WorkTicketDto> workTicketDtos = new ArrayList<>();
        if (list == null || list.size() == 0) {
            String deptName = (String) new JSONObject(new JSONObject(new JSONObject(userDetails).get("user")).get("dept")).get("name");
            workTicketQueryCriteria.setDepartment(deptName);
            workTicketList = queryAll(workTicketQueryCriteria);
            if (workTicketList == null || workTicketList.size() == 0) {
                return workTicketDtos;
            } else {
                return getWorkTicketDtos(username, workTicketList, workTicketDtos);
            }

        } else {
            workTicketList = queryAll(workTicketQueryCriteria);
            return getWorkTicketDtos(username, workTicketList, workTicketDtos);
        }

    }

    private List<WorkTicketDto> getWorkTicketDtos(String username, List<WorkTicket> workTicketList, List<WorkTicketDto> workTicketDtos) {
        for (WorkTicket workTicket : workTicketList) {
            WorkTicketDto workTicketDto = new WorkTicketDto();
            workTicketDto.setWorkTicket(workTicket);
            if (workTicket.getCreatedBy().equals(username)) {
                workTicketDto.setFlag(true);
            } else {
                workTicketDto.setFlag(false);
            }
            workTicketDtos.add(workTicketDto);
        }
        return workTicketDtos;
    }

    @Override
    public Map<String, Object> findWorkTicket(WorkTicketDingQueryCriteria workTicketQueryCriteria, Pageable pageable) {
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        String username = (String) new JSONObject(new JSONObject(userDetails).get("user")).get("username");
        WorkTicketManagerQueryCriteria criteria = new WorkTicketManagerQueryCriteria();
        criteria.setUsername(username);
        List<WorkTicketManager> list = queryAllWorkTicketManager(criteria);
        List<WorkTicket> workTicketList = new ArrayList<>();
        List<WorkTicketDto> workTicketDtos = new ArrayList<>();
        if (list == null || list.size() == 0) {
            String deptName = (String) new JSONObject(new JSONObject(new JSONObject(userDetails).get("user")).get("dept")).get("name");
            workTicketQueryCriteria.setDepartment(deptName);
            Page<WorkTicket> page = workTicketRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, workTicketQueryCriteria, criteriaBuilder), pageable);
            workTicketList = page.getContent();
            if (workTicketList == null || workTicketList.size() == 0) {
                return null;
            } else {
                Page<WorkTicketDto> newPage = new PageImpl<>(getWorkTicketDtos(username, workTicketList, workTicketDtos), pageable, page.getTotalElements());
                return PageUtil.toPage(newPage);
            }

        } else {
            Page<WorkTicket> page = workTicketRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, workTicketQueryCriteria, criteriaBuilder), pageable);
            workTicketList = page.getContent();
            if (workTicketList == null || workTicketList.size() == 0) {
                return null;
            } else {
                Page<WorkTicketDto> newPage = new PageImpl<>(getWorkTicketDtos(username, workTicketList, workTicketDtos), pageable, page.getTotalElements());
                return PageUtil.toPage(newPage);
            }
        }
    }

}
