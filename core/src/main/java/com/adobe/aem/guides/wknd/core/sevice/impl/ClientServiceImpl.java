package com.adobe.aem.guides.wknd.core.sevice.impl;

import com.adobe.aem.guides.wknd.core.dao.ClientDao;
import com.adobe.aem.guides.wknd.core.models.ClientModel;
import com.adobe.aem.guides.wknd.core.models.ErroMessage;
import com.adobe.aem.guides.wknd.core.models.ProductModel;
import com.adobe.aem.guides.wknd.core.sevice.ClientService;
import com.adobe.aem.guides.wknd.core.utils.ResponseSetter;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component(service = ClientService.class, immediate = true)
public class ClientServiceImpl implements ClientService {

    private final String urlReturn = "www.localhost:4502/bin/keepalive/clientService";
    
    String json;

    private ErroMessage errorMessage;

    @Reference
    private ClientDao clientDao;
    
    @Override
    public void getClient(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        if(request.getParameter("clientId")==null) {
            List<ClientModel> clients = clientDao.getClients();
            ResponseSetter.setOkResponse(json = new Gson().toJson(clients), HttpServletResponse.SC_OK, response);
        }else {
            ClientModel clientModel = clientDao.getClient(Integer.parseInt(request.getParameter("clientId")));
            if (clientModel == null) {
                ResponseSetter.setResponse("Cannot find clientId="+request.getParameter("clientId"), HttpServletResponse.SC_NOT_FOUND, response,urlReturn);
            } else {
                ResponseSetter.setOkResponse(new Gson().toJson(clientModel), HttpServletResponse.SC_OK, response);
            }
        }

    }

    @Override
    public void postClient(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String userPostString = IOUtils.toString(request.getReader());
        Gson gson = new Gson();
        ClientModel objClientConverter = new ClientModel();
        try {
            objClientConverter = gson.fromJson(userPostString, ClientModel.class);

            if (((objClientConverter.getName() == null || objClientConverter.getName().isEmpty()))){
                ResponseSetter.setResponse("Name is required", HttpServletResponse.SC_BAD_REQUEST, response,urlReturn);
            } else {
                clientDao.save(objClientConverter);
                ResponseSetter.setOkResponse(new Gson().toJson(objClientConverter), HttpServletResponse.SC_CREATED, response);
            }
        }catch (Exception e) {
            ResponseSetter.setResponse("Error while trying to map the json. " + e.getMessage(), HttpServletResponse.SC_BAD_REQUEST, response,urlReturn);
        }
    }

    @Override
    public void deleteClient(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        if(request.getParameter("clientId")==null) {
            ResponseSetter.setResponse("Cannot delete Client. Provide clientId", HttpServletResponse.SC_BAD_REQUEST, response,urlReturn);
        }else {
            ClientModel clientModel = clientDao.getClient(Integer.parseInt(request.getParameter("clientId")));
            if (clientModel == null) {
                ResponseSetter.setResponse("Cannot find clientId="+request.getParameter("clientId"), HttpServletResponse.SC_NOT_FOUND, response,urlReturn);
            }else {
                try {
                    clientDao.delete(Integer.parseInt(request.getParameter("clientId")));
                    ResponseSetter.setOkResponse(new Gson().toJson("Client deleted"), HttpServletResponse.SC_OK, response);
                } catch (Exception e) {
                    ResponseSetter.setResponse("Error while trying to delete client. " + e.getMessage(), HttpServletResponse.SC_BAD_REQUEST, response,urlReturn);
                }
            }
        }
    }
}
