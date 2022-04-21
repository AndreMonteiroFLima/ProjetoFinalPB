package com.adobe.aem.guides.wknd.core.service.impl;

import com.adobe.aem.guides.wknd.core.dao.ClientDao;
import com.adobe.aem.guides.wknd.core.dao.ReportDao;
import com.adobe.aem.guides.wknd.core.exception.ClientDoesNotExistException;
import com.adobe.aem.guides.wknd.core.models.ClientModel;
import com.adobe.aem.guides.wknd.core.models.ReportModel;
import com.adobe.aem.guides.wknd.core.servlets.ReportServlet;
import com.adobe.aem.guides.wknd.core.service.ReportService;
import com.adobe.aem.guides.wknd.core.utils.ResponseSetter;
import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component(service = ReportService.class, immediate = true)
public class ReportServiceImp implements ReportService {

    private Logger log = LoggerFactory.getLogger(ReportServlet.class);

    @Reference
    private ReportDao reportDao;

    private final String urlReturn = "http://localhost:4502/bin/keepalive/product/report";

    /*@Override
    public void getReport(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        if(request.getParameter("userId")!=null) {
            try{
            ReportModel reports = reportDao.getReport(Integer.parseInt(request.getParameter("userId")));
            ResponseSetter.setOkResponse(new Gson().toJson(reports), HttpServletResponse.SC_OK, response);
            }catch (ClientDoesNotExistException e){
                ResponseSetter.setResponse(e.getMessage(), HttpServletResponse.SC_BAD_REQUEST, response, urlReturn);
            }
        }else {
                ResponseSetter.setResponse("Provide userId", HttpServletResponse.SC_BAD_REQUEST, response,urlReturn);
        }
    }*/

    @Override
    public void getReport(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        if (request.getParameter("userId") != null) {
            try {
                ReportModel reports = reportDao.getReport(Integer.parseInt(request.getParameter("userId")));
                ResponseSetter.setHtmlResponse(response,reports,HttpServletResponse.SC_OK);
            } catch (ClientDoesNotExistException e) {
                ResponseSetter.setResponse(e.getMessage(), HttpServletResponse.SC_BAD_REQUEST, response, urlReturn);
            }
        } else {
            ResponseSetter.setResponse("Provide userId", HttpServletResponse.SC_BAD_REQUEST, response, urlReturn);
        }
    }

}
