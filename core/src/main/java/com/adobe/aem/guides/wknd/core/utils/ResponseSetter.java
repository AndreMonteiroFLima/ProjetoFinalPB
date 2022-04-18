package com.adobe.aem.guides.wknd.core.utils;

import com.adobe.aem.guides.wknd.core.models.ErroMessage;
import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseSetter {

    public static void setOkResponse(String message, int httpCode, SlingHttpServletResponse response) throws IOException {
        response.setStatus(httpCode);
        response.getWriter().write(message);
    }

    public static void setResponse(String message, int httpCode, SlingHttpServletResponse response,String urlReturn) throws IOException {
        ErroMessage errorMessage = new ErroMessage(message, httpCode, urlReturn);
        response.setStatus(httpCode);
        response.getWriter().write(new Gson().toJson(errorMessage));
    }

}
