package com.kosta.rb.core.abs;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface HttpHandler {
    public void doResponse(int statusCode, String response) throws JsonProcessingException;

}
