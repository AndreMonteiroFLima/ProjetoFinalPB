package com.adobe.aem.guides.wknd.core.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductModel {

    private int id;
    private String name;
    private List<Integer> categoryId;
    private String description;
    private BigDecimal price;
    static List<ProductModel> produtos;

    /*
    public ProductModel(int id, String name, String category, String description, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
    }*/

    public ProductModel(int id, String name, List<Integer> category, String description, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.categoryId = category;
        this.description = description;
        this.price = price;
    }

    public ProductModel() {
        categoryId = new ArrayList<>();
        produtos= new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

/*    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }*/

    public List<Integer> getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(List<Integer> categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public static List<ProductModel> getProdutos() {
        return produtos;
    }

    public static void setProdutos(List<ProductModel> produtos) {
        ProductModel.produtos = produtos;
    }
}
/*
###Script###
CREATE TABLE `product`
(
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `category` varchar(255) NOT NULL,
  `description` text NOT NULL,
  `price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`)
);
*/
