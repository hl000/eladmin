package me.zhengjie.service;

import me.zhengjie.domain.WorkCompletQtySubmit;
import me.zhengjie.domain.WorkSubmitTimeList;
import me.zhengjie.domain.WorkWorkingProcedure;
import me.zhengjie.service.dto.WorkCompletQtySubmitQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author HL
 * @create 2021/12/29 15:16
 */
public interface WorkSubmitService {
    List<WorkSubmitTimeList> queryAllTimeList();

    List<WorkWorkingProcedure> queryAllProcedure();

    Object getWorkList(String workShop, String fArcName, String startDate, String endDate, String workOrder, Pageable pageable);

    Object createWorkSubmit(WorkCompletQtySubmit resources);

    Object getAllWorkSubmit(WorkCompletQtySubmitQueryCriteria criteria, Pageable pageable);

    Object updateWorkSubmit(WorkCompletQtySubmit resources);

    Object getInventoryByWorkOrder(String workOrder);

    Object getWorkSubmitByArcId(Integer arcId);

    void getWorkListDownload(String workShop, String fArcName, String startDate, String endDate, String workOrder, HttpServletResponse response);

    void deleteWorkSubmit(Integer id);
}
