package com.adobe.aem.guides.wknd.core.models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvoiceModel {

    private int invoiceId;
    private Date invoiceDate;
    private BigDecimal invoiceValue;
    private List<Integer> invoiceItens;
    private int clientId;


    public InvoiceModel() {
        List<ProductModel> productsList = new ArrayList<>();
    }

    public InvoiceModel(int invoiceId, Date invoiceDate, BigDecimal invoiceValue, List<Integer> invoiceItems, int clientId) {
        this.invoiceId = invoiceId;
        this.invoiceDate = invoiceDate;
        this.invoiceValue = invoiceValue;
        this.invoiceItens = invoiceItems;
        this.clientId = clientId;
    }

    public InvoiceModel(int invoiceId, Timestamp invoiceDate, BigDecimal invoiceValue, List<Integer> invoiceItems, int clientId) {
        this.invoiceId = invoiceId;
        this.invoiceDate = new Date(invoiceDate.getTime());
        this.invoiceValue = invoiceValue;
        this.invoiceItens = invoiceItems;
        this.clientId = clientId;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getInvoiceValue() {
        return invoiceValue;
    }

    public void setInvoiceValue(BigDecimal invoiceValue) {
        this.invoiceValue = invoiceValue;
    }

    public List<Integer> getInvoiceItens() {
        return invoiceItens;
    }

    public void setInvoiceItens(List<Integer> invoiceItens) {
        this.invoiceItens = invoiceItens;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

}
/*
###Script###
CREATE TABLE invoice (
    invoice_id VARCHAR(255) NOT NULL,
    invoice_date TIMESTAMP NOT NULL,
    invoice_value DECIMAL(10,2) NOT NULL,
    client_id INT NOT NULL,
    PRIMARY KEY (invoice_number)
);
*/
