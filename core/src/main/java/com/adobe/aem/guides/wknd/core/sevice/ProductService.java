package com.adobe.aem.guides.wknd.core.sevice;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface ProductService {

    public void getProduct(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;

    public void postProduct(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;

    public void deleteProduct(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;
}
