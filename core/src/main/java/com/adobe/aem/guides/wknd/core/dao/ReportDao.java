package com.adobe.aem.guides.wknd.core.dao;

import com.adobe.aem.guides.wknd.core.exception.ClientDoesNotExistException;
import com.adobe.aem.guides.wknd.core.models.ReportModel;

public interface ReportDao {

    public ReportModel getReport(int clientId) throws ClientDoesNotExistException;
}
