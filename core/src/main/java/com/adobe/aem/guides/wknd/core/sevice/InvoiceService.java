package com.adobe.aem.guides.wknd.core.sevice;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import java.io.IOException;

public interface InvoiceService {
    
    public void getInvoice(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;
    
    public void postInvoice(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;
    
    public void deleteInvoice(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException;
}
