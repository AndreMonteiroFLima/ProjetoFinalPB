package com.adobe.aem.guides.wknd.core.servlets;/*
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
 */
/*
package com.adobe.aem.guides.wknd.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import com.adobe.aem.guides.wknd.core.models.User;
//import com.adobe.cq.wcm.core.components.models.List;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import static org.apache.sling.api.servlets.ServletResolverConstants.*;

*/
/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link org.apache.sling.api.servlets.SlingAllMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link org.apache.sling.api.servlets.SlingAllMethodsServlet}.
 *//*


@Component(immediate = true, service = Servlet.class, property = {
        SLING_SERVLET_METHODS + "=" + "POST",
        SLING_SERVLET_PATHS + "=" + "/bin/keepalive/userService",
        SLING_SERVLET_EXTENSIONS + "=" + "txt", SLING_SERVLET_EXTENSIONS + "=" + "json"})

@ServiceDescription("User Service All")
public class UserServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;

    String json;

    @Override
    protected void doGet(final SlingHttpServletRequest req,
                         final SlingHttpServletResponse resp) throws ServletException, IOException {
        // final Resource resource = req.getResource();

        User user = new User();
        List<User> userList = new ArrayList<>();
        userList = user.getUsers();
        List<User> userTemp = new ArrayList<>();
        try {
            String name = req.getParameter("username");

            if (name == null || name.isEmpty() || name.isBlank()) {
                userTemp = null;
            }

            else {
                for (int i = 0; i < userList.size(); i++) {
                    if (userList.get(i).getNome().toLowerCase().contains(name.toLowerCase())) {
                        userTemp = (Arrays.asList(userList.get(i)));
                        break;
                    }
                }
            }
        } catch(Exception e) {
            e.getStackTrace();
        }

        String json = new Gson().toJson(userTemp);
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
            throws IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        User userAdd = new User();

        String userPostString = IOUtils.toString(request.getReader());
        Gson gson = new Gson();
        User objUserConverter = new User();
        objUserConverter = gson.fromJson(userPostString, User.class);
        userAdd.addUser(objUserConverter);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.println(objUserConverter.getUsers().toString());

    }

}
*/
