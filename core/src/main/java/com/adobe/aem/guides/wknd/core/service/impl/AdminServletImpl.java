package com.adobe.aem.guides.wknd.core.service.impl;


import com.adobe.aem.guides.wknd.core.auth.JwtEmulator;
import com.adobe.aem.guides.wknd.core.dao.AdminDao;
import com.adobe.aem.guides.wknd.core.models.ClientModel;
import com.adobe.aem.guides.wknd.core.models.UserModel;
import com.adobe.aem.guides.wknd.core.service.AdminService;
import com.adobe.aem.guides.wknd.core.utils.ResponseSetter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Component(service = AdminService.class, immediate = true)
public class AdminServletImpl implements AdminService {

    private final String urlReturn = "http://localhost:4502/bin/keepalive/adminServlet";

    @Reference
    private AdminDao adminDao;
    
    @Override
    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        if(request.getParameter("userId")==null) {
            List<UserModel> users = adminDao.getUsers();
            ResponseSetter.setOkResponse(new Gson().toJson(users), HttpServletResponse.SC_OK, response);
        }else {
            UserModel userModel = adminDao.getUser(Integer.parseInt(request.getParameter("userId")));
            if (userModel == null) {
                ResponseSetter.setResponse("Cannot find userId="+request.getParameter("userId"), HttpServletResponse.SC_NOT_FOUND, response,urlReturn);
            } else {
                ResponseSetter.setOkResponse(new Gson().toJson(userModel), HttpServletResponse.SC_OK, response);
            }
        }
    }

    @Override
    public void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String userPostString = IOUtils.toString(request.getReader());
        Gson gson = new Gson();
        UserModel objUserConverter = new UserModel();
        try {
            objUserConverter = gson.fromJson(userPostString, UserModel.class);

            postVerify(response, gson, objUserConverter);
        }catch (Exception e) {
            try {
                Type listType = new TypeToken<ArrayList<UserModel>>(){}.getType();
                List<UserModel> listUserConverter = new ArrayList<>();
                listUserConverter = gson.fromJson(userPostString, listType);

                listUserConverter.forEach(userModel -> {
                    try {
                        postVerify(response, gson, userModel);
                    } catch (IOException ex) {
                        throw new RuntimeException("Error in postUser", ex);
                    }
                });
            }catch (Exception e1) {

            }
            ResponseSetter.setResponse("Error while trying to map the json. " + e.getMessage(), HttpServletResponse.SC_BAD_REQUEST, response,urlReturn);
        }

    }
    private void postVerify(SlingHttpServletResponse response, Gson gson, UserModel objUserConverter) throws IOException {
        if (((objUserConverter.getUsername() == null || objUserConverter.getUsername().isEmpty()) || (objUserConverter.getPassword() == null || objUserConverter.getPassword().isEmpty()))) {
            ResponseSetter.setResponse("Name is required", HttpServletResponse.SC_BAD_REQUEST, response,urlReturn);
        } else {
            adminDao.save(objUserConverter);
            ResponseSetter.setOkResponse(gson.toJson(objUserConverter), HttpServletResponse.SC_CREATED, response);
        }
    }
    

    @Override
    public void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {

        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        if(request.getParameter("userId")==null) {
            ResponseSetter.setResponse("Cannot delete User. Provide userId", HttpServletResponse.SC_BAD_REQUEST, response,urlReturn);
        }else {
            UserModel userModel = adminDao.getUser(Integer.parseInt(request.getParameter("userId")));
            if (userModel == null) {
                ResponseSetter.setResponse("Cannot find userId="+request.getParameter("userId"), HttpServletResponse.SC_NOT_FOUND, response,urlReturn);
            }else {
                try {
                    adminDao.delete(Integer.parseInt(request.getParameter("userId")));
                    ResponseSetter.setOkResponse(new Gson().toJson("User deleted"), HttpServletResponse.SC_OK, response);
                } catch (Exception e) {
                    ResponseSetter.setResponse("Error while trying to delete user. " + e.getMessage(), HttpServletResponse.SC_BAD_REQUEST, response,urlReturn);
                }
            }
        }
    }

    @Override
    public void doPut(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");


        String userPostString = IOUtils.toString(request.getReader());
        Gson gson = new Gson();
        UserModel objUserConverter = new UserModel();
        try {
            objUserConverter = gson.fromJson(userPostString, UserModel.class);
            putVerify(response, gson, objUserConverter);
        }catch (Exception e) {
            try{
                Type listType = new TypeToken<ArrayList<UserModel>>(){}.getType();
                List<UserModel> listUserConverter = new ArrayList<>();
                listUserConverter = gson.fromJson(userPostString, listType);

                listUserConverter.forEach(userModel -> {
                    try {
                        putVerify(response, gson, userModel);
                    } catch (IOException ex) {
                        throw new RuntimeException("Error while trying to update user. " + ex.getMessage());
                    }
                });

            }  catch (Exception e1) {
                ResponseSetter.setResponse("Cannot save user. Error while trying to map the json. "+e.getMessage(), HttpServletResponse.SC_BAD_REQUEST, response, urlReturn);
            }
        }
        
    }

    private void putVerify(SlingHttpServletResponse response, Gson gson, UserModel objUserConverter) throws IOException {
        if(adminDao.getUser(objUserConverter.getId())!=null) {
            if (((objUserConverter.getId() <= 0) || (objUserConverter.getUsername() == null || objUserConverter.getUsername().isEmpty()) || (objUserConverter.getPassword() == null || objUserConverter.getPassword().isEmpty()))) {
                ResponseSetter.setResponse("Name is required", HttpServletResponse.SC_BAD_REQUEST, response, urlReturn);
            } else {
                if (adminDao.getUser(objUserConverter.getId()) != null) {
                    adminDao.update(objUserConverter);
                    ResponseSetter.setOkResponse(gson.toJson(objUserConverter), HttpServletResponse.SC_CREATED, response);
                } else {
                    ResponseSetter.setResponse("Cannot find userId=" + objUserConverter.getId(), HttpServletResponse.SC_NOT_FOUND, response, urlReturn);
                }
            }
        }else {
            ResponseSetter.setResponse("Cannot find userId=" + objUserConverter.getId(), HttpServletResponse.SC_NOT_FOUND, response, urlReturn);
        }
    }


}
