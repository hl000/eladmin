package me.zhengjie.rest;

import cn.hutool.json.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.WorkTicket;
import me.zhengjie.service.WorkTicketService;
import me.zhengjie.service.dto.UserMatchQueryCriteria;
import me.zhengjie.service.dto.WorkTicketDingQueryCriteria;
import me.zhengjie.service.dto.WorkTicketDto;
import me.zhengjie.service.dto.WorkTicketQueryCriteria;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author HL
 * @create 2023/12/13 15:44
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "生产报工管理")
@RequestMapping("/api/workTicket")
public class WorkTicketController {
    private final WorkTicketService workTicketService;

    @GetMapping("/queryAllWorkTicket")
    @Log("钉钉查询生产报工")
    @ApiOperation("钉钉查询生产报工")
    public Object queryAllWorkReport(WorkTicketDingQueryCriteria workTicketQueryCriteria) {
        return workTicketService.queryAllWorkTicket(workTicketQueryCriteria);
    }

    @GetMapping("/queryWorkTicket")
    @Log("后台查询工作计划")
    @ApiOperation("后台查询生产报工")
    public List<WorkTicketDto> queryWorkReport(WorkTicketDingQueryCriteria workTicketQueryCriteria) {
        return workTicketService.queryWorkReport(workTicketQueryCriteria);
    }

    @GetMapping("/findWorkTicket")
    @Log("后台查询生产报工分页")
    @ApiOperation("后台查询生产报工分页")
    public Map<String, Object> findWorkTicket(WorkTicketDingQueryCriteria workTicketQueryCriteria, Pageable pageable) {
        return workTicketService.findWorkTicket(workTicketQueryCriteria, pageable);
    }


    @GetMapping("/queryUserMatch")
    @Log("用户匹配下拉框")
    @ApiOperation("用户匹配下拉框")
    public Object queryUserMatch() {
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        String deptName = (String) new JSONObject(new JSONObject(new JSONObject(userDetails).get("user")).get("dept")).get("name");
        UserMatchQueryCriteria userMatchQueryCriteria = new UserMatchQueryCriteria();
        userMatchQueryCriteria.setDepartment(deptName);
        return workTicketService.queryUserMatch(userMatchQueryCriteria);
    }

    @GetMapping("/queryAllUserMatch")
    @Log("所有用户匹配下拉框")
    @ApiOperation("所有用户匹配下拉框")
    public Object queryAllUserMatch() {
        return workTicketService.queryUserMatch();
    }

    @PostMapping("/createWorkTicket")
    @Log("新增生产报工")
    @ApiOperation("新增生产报工")
    public Object createWorkReport(@Validated @RequestBody WorkTicket resources) {
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        String username = (String) new JSONObject(new JSONObject(userDetails).get("user")).get("username");
        resources.setCreatedBy(username);

        WorkTicketQueryCriteria workTicketQueryCriteria = new WorkTicketQueryCriteria();
        workTicketQueryCriteria.setDate(resources.getDate());
        List<WorkTicket> workTicketList = workTicketService.queryAll(workTicketQueryCriteria);
        String number = String.format("%03d", workTicketList.size() + 1);
        resources.setOrderNumber(resources.getDate().replaceAll("-", "") + number);
        return workTicketService.createWorkReport(resources);
    }

    @PostMapping("/updateWorkTicket")
    @Log("更新生产报工")
    @ApiOperation("更新生产报工")
    public WorkTicket updateWorkReport(@Validated @RequestBody WorkTicket resources) {
        UserDetails userDetails = SecurityUtils.getCurrentUser();
        String username = (String) new JSONObject(new JSONObject(userDetails).get("user")).get("username");
        resources.setUpdatedBy(username);
        return workTicketService.updateWorkReport(resources);
    }

}
