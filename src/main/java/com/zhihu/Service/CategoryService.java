package com.zhihu.Service;

import com.zhihu.DAO.CategoryDAO;
import com.zhihu.Model.Category;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryDAO categoryDAO;

    public List<Category> getCategories(int limit, int offset){
        return categoryDAO.selectByLimit(offset, limit);
    }

    public Category getCategory(int id){
        return categoryDAO.selectById(id);
    }

    public int addCategory(String name){
        if(StringUtils.isBlank(name)){
            return 1;
        }
        if(categoryDAO.selectByName(name) != null){
            return categoryDAO.selectByName(name).getId();
        }
        Category category = new Category();
        category.setName(name);
        category.setQuestionCount(0);
        categoryDAO.addCategory(category);
        return category.getId();
    }
}
