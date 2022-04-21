package com.adobe.aem.guides.wknd.core.filters;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.engine.EngineConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.osgi.service.component.propertytypes.ServiceVendor;

import javax.servlet.*;
import java.io.IOException;
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
        String URL = request.getRequestURL().toString();
        if(request.getRequestURI().contains("Service/") ){
            if(!(StringUtils.countMatches(URL.substring(URL.indexOf("Service/")+8), "/") >0)) {
                if (request.getMethod().equals("GET") || request.getMethod().equals("DELETE")) {
                    String service = URL.substring(URL.indexOf("/keepalive/") + 11, URL.indexOf("Service/"));
                    URL = URL.replace("Service/", "Service?" + service + "Id=");
                    response.sendRedirect(URL);

                }
            }else{
                String fix = URL.substring(0,URL.indexOf("Service/")+8);
                String rest = URL.substring(URL.indexOf("Service/")+8);
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
                URL = fix+"Service?"+ paramsBuilder;
                response.sendRedirect(URL);
            }

        }else if(!request.getRequestURI().contains("product/report") && request.getRequestURI().contains("product/")) {
            String userId = URL.substring(URL.indexOf("/product/")+9,URL.indexOf("/report"));
            String part = URL.substring(0,URL.indexOf("/product/")+9);
            String last = URL.substring(URL.indexOf("/report"));
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
