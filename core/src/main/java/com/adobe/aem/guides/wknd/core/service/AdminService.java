package com.adobe.aem.guides.wknd.core.service;


import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface AdminService {
    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;

    public void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;

    public void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;

    public void doPut(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;

}
