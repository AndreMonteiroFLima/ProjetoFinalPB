package com.adobe.aem.guides.wknd.core.dao.Impl;


import com.adobe.aem.guides.wknd.core.dao.CategoryDao;
import com.adobe.aem.guides.wknd.core.dao.ProductDao;
import com.adobe.aem.guides.wknd.core.exception.ProductDoesNotExistException;
import com.adobe.aem.guides.wknd.core.models.ProductModel;
import com.adobe.aem.guides.wknd.core.sevice.DatabaseService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Component(service = ProductDao.class, immediate = true)
public class ProductDaoImpl implements ProductDao {

    @Reference
    private DatabaseService databaseService;
    @Reference
    private CategoryDao categoryDao;

    @Override
    public void save(ProductModel product) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "INSERT INTO product (name , description, price) VALUES ( ? , ?, ?)";

            try(PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, product.getName());
                ps.setString(2, product.getDescription());
                ps.setBigDecimal(3, product.getPrice());
                ps.execute();
                ResultSet rs=ps.getGeneratedKeys();
                if(rs.next()) {
                    saveProductCategory(product.getCategoryId(), rs.getInt(1));
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage()+"Error while saving product");
            }
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage()+"Error while trying to connect to database");
        }
    }

    private void saveProductCategory(List<Integer> categories,int productId){
        for (Integer categoryId : categories) {
            try (Connection connection = databaseService.getConnection()) {
                String sql = "INSERT INTO product_category (product_id, category_id) VALUES ( ? , ? )";

                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setInt(1, productId);
                    ps.setInt(2, categoryId);
                    ps.execute();
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage() + "Error while saving product category");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage() + "Error while trying to connect to database");
            }
        }
    }

    @Override
    public List<ProductModel> getProducts() {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM product";

            List<ProductModel> products = new ArrayList<>();
            List<Integer> categories = new ArrayList<>();

            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                try(ResultSet rs = ps.executeQuery()) {
                    while(rs.next()){
                        categories=getProductCategories(rs.getInt("id"));
                        products.add(new ProductModel(rs.getInt("id"), rs.getString("name"), categories, rs.getString("description"), rs.getBigDecimal("price")));
                    }
                    return products;
                }

            }catch (Exception e){
                throw new RuntimeException("Error while getting products: "+e.getMessage());
            }
        }catch (SQLException e){
            throw new RuntimeException("Error while trying to connect to database: "+e.getMessage());
        }
    }

    private List<Integer> getProductCategories(int productId){
        try(Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM product_category WHERE product_id = ?";

            List<Integer> categories = new ArrayList<>();

            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, productId);
                try(ResultSet rs = ps.executeQuery()) {
                    while(rs.next()){
                        categories.add(rs.getInt("category_id"));
                    }
                    return categories;
                }
            }catch (Exception e){
                throw new RuntimeException("Error while getting product categories: "+e.getMessage());
            }
        }catch (SQLException e){
            throw new RuntimeException("Error while trying to connect to database: "+e.getMessage());
        }
    }

    @Override
    public ProductModel getProduct(int productId) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM product WHERE id = ?";
            ProductModel product = null;

            List<Integer> categories = new ArrayList<>();
            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, productId);
                try(ResultSet rs = ps.executeQuery()) {
                    while(rs.next()){
                        categories=getProductCategories(rs.getInt("id"));
                        product = new ProductModel(rs.getInt("id"), rs.getString("name"), categories, rs.getString("description"), rs.getBigDecimal("price"));
                    }
                    return product;
                }
            }catch (Exception e){
                throw new RuntimeException("Error while getting product: "+e.getMessage());
            }
        }catch (SQLException e){
            throw new RuntimeException("Error while trying to connect to database: "+e.getMessage());
        }
    }

    @Override
    public void delete(int productId) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "DELETE FROM product WHERE id = ?";

            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                deleteProductCategories(productId);
                ps.setInt(1, productId);
                ps.execute();

            }catch (Exception e){
                throw new RuntimeException("Error while deleting product: "+e.getMessage());
            }
        }catch (SQLException e){
            throw new RuntimeException("Error while trying to connect to database: "+e.getMessage());
        }
    }

    private void deleteProductCategories(int productId){
        try(Connection connection = databaseService.getConnection()) {
            String sql = "DELETE FROM product_category WHERE product_id = ?";

            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, productId);
                ps.execute();

            }catch (Exception e){
                throw new RuntimeException("Error while deleting product categories: "+e.getMessage());
            }
        }catch (SQLException e){
            throw new RuntimeException("Error while trying to connect to database: "+e.getMessage());
        }
    }

    @Override
    public void update(ProductModel product) {

    }

    @Override
    public Boolean verifyProducts(List<Integer> invoiceItens){
        for (Integer item : invoiceItens) {
            try (Connection connection = databaseService.getConnection()) {
                String sql = "SELECT * FROM product as p WHERE p.id = ?";
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setInt(1, item);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new ProductDoesNotExistException("Product does not exist.");
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error while saving invoice: " + e.getMessage());
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error while trying to connect to database:"+ e.getMessage());
            }
        }
        return true;
    }

}
