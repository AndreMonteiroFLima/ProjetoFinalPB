package com.adobe.aem.guides.wknd.core.service;

import java.sql.Connection;

public interface DatabaseService {
    Connection getConnection();
}