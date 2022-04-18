package com.adobe.aem.guides.wknd.core.dao.Impl;

import com.adobe.aem.guides.wknd.core.dao.InvoiceDao;
import com.adobe.aem.guides.wknd.core.exception.ProductDoesNotExistException;
import com.adobe.aem.guides.wknd.core.models.InvoiceModel;
import com.adobe.aem.guides.wknd.core.models.ProductModel;
import com.adobe.aem.guides.wknd.core.sevice.DatabaseService;
import org.apache.tika.metadata.PDF;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component(service = InvoiceDao.class, immediate = true)
public class InvoiceDaoImpl implements InvoiceDao {

    @Reference
    private DatabaseService databaseService;

    @Override
    public void save(InvoiceModel invoice) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "INSERT INTO invoice (invoice_date, invoice_value, client_id) VALUES ( ?, ?, ?)";

            try(PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setTimestamp(1, new Timestamp(invoice.getInvoiceDate().getTime()));
                ps.setBigDecimal(2, invoice.getInvoiceValue());
                ps.setInt(3, invoice.getClientId());
                ps.execute();
                ResultSet rs=ps.getGeneratedKeys();
                if(rs.next()) {
                    saveItems(invoice.getInvoiceItens(), rs.getInt(1));
                }
            }catch (Exception e){
                throw new RuntimeException("Error while saving invoice: " + e.getMessage());
            }
        }catch (SQLException e){
            throw new RuntimeException("Error while trying to connect to database: " + e.getMessage());
        }
    }

    private void saveItems(List<Integer> invoiceItens, int invoiceId) {
        for (Integer item : invoiceItens) {
            try (Connection connection = databaseService.getConnection()) {
                String sql = "INSERT INTO invoice_product (invoice_id,product_id) VALUES ( ?, ?)";
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setInt(1, invoiceId);
                    ps.setInt(2, item);
                    ps.execute();
                } catch (Exception e) {
                    throw new RuntimeException("Error while saving invoice: " + e.getMessage());
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error while trying to connect to database: " + e.getMessage());
            }
        }
    }


    @Override
    public List<InvoiceModel> getInvoices() {
        try (Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM invoice";
            List<InvoiceModel> invoices = new ArrayList<>();
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    List<Integer> invoiceItens = getInvoicesProducts(rs.getInt("invoice_id"));
                    invoices.add(new InvoiceModel(rs.getInt("invoice_id"), rs.getTimestamp("invoice_date"), rs.getBigDecimal("invoice_value"), invoiceItens, rs.getInt("client_id")));
                }
                return invoices;
            } catch (Exception e) {
                throw new RuntimeException("Error while getting invoices: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while trying to connect to database: " + e.getMessage());
        }
    }

    private List<Integer> getInvoicesProducts(int id) {
        try (Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM invoice_product WHERE invoice_id = ?";

            List<Integer> invoiceItens = new ArrayList<>();

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        invoiceItens.add(rs.getInt("product_id"));
                    }
                    return invoiceItens;
                }
            } catch (Exception e) {
                throw new RuntimeException("Error while getting product: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while trying to connect to database: " + e.getMessage());
        }
    }

    @Override
    public void delete(int invoiceId) {
        try (Connection connection = databaseService.getConnection()) {
            String sql = "DELETE FROM invoice WHERE invoice_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, invoiceId);
                deleteInvoiceProducts(invoiceId);
                ps.execute();
            } catch (Exception e) {
                throw new RuntimeException("Error while deleting invoice: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while trying to connect to database: " + e.getMessage());
        }
    }

    private void deleteInvoiceProducts(int invoiceId) {
        try (Connection connection = databaseService.getConnection()) {
            String sql = "DELETE FROM invoice_product WHERE invoice_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, invoiceId);
                ps.execute();
            } catch (Exception e) {
                throw new RuntimeException("Error while deleting invoice: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while trying to connect to database: " + e.getMessage());
        }
    }

    @Override
    public InvoiceModel getInvoice(int invoiceId) {
        try (Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM invoice WHERE invoice_id = ?";
            InvoiceModel invoice = null;
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, invoiceId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    List<Integer> invoiceItens = getInvoicesProducts(rs.getInt("invoice_id"));
                   invoice= new InvoiceModel(rs.getInt("invoice_id"), rs.getDate("invoice_date"), rs.getBigDecimal("invoice_value"),invoiceItens, rs.getInt("client_id"));
                }
                return invoice;
            }catch (Exception e){
                throw new RuntimeException("Error while getting invoices: " + e.getMessage());
            }
        }catch (SQLException e){
            throw new RuntimeException("Error while trying to connect to database: " + e.getMessage());
        }
    }

    @Override
    public void update(InvoiceModel invoice) {

    }


}
