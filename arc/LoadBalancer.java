package com.kosta.hcr.arc;

import com.kosta.hcr.arc.module.connector.ServerConnector;

import java.util.concurrent.ConcurrentHashMap;

public class LoadBalancer {
    private ConcurrentHashMap<String, ServerConnector> serverConnector
            = new ConcurrentHashMap<>();

    public LoadBalancer(){

    }

    public void apiGate(String url){
    }

}
