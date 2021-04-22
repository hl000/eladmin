package me.zhengjie.service.impl;

import cn.hutool.json.JSONObject;
import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.Category;
import me.zhengjie.domain.DailyPlan;
import me.zhengjie.mapper.CategoryMapper;
import me.zhengjie.repository.CategoryRepository;
import me.zhengjie.service.CategoryService;
import me.zhengjie.service.dto.CategoryDto;
import me.zhengjie.service.dto.CategoryQueryCriteria;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HL
 * @create 2021/4/13 11:48
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    @Resource(name="categoryMapperImpl")
    private final CategoryMapper categoryMapper;

//    @Resource
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> queryAll(CategoryQueryCriteria criteria) {
        List<Category> categories =categoryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));
//        UserDetails userDetails = SecurityUtils.getCurrentUser();
//        JSONObject deptObject = (JSONObject) new JSONObject(new JSONObject(userDetails).get("user")).get("dept");
//        String deptId = deptObject.get("id", String.class);
//        List<Category> categoryList =categoryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));
//        List<Category> categories = categoryList.stream().filter(p -> {
//            String deptIds = p.getDeptIds();
//            String[] deptIdList = deptIds.split(",");
//            for(int i=0;i<deptIdList.length;i++){
//                if(deptIdList[i].equals(deptId))
//                    return true;
//            }
//            return false;
//        }).collect(Collectors.toList());
        return categoryMapper.toDto(categories);


    }

    @Override
    public Category create(Category resources) {
        return categoryRepository.save(resources);
    }


}
