package com.adobe.aem.guides.wknd.core.service;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import java.io.IOException;

public interface ProductService {

    public void getProduct(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;

    public void postProduct(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;

    public void deleteProduct(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;

    void putProduct(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;
}
