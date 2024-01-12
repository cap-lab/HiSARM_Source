package com.strategy.strategydatastructure.additionalinfo;

import java.util.List;

public class SimulationClient {
    private String allocationMethod;
    private List<ClientInfo> clientList;

    public String getAllocationMethod() {
        return allocationMethod;
    }

    public void setAllocationMethod(String allocationMethod) {
        this.allocationMethod = allocationMethod;
    }

    public List<ClientInfo> getClientList() {
        return clientList;
    }

    public void setClientList(List<ClientInfo> clientList) {
        this.clientList = clientList;
    }
}
