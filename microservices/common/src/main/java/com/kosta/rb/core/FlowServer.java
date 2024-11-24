package com.kosta.rb.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosta.rb.actor.metrics.dynamicflow.ComputerFlowContext;
import com.kosta.rb.actor.metrics.dynamicflow.TrafficFlowContext;
import com.kosta.rb.actor.metrics.dynamicflow.abs.UniqueDynamicFlowContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FlowServer extends Thread{

    private FlowConnector flowConnector;
    private ServerSocket serverSocket;
    private ObjectMapper objectMapper = new ObjectMapper();
    private TrafficTracker trafficTracker;
    private final int SERVER_PORT = 9999;

    public FlowServer(FlowConnector flowConnector){
        this.flowConnector = flowConnector;
        this.trafficTracker = new TrafficTracker(flowConnector);
    }

    @Override
    public void run() {
        System.out.println("FlowServer Thread is running");

        try {
            ExecutorService executor = Executors.newFixedThreadPool(10);
            serverSocket = new ServerSocket(SERVER_PORT);
            while (true){
                System.out.println("소켓 연결 대기중");
                try {
                    Socket clientSocket = serverSocket.accept();
                    Runnable task = () -> handleClient(clientSocket);
                    executor.submit(task);
                } catch (IOException e) {
                    System.err.println("소켓 연결 중 오류 : " + e.getMessage());
                }

            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void onRun(){
        ComputerFlowContext clientCTX0 =
                ComputerFlowContext.builder()
                        .serverType("client")
                        .ip("0:0:0:0:0:0:0:1")
                        .port("0000")
                        .dirKey("client-postman")
                        .gridX(0)
                        .gridY(0)
                        .build();

        ComputerFlowContext clientCTX1 =
                ComputerFlowContext.builder()
                        .serverType("client")
                        .ip("0:0:0:0:0:0:0:1")
                        .port("5500")
                        .dirKey("client-thomas")
                        .gridX(0)
                        .gridY(1)
                        .build();
        flowConnector.insertFlow(clientCTX0);
        flowConnector.insertFlow(clientCTX1);
    }

    private void handleClient(Socket clientSocket) {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            System.out.println("클라이언트 연결됨 : " + clientSocket);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                //System.out.println("Received line: " + inputLine);
                msgHandle(clientSocket, inputLine);
            }
        } catch (IOException e) {
            System.err.println("클라이언트 처리 중 오류: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("클라이언트 소켓 닫기 중 오류: " + e.getMessage());
            }
        }
    }

    private void msgHandle(Socket clientSocket, String inputLine){
        //System.out.println("클라이언트로부터 받은 메시지: " + inputLine);
        UniqueDynamicFlowContext insertCTX = extractMetaDataValue(inputLine);
        flowConnector.insertFlow(insertCTX);
        /*

        if(insertCTX instanceof TrafficFlowContext){
            trafficTracker.enqueue((TrafficFlowContext) insertCTX);
        }
        else {

        }*/
    }

    private Class<? extends UniqueDynamicFlowContext> getMsgClassType(String className){
        Class<?> type = null;
        try {
            type = Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR MSG CLASS TYPE");
        }
        return (Class<? extends UniqueDynamicFlowContext>) type;
    }

    private <T extends UniqueDynamicFlowContext> T casting(String msg, Class<T> type){
        T ctx;
        try {
            ctx = objectMapper.readValue(msg, type);
        } catch (JsonProcessingException e) {
            System.out.println("ERROR CASTING ERROR");
            System.out.println(e);
            throw new RuntimeException(e);
        }
        return ctx;
    }


    public UniqueDynamicFlowContext extractMetaDataValue(String metaData) {
        String co = "@||:||@";
        if (metaData == null || !metaData.contains(co)) {
            System.out.println("META DATA ERROR : NULL OR META CONTAINS");
            return null;
        }

        int delimiterIndex = metaData.indexOf(co);
        if (delimiterIndex == -1) {
            System.out.println("META DATA ERROR : NOT VALID TYPE");
            return null;
        }

        String classTypeName = metaData.substring(0, delimiterIndex);
        String message = metaData.substring(delimiterIndex + co.length());

        //System.out.println("Class Type Name : " + classTypeName);
        //System.out.println("Message : " + message);

        Class<? extends UniqueDynamicFlowContext> msgClassType = getMsgClassType(classTypeName);
        UniqueDynamicFlowContext value = casting(message, msgClassType);

        return value;
    }

}
