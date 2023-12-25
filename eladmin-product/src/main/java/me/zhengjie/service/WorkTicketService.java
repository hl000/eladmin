package me.zhengjie.service;

import me.zhengjie.domain.WorkTicket;
import me.zhengjie.domain.WorkTicketManager;
import me.zhengjie.service.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author HL
 * @create 2023/12/13 16:18
 */
public interface WorkTicketService {
    Object createWorkReport(WorkTicket resources);

    WorkTicket updateWorkReport(WorkTicket resources);

    List<UserMatchDto> queryUserMatch();

    List<UserMatchDto> queryUserMatch(UserMatchQueryCriteria criteria);

    List<WorkTicket> queryAllWorkTicket(WorkTicketDingQueryCriteria criteria);

    Map<String, Object> queryAll(WorkTicketDingQueryCriteria criteria, Pageable pageable);

    List<WorkTicket> queryAll(WorkTicketDingQueryCriteria criteria);

    List<WorkTicket> queryAll(WorkTicketQueryCriteria criteria);

    List<WorkTicketManager> queryAllWorkTicketManager(WorkTicketManagerQueryCriteria criteria);

    List<WorkTicketDto> queryWorkReport(WorkTicketDingQueryCriteria workTicketQueryCriteria);

    Map<String, Object> findWorkTicket(WorkTicketDingQueryCriteria workTicketQueryCriteria, Pageable pageable);

}
