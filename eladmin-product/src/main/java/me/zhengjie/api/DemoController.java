package me.zhengjie.api;

import me.zhengjie.mapper.MybatisMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DemoController {

    @Resource
    private MybatisMapper mybatisMapper;

    @GetMapping("mybatis")
    public Object mybatis(){
        return mybatisMapper.getList();
    }
}
