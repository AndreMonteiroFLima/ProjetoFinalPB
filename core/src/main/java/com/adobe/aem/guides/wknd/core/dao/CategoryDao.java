package com.adobe.aem.guides.wknd.core.dao;

import com.adobe.aem.guides.wknd.core.models.CategoryModel;
import com.adobe.aem.guides.wknd.core.models.ClientModel;

import java.util.List;

public interface CategoryDao {

    public void save(CategoryModel category);

    public List<CategoryModel> getCategorys();

    public CategoryModel getCategory(int categoryId);

    public void delete(int categoryId);

    public void update(CategoryModel category);

    public boolean exists(int categoryId);
}
