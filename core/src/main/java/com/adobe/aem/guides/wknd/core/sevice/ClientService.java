package com.adobe.aem.guides.wknd.core.sevice;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import java.io.IOException;

public interface ClientService {
    
    public void getClient(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;
    
    public void postClient(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;
    
    public void deleteClient(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;
}
