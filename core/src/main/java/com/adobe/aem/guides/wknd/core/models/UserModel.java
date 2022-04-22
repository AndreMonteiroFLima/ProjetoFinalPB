package com.adobe.aem.guides.wknd.core.models;

import com.google.gson.annotations.Expose;

public class UserModel {

    @Expose
    private int id;
    @Expose
    private String username;
    @Expose(serialize = false)
    private String password;

    public UserModel() {
    }

    public UserModel(int id,String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
/*
###Script###
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
  );
*/
