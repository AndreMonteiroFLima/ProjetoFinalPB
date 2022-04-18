package com.adobe.aem.guides.wknd.core.sevice.impl;

import com.adobe.aem.guides.wknd.core.dao.CategoryDao;
import com.adobe.aem.guides.wknd.core.dao.ProductDao;
import com.adobe.aem.guides.wknd.core.exception.CategoryDoesNotExistException;
import com.adobe.aem.guides.wknd.core.models.ErroMessage;
import com.adobe.aem.guides.wknd.core.models.ProductModel;
import com.adobe.aem.guides.wknd.core.sevice.ProductService;
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
import java.io.UnsupportedEncodingException;
import java.util.List;

@Component(service = ProductService.class, immediate = true)
public class ProductServiceImpl implements ProductService {

    private final String urlReturn = "www.localhost:4502/bin/keepalive/productService";
    
    String json;

    private ErroMessage errorMessage;

    @Reference
    private ProductDao productDao;
    @Reference
    private CategoryDao categoryDao;

    
    @Override
    public void getProduct(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        if(request.getParameter("productId")==null) {
            List<ProductModel> products = productDao.getProducts();
            json = new Gson().toJson(products);
            ResponseSetter.setOkResponse(new Gson().toJson(products),HttpServletResponse.SC_OK,response);
        }else {
            ProductModel productModel = productDao.getProduct(Integer.parseInt(request.getParameter("productId")));
            if (productModel == null) {
                ResponseSetter.setResponse("Cannot find productId="+request.getParameter("productId") ,HttpServletResponse.SC_NOT_FOUND,response,urlReturn);
            } else {
                ResponseSetter.setOkResponse(new Gson().toJson(productModel),HttpServletResponse.SC_OK,response);
            }
        }
    }

    @Override
    public void postProduct(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String userPostString = IOUtils.toString(request.getReader());
        Gson gson = new Gson();
        ProductModel objProductConverter = new ProductModel();
        try {
            objProductConverter = gson.fromJson(userPostString, ProductModel.class);

            if (objProductConverter.getCategoryId() == null ||  ((objProductConverter.getName() == null || objProductConverter.getName().isEmpty())) ||
                    ((objProductConverter.getPrice() == null || objProductConverter.getPrice().equals(0))) ||
                    ((objProductConverter.getDescription() == null || objProductConverter.getDescription().isEmpty()))) {
                ResponseSetter.setResponse("Cannot add product. Provide all the required fields",HttpServletResponse.SC_BAD_REQUEST,response,urlReturn);
            } else {
                productDao.save(objProductConverter);
                ResponseSetter.setOkResponse(new Gson().toJson(objProductConverter),HttpServletResponse.SC_OK,response);
            }
        }catch (Exception e) {
            ResponseSetter.setResponse("Error while trying to map the json ",HttpServletResponse.SC_BAD_REQUEST,response,urlReturn);
        }
    }

    @Override
    public void deleteProduct(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        if(request.getParameter("productId")==null) {
            ResponseSetter.setResponse("Cannot delete product. Provide productId",HttpServletResponse.SC_BAD_REQUEST,response,urlReturn);
        }else {
            ProductModel productModel = productDao.getProduct(Integer.parseInt(request.getParameter("productId")));
            if (productModel == null) {
                ResponseSetter.setResponse("Cannot find productId="+request.getParameter("productId") ,HttpServletResponse.SC_NOT_FOUND,response,urlReturn);
            }else {
                try {
                    productDao.delete(Integer.parseInt(request.getParameter("productId")));
                    ResponseSetter.setOkResponse(new Gson().toJson("Product deleted successfully"),HttpServletResponse.SC_OK,response);
                } catch (Exception e) {
                    ResponseSetter.setResponse(e.getMessage() ,HttpServletResponse.SC_BAD_REQUEST,response,urlReturn);
                }
            }
        }
    }
}
