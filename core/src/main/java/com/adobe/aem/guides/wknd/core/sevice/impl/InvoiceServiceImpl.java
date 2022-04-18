package com.adobe.aem.guides.wknd.core.sevice.impl;

import com.adobe.aem.guides.wknd.core.dao.InvoiceDao;
import com.adobe.aem.guides.wknd.core.dao.ProductDao;
import com.adobe.aem.guides.wknd.core.models.ErroMessage;
import com.adobe.aem.guides.wknd.core.models.InvoiceModel;
import com.adobe.aem.guides.wknd.core.sevice.InvoiceService;
import com.adobe.aem.guides.wknd.core.utils.ResponseSetter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component(service = InvoiceService.class, immediate = true)
public class InvoiceServiceImpl implements InvoiceService {

    private final String urlReturn = "www.localhost:4502/bin/keepalive/invoiceService";

    private String json;

    ErroMessage errorMessage = new ErroMessage();

    @Reference
    private InvoiceDao invoiceDao;
    @Reference
    private ProductDao productDao;

    @Override
    public void getInvoice(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").create();

        if(request.getParameter("invoiceId")==null) {
            List<InvoiceModel> invoices = invoiceDao.getInvoices();
            ResponseSetter.setOkResponse(gson.toJson(invoices), HttpServletResponse.SC_OK,response);
        }else {
            InvoiceModel invoiceModel = invoiceDao.getInvoice(Integer.parseInt(request.getParameter("invoiceId")));
            if (invoiceModel == null) {
                ResponseSetter.setResponse("Cannot find invoiceId="+request.getParameter("invoiceId"), HttpServletResponse.SC_NOT_FOUND,response,urlReturn);
            } else {
                ResponseSetter.setOkResponse(gson.toJson(invoiceModel), HttpServletResponse.SC_OK,response);
            }
        }
    }

    @Override
    public void postInvoice(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String userPostString = IOUtils.toString(request.getReader());
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").create();
        InvoiceModel objInvoiceConverter = new InvoiceModel();
        try {
            objInvoiceConverter = gson.fromJson(userPostString, InvoiceModel.class);

            if ((objInvoiceConverter.getInvoiceDate() == null) ||
                    ((objInvoiceConverter.getInvoiceItens() == null || objInvoiceConverter.getInvoiceItens().isEmpty())) ||
                    ((objInvoiceConverter.getInvoiceValue() == null || objInvoiceConverter.getInvoiceValue().equals(0))) ||
                    ((objInvoiceConverter.getClientId() < 0))) {
                ResponseSetter.setResponse(gson.toJson("Cannot add Invoice. Provide all the required fields"), HttpServletResponse.SC_BAD_REQUEST,response,urlReturn);

            } else {
                if(productDao.verifyProducts(objInvoiceConverter.getInvoiceItens())) {
                    invoiceDao.save(objInvoiceConverter);
                    ResponseSetter.setOkResponse("Invoice added successfully.\n"+gson.toJson(objInvoiceConverter), HttpServletResponse.SC_CREATED,response);
                }
            }
        }catch (Exception e) {
            ResponseSetter.setResponse(gson.toJson("Cannot add Invoice. Error while trying to map the json. "+e.getMessage()), HttpServletResponse.SC_BAD_REQUEST,response,urlReturn);
        }
    }

    @Override
    public void deleteInvoice(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        if(request.getParameter("invoiceId")==null) {
            ResponseSetter.setResponse("Cannot delete Invoice. Provide invoiceId", HttpServletResponse.SC_BAD_REQUEST,response,urlReturn);
        }else {
            try{
                invoiceDao.delete(Integer.parseInt(request.getParameter("invoiceId")));
                ResponseSetter.setOkResponse(new Gson().toJson("Invoice deleted successfully"), HttpServletResponse.SC_OK,response);
            }catch (Exception e) {
                ResponseSetter.setResponse("Cannot delete Invoice. Error while trying to delete the invoice. "+e.getMessage(), HttpServletResponse.SC_BAD_REQUEST,response,urlReturn);
            }
        }
    }
}
