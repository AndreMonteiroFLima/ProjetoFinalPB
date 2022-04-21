package com.adobe.aem.guides.wknd.core.models;

import java.util.ArrayList;
import java.util.List;

public class ReportModel {

    private ClientModel client;
    private List<InvoiceModel> invoiceModels;
    private List<ProductModel> products;

    public ReportModel() {
        invoiceModels= new ArrayList<>();
        products= new ArrayList<>();
    }

    public ReportModel(ClientModel client, List<InvoiceModel> invoiceModels, List<ProductModel> products) {
        this.client = client;
        this.invoiceModels = invoiceModels;
        this.products = products;
    }

    public ClientModel getClient() {
        return client;
    }

    public void setClient(ClientModel client) {
        this.client = client;
    }

    public List<InvoiceModel> getInvoiceModels() {
        return invoiceModels;
    }

    public void setInvoiceModels(List<InvoiceModel> invoiceModels) {
        this.invoiceModels = invoiceModels;
    }

    public List<ProductModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductModel> products) {
        this.products = products;
    }
}
