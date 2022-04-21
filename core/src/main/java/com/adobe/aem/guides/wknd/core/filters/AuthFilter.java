/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *//*

package com.adobe.aem.guides.wknd.core.filters;

import com.adobe.aem.guides.wknd.core.auth.JwtEmulator;
import com.adobe.aem.guides.wknd.core.exception.JwtInvalidException;
import com.adobe.aem.guides.wknd.core.utils.ResponseSetter;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.engine.EngineConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.osgi.service.component.propertytypes.ServiceVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

*/
/**
 * Simple servlet filter component that logs incoming requests.
 *//*

@Component(service = Filter.class,
           property = {
                   EngineConstants.SLING_FILTER_SCOPE + "=" + EngineConstants.FILTER_SCOPE_REQUEST,
           })
@ServiceDescription("Demo to filter incoming requests")
@ServiceRanking(-5000)
@ServiceVendor("Adobe")
public class AuthFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private ResourceResolver resourceResolver;

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
                         final FilterChain filterChain) throws IOException, ServletException {
        final SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
        final SlingHttpServletResponse slingResponse = (SlingHttpServletResponse) response;

        if(slingRequest.getRequestURI().contains("Service") || (slingRequest.getRequestURI().contains("/report") && slingRequest.getRequestURI().contains("/product"))){
            if (slingRequest.getCookie("JWT") == null) {
                logger.info("No JWT cookie found");
                Session session = resourceResolver.adaptTo(Session.class);
                if(session == null)
                    filterChain.doFilter(request, response);
                String JwtToken = JwtEmulator.generateJwt(session.getUserID(), "localhost:4502");
                Cookie cookie = new Cookie("JWT", JwtToken);
                cookie.setMaxAge(900);
                slingResponse.addCookie(cookie);
            } else {
                logger.info("JWT cookie found");
                try {
                    JwtEmulator.validateJwt(slingRequest.getCookie("JWT").getValue());
                } catch (JwtInvalidException e) {
                    ResponseSetter.setResponse(e.getMessage(), HttpServletResponse.SC_UNAUTHORIZED, slingResponse, "localhost:4502");
                } catch (Exception e) {
                    ResponseSetter.setResponse(e.getMessage(), HttpServletResponse.SC_BAD_REQUEST, slingResponse, "localhost:4502");
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

}*/
