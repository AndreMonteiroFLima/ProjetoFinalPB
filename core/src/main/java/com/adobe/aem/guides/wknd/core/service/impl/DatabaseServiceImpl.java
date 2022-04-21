package com.adobe.aem.guides.wknd.core.service.impl;


import com.adobe.aem.guides.wknd.core.service.DatabaseService;
import com.day.commons.datasource.poolservice.DataSourcePool;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;

@Component(service = DatabaseService.class, immediate = true)
public class DatabaseServiceImpl implements DatabaseService {

    private final Logger logger = LoggerFactory.getLogger(DatabaseServiceImpl.class);

    @Reference
    private DataSourcePool dataSourcePool;

    @Override
    public Connection getConnection() {
        Connection connection = null;
        try {
            DataSource dataSource = (DataSource) dataSourcePool.getDataSource("aem_datasource");
            connection = dataSource.getConnection();
            logger.debug("Connection obtained");
        }
        catch (Exception e) {
            logger.debug("Unable to connect to database. Error message: " + e.getMessage());
        }
        return connection;
    }
}