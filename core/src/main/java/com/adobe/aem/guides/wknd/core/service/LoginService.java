package com.adobe.aem.guides.wknd.core.service;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import java.io.IOException;

public interface LoginService {
    void login(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;

    void logout(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;
}
