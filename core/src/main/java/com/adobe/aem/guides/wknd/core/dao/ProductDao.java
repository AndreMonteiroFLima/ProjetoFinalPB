package com.adobe.aem.guides.wknd.core.dao;

import com.adobe.aem.guides.wknd.core.exception.ProductDoesNotExistException;
import com.adobe.aem.guides.wknd.core.models.ProductModel;
import org.apache.sling.api.SlingHttpServletRequest;

import java.util.List;

public interface ProductDao {

    public void save(ProductModel product);

    public List<ProductModel> getProducts();

    public ProductModel getProduct(int productId);

    public void delete(int productId);

    public void update(ProductModel product);

    public Boolean verifyProducts(List<Integer> invoiceItens) throws ProductDoesNotExistException;

    List<ProductModel> getProductsWithParams(SlingHttpServletRequest request);
}
