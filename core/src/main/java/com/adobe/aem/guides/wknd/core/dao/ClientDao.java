package com.adobe.aem.guides.wknd.core.dao;


import com.adobe.aem.guides.wknd.core.models.ClientModel;

import java.util.List;

public interface ClientDao {

    public void save(ClientModel client);

    public List<ClientModel> getClients();

    public ClientModel getClient(int clientId);

    public void delete(int clientId);

    public void update(ClientModel client);
}
