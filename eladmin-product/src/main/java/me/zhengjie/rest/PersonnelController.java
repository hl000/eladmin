package me.zhengjie.rest;

import com.taobao.api.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.DingUser;
import me.zhengjie.service.PersonnelService;
import me.zhengjie.service.dto.DingUserDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author HL
 * @create 2022/3/15 14:46
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "钉钉人员信息")
@RequestMapping("/api/personnel")
public class PersonnelController {

    private final PersonnelService personnelService;

    @GetMapping(value = "/getListSubID")
    public String getListSubID() throws ApiException {
        personnelService.getListSubId();
        return "success";
    }

    @GetMapping(value = "/getUserList")
    @Log("获取用户列表")
    @ApiOperation("获取用户列表")
    public List<DingUserDto> getUserList() {
        return personnelService.getUserList();
    }
}
