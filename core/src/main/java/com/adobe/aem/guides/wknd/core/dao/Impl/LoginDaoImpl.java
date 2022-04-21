package com.adobe.aem.guides.wknd.core.dao.Impl;

import com.adobe.aem.guides.wknd.core.dao.LoginDao;
import com.adobe.aem.guides.wknd.core.models.UserModel;
import com.adobe.aem.guides.wknd.core.service.DatabaseService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component(service = LoginDao.class, immediate = true)
public class LoginDaoImpl implements LoginDao {

    @Reference
    private DatabaseService databaseService;

    @Override
    public UserModel isValidUser(UserModel objUserConverter) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            UserModel user = null;
            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, objUserConverter.getUsername());
                ps.setString(2, objUserConverter.getPassword());
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
}