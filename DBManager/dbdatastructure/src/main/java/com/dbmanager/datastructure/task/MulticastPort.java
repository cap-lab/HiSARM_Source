package com.dbmanager.datastructure.task;

public class MulticastPort implements Communication {
    public CommunicationType getCommunicationType() {
        return CommunicationType.MULTICAST;
    }
}
