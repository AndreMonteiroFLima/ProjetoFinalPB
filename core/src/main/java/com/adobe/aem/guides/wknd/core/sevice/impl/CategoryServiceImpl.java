package com.adobe.aem.guides.wknd.core.sevice.impl;

import com.adobe.aem.guides.wknd.core.dao.CategoryDao;
import com.adobe.aem.guides.wknd.core.models.CategoryModel;
import com.adobe.aem.guides.wknd.core.models.ErroMessage;
import com.adobe.aem.guides.wknd.core.sevice.CategoryService;
import com.adobe.aem.guides.wknd.core.utils.ResponseSetter;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component(service = CategoryService.class, immediate = true)
public class CategoryServiceImpl implements CategoryService {

    String json;

    private final String urlReturn = "www.localhost:4502/bin/keepalive/categoryService";
    
    private ErroMessage errorMessage;

    @Reference
    private CategoryDao categoryDao;
    
    @Override
    public void getCategory(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        if(request.getParameter("categoryId")==null) {
            List<CategoryModel> categorys = categoryDao.getCategorys();
            ResponseSetter.setOkResponse(new Gson().toJson(categorys), HttpServletResponse.SC_OK, response);
        }else {
            CategoryModel categoryModel = categoryDao.getCategory(Integer.parseInt(request.getParameter("categoryId")));
            if (categoryModel == null) {
                ResponseSetter.setResponse("Cannot find categoryId="+request.getParameter("categoryId"), HttpServletResponse.SC_NOT_FOUND, response,urlReturn);
            } else {
                ResponseSetter.setOkResponse(new Gson().toJson(categoryModel),HttpServletResponse.SC_OK,response);
            }
        }
    }

    @Override
    public void postCategory(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String userPostString = IOUtils.toString(request.getReader());
        Gson gson = new Gson();
        CategoryModel objCategoryConverter = new CategoryModel();
        try {
            objCategoryConverter = gson.fromJson(userPostString, CategoryModel.class);

            if (((objCategoryConverter.getName() == null || objCategoryConverter.getName().isEmpty()))){
                ResponseSetter.setResponse("Name is required", HttpServletResponse.SC_BAD_REQUEST, response,urlReturn);
            } else {
                
                categoryDao.save(objCategoryConverter);
                ResponseSetter.setOkResponse(new Gson().toJson(objCategoryConverter),HttpServletResponse.SC_CREATED,response);
            }
        }catch (Exception e) {
            ResponseSetter.setResponse("Cannot save category. Error while trying to map the json.", HttpServletResponse.SC_BAD_REQUEST, response,urlReturn);
        }
    }

    @Override
    public void deleteCategory(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        if(request.getParameter("categoryId")==null) {
            ResponseSetter.setResponse("Cannot delete Category. Provide categoryId", HttpServletResponse.SC_BAD_REQUEST, response,urlReturn);
        }else {
            CategoryModel categoryModel = categoryDao.getCategory(Integer.parseInt(request.getParameter("categoryId")));
            if (categoryModel == null) {
                ResponseSetter.setResponse("Cannot find categoryId="+request.getParameter("categoryId"), HttpServletResponse.SC_NOT_FOUND, response,urlReturn);
            }else {
                try {
                    categoryDao.delete(Integer.parseInt(request.getParameter("categoryId")));
                    ResponseSetter.setOkResponse(new Gson().toJson("Category deleted"),HttpServletResponse.SC_OK,response);
                } catch (Exception e) {
                    ResponseSetter.setResponse("Cannot delete category."+e.getMessage(), HttpServletResponse.SC_BAD_REQUEST, response,urlReturn);
                }
            }
        }
    }
}
