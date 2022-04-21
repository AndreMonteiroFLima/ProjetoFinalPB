package com.adobe.aem.guides.wknd.core.service;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import java.io.IOException;

public interface CategoryService {

    void getCategory(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;

    void postCategory(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;

    void deleteCategory(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;

    void putCategory(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;
}
