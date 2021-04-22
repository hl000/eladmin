package me.zhengjie.service;

import me.zhengjie.domain.TechniqueInfo;
import me.zhengjie.service.dto.TechniqueInfoDto;
import me.zhengjie.service.dto.TechniqueInfoQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author HL
 * @create 2021/4/13 11:48
 */
public interface TechniqueInfoService {

    /**
     * 查询数据分页
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String,Object>
     */
    Map<String,Object> queryAll(TechniqueInfoQueryCriteria criteria, Pageable pageable);

    List<TechniqueInfoDto> queryAll(TechniqueInfoQueryCriteria criteria);


    /**
     * 创建
     * @param resources /
     * @return TechniqueInfoDto
     */
    TechniqueInfoDto create(TechniqueInfo resources);

    /**
     * 编辑
     * @param resources /
     */
    void update(TechniqueInfo resources);

    void download(HttpServletResponse response, TechniqueInfoQueryCriteria criteria);

    List<TechniqueInfo> getTechniqueInfoByUser(String deptId);
}
