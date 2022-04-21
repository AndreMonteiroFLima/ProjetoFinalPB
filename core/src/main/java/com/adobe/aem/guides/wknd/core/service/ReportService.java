package com.adobe.aem.guides.wknd.core.service;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import java.io.IOException;

public interface ReportService {

    public void getReport(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;
}
