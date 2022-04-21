package com.adobe.aem.guides.wknd.core.service.impl;

import com.adobe.aem.guides.wknd.core.dao.InvoiceDao;
import com.adobe.aem.guides.wknd.core.dao.ProductDao;
import com.adobe.aem.guides.wknd.core.exception.ProductDoesNotExistException;
import com.adobe.aem.guides.wknd.core.models.ClientModel;
import com.adobe.aem.guides.wknd.core.models.ErroMessage;
import com.adobe.aem.guides.wknd.core.models.InvoiceModel;
import com.adobe.aem.guides.wknd.core.service.InvoiceService;
import com.adobe.aem.guides.wknd.core.utils.ResponseSetter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
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

            postVerify(response, gson, objInvoiceConverter);
        }catch (Exception e) {
            try{
                Type listType = new TypeToken<ArrayList<InvoiceModel>>(){}.getType();
                List<InvoiceModel> listInvoiceConverter = new ArrayList<>();
                listInvoiceConverter = gson.fromJson(userPostString, listType);

                listInvoiceConverter.forEach(invoiceModel -> {
                    try {
                        postVerify(response, gson, invoiceModel);
                    } catch (IOException | ProductDoesNotExistException ex) {
                        throw new RuntimeException("Error in postInvoice", ex);
                    }
                });
            }  catch (Exception e1) {
                ResponseSetter.setResponse(gson.toJson("Cannot add Invoice. Error while trying to map the json. " + e.getMessage()), HttpServletResponse.SC_BAD_REQUEST, response, urlReturn);
            }
        }
    }

    private void postVerify(SlingHttpServletResponse response, Gson gson, InvoiceModel objInvoiceConverter) throws IOException, ProductDoesNotExistException {
        if ((objInvoiceConverter.getInvoiceDate() == null) ||
                ((objInvoiceConverter.getInvoiceItens() == null || objInvoiceConverter.getInvoiceItens().isEmpty())) ||
                ((objInvoiceConverter.getInvoiceValue() == null || objInvoiceConverter.getInvoiceValue().equals(0))) ||
                ((objInvoiceConverter.getClientId() < 0))) {
            ResponseSetter.setResponse(gson.toJson("Cannot add Invoice. Provide all the required fields"), HttpServletResponse.SC_BAD_REQUEST, response,urlReturn);

        } else {
            if(productDao.verifyProducts(objInvoiceConverter.getInvoiceItens())) {
                invoiceDao.save(objInvoiceConverter);
                ResponseSetter.setOkResponse("Invoice added successfully.\n"+ gson.toJson(objInvoiceConverter), HttpServletResponse.SC_CREATED, response);
            }
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

    @Override
    public void putInvoice(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");


        String userPostString = IOUtils.toString(request.getReader());
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").create();

        InvoiceModel objInvoiceConverter = new InvoiceModel();
        try {
            objInvoiceConverter = gson.fromJson(userPostString, InvoiceModel.class);
            putVerify(response, gson, objInvoiceConverter);
        }catch (Exception e) {
            try{
                Type listType = new TypeToken<ArrayList<InvoiceModel>>(){}.getType();
                List<InvoiceModel> listInvoiceConverter = new ArrayList<>();
                listInvoiceConverter = gson.fromJson(userPostString, listType);

                listInvoiceConverter.forEach(invoiceModel -> {
                    try {
                        putVerify(response, gson, invoiceModel);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

            }  catch (Exception e1) {
                ResponseSetter.setResponse("Cannot save invoice. Error while trying to map the json.", HttpServletResponse.SC_BAD_REQUEST, response, urlReturn);
            }
        }
    }

    private void putVerify(SlingHttpServletResponse response, Gson gson, InvoiceModel objInvoiceConverter) throws IOException {
        if(invoiceDao.getInvoice(objInvoiceConverter.getInvoiceId())!=null) {
            if (((objInvoiceConverter.getInvoiceId() <= 0) || (objInvoiceConverter.getInvoiceDate() == null) ||
                    ((objInvoiceConverter.getInvoiceItens() == null || objInvoiceConverter.getInvoiceItens().isEmpty())) ||
                    ((objInvoiceConverter.getInvoiceValue() == null || objInvoiceConverter.getInvoiceValue().equals(0))) ||
                    ((objInvoiceConverter.getClientId() < 0)))) {
                ResponseSetter.setResponse("Name is required", HttpServletResponse.SC_BAD_REQUEST, response, urlReturn);
            } else {
                if (invoiceDao.getInvoice(objInvoiceConverter.getInvoiceId()) != null) {
                    invoiceDao.update(objInvoiceConverter);
                    ResponseSetter.setOkResponse(gson.toJson(objInvoiceConverter), HttpServletResponse.SC_CREATED, response);
                } else {
                    ResponseSetter.setResponse("Cannot find invoiceId=" + objInvoiceConverter.getInvoiceId(), HttpServletResponse.SC_NOT_FOUND, response, urlReturn);
                }
            }
        }else {
            ResponseSetter.setResponse("Cannot find invoiceId=" + objInvoiceConverter.getInvoiceId(), HttpServletResponse.SC_NOT_FOUND, response, urlReturn);
        }
    }
}
