package com.adobe.aem.guides.wknd.core.service.impl;


import com.adobe.aem.guides.wknd.core.auth.JwtEmulator;
import com.adobe.aem.guides.wknd.core.dao.AdminDao;
import com.adobe.aem.guides.wknd.core.dao.LoginDao;
import com.adobe.aem.guides.wknd.core.models.UserModel;
import com.adobe.aem.guides.wknd.core.service.LoginService;
import com.adobe.aem.guides.wknd.core.utils.ResponseSetter;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component(service = LoginService.class, immediate = true)
public class LoginServiceImpl implements LoginService {

    private final String urlReturn ="www.localhost:4502/bin/keepalive/loginServlet";

    @Reference
    private LoginDao loginDao;

    @Override
    public void login(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String userPostString = IOUtils.toString(request.getReader());
        Gson gson = new Gson();
        UserModel objUserConverter = new UserModel();
        try {
            objUserConverter = gson.fromJson(userPostString, UserModel.class);
            loginVerify(response, gson, objUserConverter);
        }catch (Exception e) {
            ResponseSetter.setResponse("Cannot login. Error while trying to map the json. "+e.getMessage(), HttpServletResponse.SC_BAD_REQUEST, response, urlReturn);
        }
    }

    private void loginVerify(SlingHttpServletResponse response, Gson gson, UserModel objUserConverter) throws IOException {
        if ((objUserConverter.getUsername() == null || objUserConverter.getUsername().isEmpty()) || (objUserConverter.getPassword() == null || objUserConverter.getPassword().isEmpty())) {
            ResponseSetter.setResponse("Provide all the required fields.", HttpServletResponse.SC_BAD_REQUEST, response, urlReturn);
        } else {
            UserModel user = loginDao.isValidUser(objUserConverter);
            if (user != null) {
                Cookie cookie = new Cookie("LoggedUser", "true");
                cookie.setMaxAge(900);
                response.addCookie(cookie);
                ResponseSetter.setOkResponse(gson.toJson("You now are logged"), HttpServletResponse.SC_OK, response);
            } else {
                ResponseSetter.setResponse("Login Fail", HttpServletResponse.SC_NOT_FOUND, response, urlReturn);
            }
        }
    }

    @Override
    public void logout(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        if(request.getCookie("LoggedUser") != null) {
            request.getCookie("LoggedUser").setMaxAge(0);
            response.addCookie(request.getCookie("LoggedUser"));
            ResponseSetter.setOkResponse(new Gson().toJson("You are now logged out"), HttpServletResponse.SC_OK, response);
        }
        else{
            ResponseSetter.setResponse("You are not logged", HttpServletResponse.SC_NOT_FOUND, response, urlReturn);
            }

    }
}
