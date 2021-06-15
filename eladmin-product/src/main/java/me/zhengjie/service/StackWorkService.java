package me.zhengjie.service;

import me.zhengjie.service.dto.StackWorkQueryCriteria;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author HL
 * @create 2021/6/7 10:01
 */
public interface StackWorkService {
    List<Map<String, Object>> getStackWork(StackWorkQueryCriteria criteria);

    void stackWorkDownload(StackWorkQueryCriteria criteria, HttpServletResponse response) throws IOException;
}
