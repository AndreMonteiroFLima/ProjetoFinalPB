package com.adobe.aem.guides.wknd.core.sevice;

import java.sql.Connection;

public interface DatabaseService {
    Connection getConnection();
}