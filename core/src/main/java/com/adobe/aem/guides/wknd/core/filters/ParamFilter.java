package com.adobe.aem.guides.wknd.core.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.engine.EngineConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.osgi.service.component.propertytypes.ServiceVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
        if(request.getRequestURI().contains("Service/") ){
            if(request.getMethod().equals("GET") || request.getMethod().equals("DELETE")){
                String URL = request.getRequestURL().toString();
                String service = URL.substring(URL.indexOf("/keepalive/")+11,URL.indexOf("Service/"));
                URL=URL.replace("Service/","Service?"+service+"Id=");
                response.sendRedirect(URL);
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
