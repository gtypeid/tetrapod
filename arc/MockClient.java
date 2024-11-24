package com.kosta.hcr.arc;

public class MockClient {
    private LoadBalancer loadBalancer;

    public MockClient(LoadBalancer loadBalancer){
        this.loadBalancer = loadBalancer;
    }
    public void request(String api){
        loadBalancer.apiGate(api);
    }
}
