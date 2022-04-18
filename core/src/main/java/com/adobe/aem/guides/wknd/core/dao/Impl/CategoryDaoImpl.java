package com.adobe.aem.guides.wknd.core.dao.Impl;

import com.adobe.aem.guides.wknd.core.dao.CategoryDao;
import com.adobe.aem.guides.wknd.core.models.CategoryModel;
import com.adobe.aem.guides.wknd.core.models.CategoryModel;
import com.adobe.aem.guides.wknd.core.sevice.DatabaseService;
import com.adobe.xfa.Int;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component(service = CategoryDao.class, immediate = true)
public class CategoryDaoImpl implements CategoryDao {

    @Reference
    private DatabaseService databaseService;

    @Override
    public void save(CategoryModel category) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "INSERT INTO categories (name ) VALUES ( ?)";

            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, category.getName());
                ps.execute();

            }catch (Exception e){
                throw new RuntimeException(e.getMessage()+"Error while saving category");
            }
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage()+"Error while trying to connect to database");
        }
    }

    @Override
    public List<CategoryModel> getCategorys() {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM categories";
            List<CategoryModel> categorys = new ArrayList<>();
            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                try(java.sql.ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        categorys.add(new CategoryModel(rs.getInt("id"), rs.getString("name")));
                    }
                    return categorys;
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage()+"Error while getting categorys");
            }
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage()+"Error while trying to connect to database");
        }
    }

    @Override
    public CategoryModel getCategory(int categoryId) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM categories WHERE id = ?";
            CategoryModel category = null;

            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, categoryId);
                try(ResultSet rs = ps.executeQuery()) {
                    while(rs.next()){
                        category = new CategoryModel(rs.getInt("id"), rs.getString("name"));
                    }
                    return category;
                }
            }catch (Exception e){
                throw new RuntimeException("Error while getting category: "+e.getMessage());
            }
        }catch (SQLException e){
            throw new RuntimeException("Error while trying to connect to database: "+e.getMessage());
        }
    }

    @Override
    public void delete(int categoryId) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "DELETE FROM categories WHERE id = ?";

            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, categoryId);
                ps.execute();

            }catch (Exception e){
                throw new RuntimeException("Error while deleting category: "+e.getMessage());
            }
        }catch (SQLException e){
            throw new RuntimeException("Error while trying to connect to database: "+e.getMessage());
        }
    }

    @Override
    public void update(CategoryModel category) {

    }

    public boolean exists(int categoryId){
        try(Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM categories WHERE id = ?";
            CategoryModel category = null;

            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, categoryId);
                try(ResultSet rs = ps.executeQuery()) {
                    while(rs.next()){
                        category = new CategoryModel(rs.getInt("id"), rs.getString("name"));
                    }
                    return category != null;
                }
            }catch (Exception e){
                throw new RuntimeException("Error while getting category: "+e.getMessage());
            }
        }catch (SQLException e){
            throw new RuntimeException("Error while trying to connect to database: "+e.getMessage());
        }
    }
}
