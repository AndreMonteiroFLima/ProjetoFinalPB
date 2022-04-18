package com.adobe.aem.guides.wknd.core.servlets;

import com.adobe.aem.guides.wknd.core.dao.InvoiceDao;
import com.adobe.aem.guides.wknd.core.dao.ProductDao;
import com.adobe.aem.guides.wknd.core.models.ErroMessage;
import com.adobe.aem.guides.wknd.core.models.InvoiceModel;
import com.adobe.aem.guides.wknd.core.sevice.DatabaseService;
import com.adobe.aem.guides.wknd.core.sevice.InvoiceService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.apache.sling.api.servlets.ServletResolverConstants.*;


/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link org.apache.sling.api.servlets.SlingAllMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */


@Component(immediate = true, service = Servlet.class, property = {
        SLING_SERVLET_METHODS + "=" + "POST",
        SLING_SERVLET_METHODS + "=" + "GET",
        SLING_SERVLET_METHODS + "=" + "DELETE",
        SLING_SERVLET_PATHS + "=" + "/bin/keepalive/invoiceService",
        SLING_SERVLET_EXTENSIONS + "=" + "txt", SLING_SERVLET_EXTENSIONS + "=" + "json"})

@ServiceDescription("Invoice Service All")
public class InvoiceServlet extends SlingAllMethodsServlet {

    @Reference
    private InvoiceService invoiceService;


    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws ServletException, IOException {
        invoiceService.getInvoice(request, response);
    }

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws ServletException, IOException {
        invoiceService.postInvoice(request, response);
    }

    @Override
    protected void doDelete(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws ServletException, IOException {
       invoiceService.deleteInvoice(request, response);
    }
}
