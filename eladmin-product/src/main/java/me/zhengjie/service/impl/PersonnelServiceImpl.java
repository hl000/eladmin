package me.zhengjie.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiUserListsimpleRequest;
import com.dingtalk.api.request.OapiV2DepartmentGetRequest;
import com.dingtalk.api.request.OapiV2DepartmentListsubidRequest;
import com.dingtalk.api.response.OapiUserListsimpleResponse;
import com.dingtalk.api.response.OapiV2DepartmentGetResponse;
import com.dingtalk.api.response.OapiV2DepartmentListsubidResponse;
import com.taobao.api.ApiException;
import lombok.RequiredArgsConstructor;
import me.zhengjie.Utils.HttpClientUtil;
import me.zhengjie.domain.DingDepartment;
import me.zhengjie.domain.DingUser;
import me.zhengjie.mapper.PersonnelMapper;
import me.zhengjie.service.PersonnelService;
import me.zhengjie.service.dto.DingUserDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HL
 * @create 2022/3/15 20:35
 */
@Service
@RequiredArgsConstructor
public class PersonnelServiceImpl implements PersonnelService {

    @Resource
    private final PersonnelMapper personnelMapper;

    public static final long cacheTime = 1000 * 60 * 55 * 2;

    public static long tokenTime = 0, ticketTime = 0;

    public static String token, ticket;

    public String getCorpToken() {
        Long now = System.currentTimeMillis();
        if (now - tokenTime > cacheTime) { //过期
            String url = "https://oapi.dingtalk.com/gettoken?appkey=dinghwdcpna5bzpgfbjp&appsecret=ZgDIImJxhQdKiRqoi_Bkv0RsT7CByUTw2gvS1VL_MKFYaTNSVI5gAwiaxP5IdLMB";
            String result = HttpClientUtil.doGet(url);
            JSONObject jsonObject = JSONObject.parseObject(result);
            token = jsonObject.getString("access_token");
            tokenTime = now;
        }
        return token;
    }


    @Override
    public void getListSubId() throws ApiException {
        List<Long> deptIds = new ArrayList<>();
        deptIds.add(1L);
        //获取所有部门id列表
        recursive(deptIds, 1L);
        List<DingDepartment> dingDepartments = new ArrayList<>();
        List<DingUser> dingUserList = new ArrayList<>();
        for (int i = 0; i < deptIds.size(); i++) {
            DingDepartment dingDepartment = new DingDepartment();
            //根据部门id获取部门详情
            String deptInfo = getDepartmentInfo(deptIds.get(i));
            if (deptInfo != null) {
                HashMap map = JSONObject.parseObject(JSON.parseObject(deptInfo, HashMap.class).get("result").toString(), HashMap.class);
                dingDepartment.setDeptId(deptIds.get(i));
                dingDepartment.setDeptName(map.get("name").toString());
                dingDepartments.add(dingDepartment);
            }

            //根据部门id获取部门下员工基础信息
            String userList = getList(deptIds.get(i));
            Long id = deptIds.get(i);
            if (userList != null && dingDepartment != null) {
                HashMap map = JSONObject.parseObject(JSON.parseObject(userList, HashMap.class).get("result").toString(), HashMap.class);
                List<DingUser> list = changeList(map.get("list"), DingUser.class);
                list.forEach(a -> {
                    a.setDeptId(id);
                    a.setUserStatus(true);
                });
                dingUserList.addAll(list);
            }
        }

        saveDepartmentAndUser(dingDepartments, dingUserList);
    }

    @Override
    public List<DingUserDto> getUserList() {
        return personnelMapper.getUserList();
    }

    @Override
    @Transactional
    public void saveDepartmentAndUser(List<DingDepartment> dingDepartments, List<DingUser> dingUserList) {
        dingUserList = dingUserList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(
                () -> new TreeSet<>(Comparator.comparing(DingUser::getUserid))), ArrayList::new));

        List<DingDepartment> dingDepartmentList = personnelMapper.findAllDepartment();
        List<DingUser> dingUsers = personnelMapper.findAllUser();
        for (DingDepartment dingDepartment : dingDepartments) {
            Boolean flag = true;
            for (DingDepartment dingDepartment1 : dingDepartmentList) {
                if (dingDepartment1.equals(dingDepartment)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                personnelMapper.insertDepartment(dingDepartment);
            }
        }

        for (DingUser dingUser : dingUserList) {
            Boolean flag = true;
            for (DingUser dingUser1 : dingUsers) {
                if (dingUser1.equals(dingUser)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                personnelMapper.insertUser(dingUser);
            }

        }
    }

    private void recursive(List<Long> deptIds, Long id) throws ApiException {
        String accessToken = getCorpToken();
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/listsubid");
        OapiV2DepartmentListsubidRequest req = new OapiV2DepartmentListsubidRequest();
        req.setDeptId(id);
        OapiV2DepartmentListsubidResponse rsp = client.execute(req, accessToken);

        HashMap map = JSON.parseObject(rsp.getBody(), HashMap.class);
        List<Long> ids = changeList(JSON.parseObject(map.get("result").toString(), HashMap.class).get("dept_id_list"), Long.class);

        if (ids != null && ids.size() > 0) {
            deptIds.addAll(ids);
            ids.forEach(a -> {
                try {
                    recursive(deptIds, a);
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            });
        }

    }

    private <T> List<T> changeList(Object object, Class<T> clazz) {
        try {
            List<T> result = new ArrayList<>();
            if (object instanceof List<?>) {
                for (Object o : (List<?>) object) {
                    String string = JSONObject.toJSONString(o);
                    T t = JSONObject.parseObject(string, clazz);
                    result.add(t);
                }
                return result;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getDepartmentInfo(Long deptId) throws ApiException {
        String accessToken = getCorpToken();
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/get");
        OapiV2DepartmentGetRequest req = new OapiV2DepartmentGetRequest();
        req.setDeptId(deptId);
        req.setLanguage("zh_CN");
        OapiV2DepartmentGetResponse rsp = client.execute(req, accessToken);
        return rsp.getBody();
    }

    private String getList(Long deptId) throws ApiException {
        String accessToken = getCorpToken();
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/user/listsimple");
        OapiUserListsimpleRequest req = new OapiUserListsimpleRequest();
        req.setDeptId(deptId);
        req.setCursor(0L);
        req.setSize(100L);
        req.setOrderField("modify_desc");
        req.setContainAccessLimit(false);
        req.setLanguage("zh_CN");
        OapiUserListsimpleResponse rsp = client.execute(req, accessToken);
        return rsp.getBody();
    }
}
