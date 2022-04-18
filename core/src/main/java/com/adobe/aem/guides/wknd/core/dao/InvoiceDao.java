package com.adobe.aem.guides.wknd.core.dao;

import com.adobe.aem.guides.wknd.core.exception.ProductDoesNotExistException;
import com.adobe.aem.guides.wknd.core.models.InvoiceModel;
import com.adobe.aem.guides.wknd.core.models.ProductModel;

import java.util.List;

public interface InvoiceDao {

    public void save(InvoiceModel invoice);

    public List<InvoiceModel> getInvoices();

    public InvoiceModel getInvoice(int invoiceId);

    public void delete(int invoiceId);

    public void update(InvoiceModel invoice);

}
