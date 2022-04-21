package com.adobe.aem.guides.wknd.core.filters;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.engine.EngineConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.osgi.service.component.propertytypes.ServiceVendor;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;


/**
 * Simple servlet filter component that logs incoming requests.
 */

@Component(service = Filter.class,
        property = {
                EngineConstants.SLING_FILTER_SCOPE + "=" + EngineConstants.FILTER_SCOPE_REQUEST,
        })
@ServiceDescription("Demo to filter incoming requests")
@ServiceRanking(-700)
@ServiceVendor("Adobe")
public class ParamFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        SlingHttpServletRequest request = (SlingHttpServletRequest) servletRequest;
        SlingHttpServletResponse response = (SlingHttpServletResponse) servletResponse;
        String URLString = request.getRequestURL().toString();
        if(request.getRequestURI().contains("Service/") ){
            if(!(StringUtils.countMatches(URLString.substring(URLString.indexOf("Service/")+8), "/") >0)) {
                if (request.getMethod().equals("GET") || request.getMethod().equals("DELETE")) {
                    String service = URLString.substring(URLString.indexOf("/keepalive/") + 11, URLString.indexOf("Service/"));
                    URLString = URLString.replace("Service/", "Service?" + service + "Id=");
                    response.sendRedirect(URLString);
                }
            }else{
                String fix = URLString.substring(0,URLString.indexOf("Service/")+8);
                String rest = URLString.substring(URLString.indexOf("Service/")+8);
                String[] parts = StringUtils.split(rest, "/");
                StringBuilder paramsBuilder = new StringBuilder();
                int params = 0;
                for(String part: parts){
                    if(part.matches("\\d+")){
                        String category= "category="+part;
                        if(params>0)
                            paramsBuilder.append("&");
                        paramsBuilder.append(category);
                        params++;
                    }else if(part.equals("priceDown")){
                        String priceDown = "priceDown";
                        if(params>0)
                            paramsBuilder.append("&");
                        paramsBuilder.append(priceDown);
                        params++;
                    }else{
                        String word = "word="+part;
                        if(params>0)
                            paramsBuilder.append("&");
                        params++;
                        paramsBuilder.append(word);
                    }
                }
                URLString = fix+"Service?"+ paramsBuilder;
                response.sendRedirect(URLString);
            }

        }else if(!request.getRequestURI().contains("product/report") && request.getRequestURI().contains("product/")) {
            String userId = URLString.substring(URLString.indexOf("/product/")+9,URLString.indexOf("/report"));
            String part = URLString.substring(0,URLString.indexOf("/product/")+9);
            String last = URLString.substring(URLString.indexOf("/report"));
            StringBuilder URL1 = new StringBuilder();
            last=last.replace("/","");
            URL1.append(part).append(last).append("?userId=").append(userId);
            response.sendRedirect(URL1.toString());
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
