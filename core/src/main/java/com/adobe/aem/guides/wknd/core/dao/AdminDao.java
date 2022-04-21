package com.adobe.aem.guides.wknd.core.dao;

import com.adobe.aem.guides.wknd.core.models.CategoryModel;
import com.adobe.aem.guides.wknd.core.models.UserModel;

import java.util.List;

public interface AdminDao {

    public void save(UserModel user);

    public List<UserModel> getUsers();

    public UserModel getUser(int userId);

    public void delete(int userId);

    public void update(UserModel user);

    public boolean exists(int userId);
}
