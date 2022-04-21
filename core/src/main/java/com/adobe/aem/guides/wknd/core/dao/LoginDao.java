package com.adobe.aem.guides.wknd.core.dao;

import com.adobe.aem.guides.wknd.core.models.UserModel;

public interface LoginDao {

    public UserModel isValidUser(UserModel objUserConverter);
}
