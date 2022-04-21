package com.adobe.aem.guides.wknd.core.dao.Impl;


import com.adobe.aem.guides.wknd.core.dao.ClientDao;
import com.adobe.aem.guides.wknd.core.dao.InvoiceDao;
import com.adobe.aem.guides.wknd.core.dao.ProductDao;
import com.adobe.aem.guides.wknd.core.dao.ReportDao;
import com.adobe.aem.guides.wknd.core.exception.ClientDoesNotExistException;
import com.adobe.aem.guides.wknd.core.models.ClientModel;
import com.adobe.aem.guides.wknd.core.models.ReportModel;
import com.adobe.aem.guides.wknd.core.service.DatabaseService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component(service = ReportDao.class, immediate = true)
public class ReportDaoImpl implements ReportDao {

    @Reference
    private DatabaseService databaseService;

    @Reference
    private ClientDao clientDao;

    @Reference
    private InvoiceDao invoiceDao;

    @Reference
    private ProductDao productDao;

    @Override
    public ReportModel getReport(int clientId) throws ClientDoesNotExistException {

        ClientModel clientModel = clientDao.getClient(clientId);
        if(clientModel == null) {
            throw new ClientDoesNotExistException("Client with id " + clientId + " does not exist");
        }
        try(Connection connection = databaseService.getConnection()) {
            String sql = "SELECT i.invoice_id FROM invoice as i where client_id=?";

            ReportModel reportModel = new ReportModel();
            reportModel.setClient(clientModel);
            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1,clientId);
                try(java.sql.ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        reportModel.getInvoiceModels().add(invoiceDao.getInvoice(rs.getInt("invoice_id")));
                    }
                    reportModel.getInvoiceModels().forEach(invoice ->{
                        invoice.getInvoiceItens().forEach(item->{
                            reportModel.getProducts().add(productDao.getProduct(item));
                        });
                    });
                    return reportModel;
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage()+"Error while getting clients");
            }
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage()+"Error while trying to connect to database");
        }
    }
}
