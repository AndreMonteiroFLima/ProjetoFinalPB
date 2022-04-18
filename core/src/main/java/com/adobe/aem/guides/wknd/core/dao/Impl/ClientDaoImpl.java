package com.adobe.aem.guides.wknd.core.dao.Impl;

import com.adobe.aem.guides.wknd.core.dao.ClientDao;
import com.adobe.aem.guides.wknd.core.models.ClientModel;
import com.adobe.aem.guides.wknd.core.models.ProductModel;
import com.adobe.aem.guides.wknd.core.sevice.DatabaseService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component(service = ClientDao.class, immediate = true)    
public class ClientDaoImpl implements ClientDao {
    
    @Reference
    private DatabaseService databaseService;
    
    @Override
    public void save(ClientModel client) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "INSERT INTO client (name ) VALUES ( ?)";

            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, client.getName());
                ps.execute();

            }catch (Exception e){
                throw new RuntimeException(e.getMessage()+"Error while saving client");
            }
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage()+"Error while trying to connect to database");
        }
    }

    @Override
    public List<ClientModel> getClients() {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM client";
            List<ClientModel> clients = new ArrayList<>();
            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                try(java.sql.ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        clients.add(new ClientModel(rs.getInt("id"), rs.getString("name")));
                    }
                    return clients;
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage()+"Error while getting clients");
            }
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage()+"Error while trying to connect to database");
        }
    }

    @Override
    public ClientModel getClient(int clientId) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM client WHERE id = ?";
            ClientModel client = null;

            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, clientId);
                try(ResultSet rs = ps.executeQuery()) {
                    while(rs.next()){
                        client = new ClientModel(rs.getInt("id"), rs.getString("name"));
                    }
                    return client;
                }
            }catch (Exception e){
                throw new RuntimeException("Error while getting client: "+e.getMessage());
            }
        }catch (SQLException e){
            throw new RuntimeException("Error while trying to connect to database: "+e.getMessage());
        }
        
    }

    @Override
    public void delete(int clientId) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "DELETE FROM client WHERE id = ?";

            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, clientId);
                ps.execute();

            }catch (Exception e){
                throw new RuntimeException("Error while deleting client: "+e.getMessage());
            }
        }catch (SQLException e){
            throw new RuntimeException("Error while trying to connect to database: "+e.getMessage());
        }
    }

    @Override
    public void update(ClientModel client) {

    }
}
