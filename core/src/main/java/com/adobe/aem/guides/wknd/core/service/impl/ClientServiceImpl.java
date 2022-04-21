package com.adobe.aem.guides.wknd.core.service.impl;

import com.adobe.aem.guides.wknd.core.dao.ClientDao;
import com.adobe.aem.guides.wknd.core.models.CategoryModel;
import com.adobe.aem.guides.wknd.core.models.ClientModel;
import com.adobe.aem.guides.wknd.core.models.ErroMessage;
import com.adobe.aem.guides.wknd.core.service.ClientService;
import com.adobe.aem.guides.wknd.core.utils.ResponseSetter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
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

            postVerify(response, gson, objClientConverter);
        }catch (Exception e) {
            try {
                Type listType = new TypeToken<ArrayList<ClientModel>>(){}.getType();
                List<ClientModel> listClientConverter = new ArrayList<>();
                listClientConverter = gson.fromJson(userPostString, listType);

                listClientConverter.forEach(clientModel -> {
                    try {
                        postVerify(response, gson, clientModel);
                    } catch (IOException ex) {
                        throw new RuntimeException("Error in postClient", ex);
                    }
                });
            }catch (Exception e1) {

            }
            ResponseSetter.setResponse("Error while trying to map the json. " + e.getMessage(), HttpServletResponse.SC_BAD_REQUEST, response,urlReturn);
        }
    }

    private void postVerify(SlingHttpServletResponse response, Gson gson, ClientModel objClientConverter) throws IOException {
        if (((objClientConverter.getName() == null || objClientConverter.getName().isEmpty()))){
            ResponseSetter.setResponse("Name is required", HttpServletResponse.SC_BAD_REQUEST, response,urlReturn);
        } else {
            clientDao.save(objClientConverter);
            ResponseSetter.setOkResponse(gson.toJson(objClientConverter), HttpServletResponse.SC_CREATED, response);
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

    @Override
    public void putClient(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");


        String userPostString = IOUtils.toString(request.getReader());
        Gson gson = new Gson();
        ClientModel objClientConverter = new ClientModel();
        try {
            objClientConverter = gson.fromJson(userPostString, ClientModel.class);
            putVerify(response, gson, objClientConverter);
        }catch (Exception e) {
            try{
                Type listType = new TypeToken<ArrayList<ClientModel>>(){}.getType();
                List<ClientModel> listClientConverter = new ArrayList<>();
                listClientConverter = gson.fromJson(userPostString, listType);

                listClientConverter.forEach(clientModel -> {
                    try {
                        putVerify(response, gson, clientModel);
                    } catch (IOException ex) {
                        throw new RuntimeException("Error while trying to update client. " + ex.getMessage());
                    }
                });

            }  catch (Exception e1) {
                ResponseSetter.setResponse("Cannot save client. Error while trying to map the json. "+e.getMessage(), HttpServletResponse.SC_BAD_REQUEST, response, urlReturn);
            }
        }
    }

    private void putVerify(SlingHttpServletResponse response, Gson gson, ClientModel objClientConverter) throws IOException {
        if(clientDao.getClient(objClientConverter.getId())!=null) {
            if (((objClientConverter.getId() <= 0) || (objClientConverter.getName() == null || objClientConverter.getName().isEmpty()))) {
                ResponseSetter.setResponse("Name is required", HttpServletResponse.SC_BAD_REQUEST, response, urlReturn);
            } else {
                if (clientDao.getClient(objClientConverter.getId()) != null) {
                    clientDao.update(objClientConverter);
                    ResponseSetter.setOkResponse(gson.toJson(objClientConverter), HttpServletResponse.SC_CREATED, response);
                } else {
                    ResponseSetter.setResponse("Cannot find clientId=" + objClientConverter.getId(), HttpServletResponse.SC_NOT_FOUND, response, urlReturn);
                }
            }
        }else {
            ResponseSetter.setResponse("Cannot find clientId=" + objClientConverter.getId(), HttpServletResponse.SC_NOT_FOUND, response, urlReturn);
        }
    }
}
