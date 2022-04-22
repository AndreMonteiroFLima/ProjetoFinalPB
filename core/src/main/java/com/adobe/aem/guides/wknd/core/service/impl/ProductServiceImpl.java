package com.adobe.aem.guides.wknd.core.service.impl;

import com.adobe.aem.guides.wknd.core.dao.CategoryDao;
import com.adobe.aem.guides.wknd.core.dao.ProductDao;
import com.adobe.aem.guides.wknd.core.models.ErroMessage;
import com.adobe.aem.guides.wknd.core.models.ProductModel;
import com.adobe.aem.guides.wknd.core.service.ProductService;
import com.adobe.aem.guides.wknd.core.utils.ResponseSetter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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

            List<ProductModel> products = productDao.getProductsWithParams(request);

            json = new Gson().toJson(products);
            ResponseSetter.setOkResponse(new Gson().toJson(products),HttpServletResponse.SC_OK,response);
        }else {
            ProductModel productModel = productDao.getProduct(Integer.parseInt(request.getParameter("productId")));
            if (productModel == null) {
                ResponseSetter.setResponse("Cannot find productId=" + request.getParameter("productId"), HttpServletResponse.SC_NOT_FOUND, response, urlReturn);
            } else {
                ResponseSetter.setOkResponse(new Gson().toJson(productModel), HttpServletResponse.SC_OK, response);
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

            postVerify(response, gson, objProductConverter);

        }catch (Exception e) {
            try {
                Type listType = new TypeToken<ArrayList<ProductModel>>() {}.getType();
                List<ProductModel> listProductConverter = new ArrayList<>();
                listProductConverter = gson.fromJson(userPostString, listType);

                listProductConverter.forEach(productModel -> {
                    try {
                        postVerify(response, gson, productModel);
                    } catch (IOException ex) {
                        throw new RuntimeException("Error in postProduct", ex);
                    }
                });
            } catch (Exception ex) {
                ResponseSetter.setResponse("Error while trying to map the json ", HttpServletResponse.SC_BAD_REQUEST, response, urlReturn);
            }
        }
    }

    private void postVerify(SlingHttpServletResponse response, Gson gson, ProductModel objProductConverter) throws IOException {
        if (objProductConverter.getCategoryId() == null ||  ((objProductConverter.getName() == null || objProductConverter.getName().isEmpty())) ||
                ((objProductConverter.getPrice() == null || objProductConverter.getPrice().equals(0))) ||
                ((objProductConverter.getDescription() == null || objProductConverter.getDescription().isEmpty()))) {
            ResponseSetter.setResponse("Cannot add product. Provide all the required fields",HttpServletResponse.SC_BAD_REQUEST, response,urlReturn);
        } else {
            for (Integer categoryId : objProductConverter.getCategoryId()) {
                if(!categoryDao.exists(categoryId)) {
                    throw new RuntimeException("Cannot add product. CategoryId=" + categoryId + " does not exist");
                }
            }
            productDao.save(objProductConverter);
            ResponseSetter.setOkResponse(gson.toJson(objProductConverter), HttpServletResponse.SC_OK, response);
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

    @Override
    public void putProduct(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");


        String userPostString = IOUtils.toString(request.getReader());
        Gson gson = new Gson();
        ProductModel objProductConverter = new ProductModel();
        try {
            objProductConverter = gson.fromJson(userPostString, ProductModel.class);
            putVerify(response, gson, objProductConverter);
        }catch (Exception e) {
            try{
                Type listType = new TypeToken<ArrayList<ProductModel>>(){}.getType();
                List<ProductModel> listProductConverter = new ArrayList<>();
                listProductConverter = gson.fromJson(userPostString, listType);

                listProductConverter.forEach(productModel -> {
                    try {
                        putVerify(response, gson, productModel);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

            }  catch (Exception e1) {
                ResponseSetter.setResponse("Cannot save product. Error while trying to map the json.", HttpServletResponse.SC_BAD_REQUEST, response, urlReturn);
            }
        }
    }

    private void putVerify(SlingHttpServletResponse response, Gson gson, ProductModel objProductConverter) throws IOException {
        if(productDao.getProduct(objProductConverter.getId())!=null) {
            if (((objProductConverter.getId() <= 0) || (objProductConverter.getCategoryId() == null
                    ||  ((objProductConverter.getName() == null || objProductConverter.getName().isEmpty())) ||
                    ((objProductConverter.getPrice() == null || objProductConverter.getPrice().equals(0)))))) {
                ResponseSetter.setResponse("Name is required", HttpServletResponse.SC_BAD_REQUEST, response, urlReturn);
            } else {
                if (productDao.getProduct(objProductConverter.getId()) != null) {
                    productDao.update(objProductConverter);
                    ResponseSetter.setOkResponse(gson.toJson(objProductConverter), HttpServletResponse.SC_CREATED, response);
                } else {
                    ResponseSetter.setResponse("Cannot find productId=" + objProductConverter.getId(), HttpServletResponse.SC_NOT_FOUND, response, urlReturn);
                }
            }
        }else {
            ResponseSetter.setResponse("Cannot find productId=" + objProductConverter.getId(), HttpServletResponse.SC_NOT_FOUND, response, urlReturn);
        }
    }
}
