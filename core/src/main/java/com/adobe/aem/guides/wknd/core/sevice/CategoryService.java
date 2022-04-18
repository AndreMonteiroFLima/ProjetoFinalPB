package com.adobe.aem.guides.wknd.core.sevice;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import java.io.IOException;

public interface CategoryService {

    public void getCategory(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;

    public void postCategory(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;

    public void deleteCategory(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;
}
