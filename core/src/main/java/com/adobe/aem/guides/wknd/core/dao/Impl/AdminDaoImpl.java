package com.adobe.aem.guides.wknd.core.dao.Impl;

import com.adobe.aem.guides.wknd.core.dao.AdminDao;
import com.adobe.aem.guides.wknd.core.models.ClientModel;
import com.adobe.aem.guides.wknd.core.models.UserModel;
import com.adobe.aem.guides.wknd.core.service.DatabaseService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component(service = AdminDao.class, immediate = true)
public class AdminDaoImpl implements AdminDao {

    @Reference
    private DatabaseService databaseService;

    @Override
    public void save(UserModel user) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "INSERT INTO users (username, password) VALUES ( ?,?)";

            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                ps.execute();

            }catch (Exception e){
                throw new RuntimeException(e.getMessage()+"Error while saving user");
            }
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage()+"Error while trying to connect to database");
        }
    }

    @Override
    public List<UserModel> getUsers() {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM users";
            List<UserModel> users = new ArrayList<>();
            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                try(ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        users.add(new UserModel(rs.getInt("id"), rs.getString("username"), rs.getString("password")));
                    }
                    return users;
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage()+"Error while getting users");
            }
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage()+"Error while trying to connect to database");
        }
        
    }

    @Override
    public UserModel getUser(int userId) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM users WHERE id = ?";
            UserModel user = null;
            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, userId);
                try(ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        user = new UserModel(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
                    }
                    return user;
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage()+"Error while getting users");
            }
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage()+"Error while trying to connect to database");
        }

    }

    @Override
    public void delete(int userId) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "DELETE FROM users WHERE id = ?";
            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.execute();
            }catch (Exception e){
                throw new RuntimeException(e.getMessage()+"Error while deleting user");
            }
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage()+"Error while trying to connect to database");
        }
    }

    @Override
    public void update(UserModel user) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "UPDATE users SET username = ?, password = ? WHERE id = ?";
            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                ps.setInt(3, user.getId());
                ps.execute();
            }catch (Exception e){
                throw new RuntimeException(e.getMessage()+"Error while updating user");
            }
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage()+"Error while trying to connect to database");
        }
    }

    @Override
    public boolean exists(int userId) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM users WHERE id = ?";
            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, userId);
                try(ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage()+"Error while getting users");
            }
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage()+"Error while trying to connect to database");
        }
    }

}
