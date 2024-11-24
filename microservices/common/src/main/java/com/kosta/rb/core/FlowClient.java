package com.kosta.rb.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosta.common.core.data.argctx.ServerTypeContext;
import com.kosta.rb.actor.metrics.dynamicflow.ComputerFlowContext;
import com.kosta.rb.actor.metrics.dynamicflow.TrafficFlowContext;
import com.kosta.rb.actor.metrics.dynamicflow.abs.UniqueDynamicFlowContext;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class FlowClient extends Thread{

    private PrintWriter outPtr;
    private Socket socket;

    private ObjectMapper objectMapper = new ObjectMapper();
    private String serverAddress;
    private int port;

    private Runnable onRunServer;

    public FlowClient(String serverAddress, int port){
        this.serverAddress = serverAddress;
        this.port = port;
    }

    public void runable(Runnable runnable){
        onRunServer = runnable;
        run();
    }

    @Override
    public void run() {
        try {
            socket = new Socket(serverAddress, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outPtr = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("서버에 연결되었습니다.");
            onRunServer.run();

//            ComputerFlowContext clientCTX =
//                    ComputerFlowContext.builder()
//                            .serverType("test")
//                            .ip("1234")
//                            .port("456")
//                            .dirKey("client")
//                            .gridX(0)
//                            .gridY(0)
//                            .build();
//
//            send(outPtr, clientCTX, ComputerFlowContext.class);
//            System.out.println("MSG 발송함");

            /*
            String userInputLine;
            while ((userInputLine = userInput.readLine()) != null) {
                //System.out.println("서버로부터 응답: " + in.readLine()); // 서버로부터 응답 읽기
            }
            */

        } catch (Exception e) {
            System.out.println("METRIC SERVER NOT FOUND");
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    public void insertComputer(ServerTypeContext argsCtx){
        int gridX = 0;
        int gridY = 0;
        try {
            gridX = Integer.parseInt(argsCtx.getGridX());
            gridY = Integer.parseInt(argsCtx.getGridY());
        }
        catch (Exception e){
            System.out.println("Int Parse ERROR");
        }

        ComputerFlowContext clientCTX =
                ComputerFlowContext.builder()
                        .serverType(argsCtx.getServerType())
                        .dirKey(argsCtx.getServerLabel())
                        .ip(argsCtx.getServerAddress())
                        .port(argsCtx.getServerPort().toString())
                        .gridX(gridX)
                        .gridY(gridY)
                        .build();

        send(outPtr, clientCTX, ComputerFlowContext.class);
    }

    public void insertTraffic(UniqueDynamicFlowContext flowContext){
        send(outPtr, flowContext, (Class<UniqueDynamicFlowContext>) flowContext.getClass() );
    }

    public <T extends UniqueDynamicFlowContext> void send(PrintWriter out, T ctx, Class<T> type){
        System.out.println("OUT PTR : " + outPtr);

        JSONObject json = null;
        try {
            json = new JSONObject(ctx);
        }
        catch (Exception e){
            System.out.println("SEND ERROR : " + e);
        }
        out.println( metaData(type.getName(), json.toString()));
        out.flush();
        System.out.println("MSG 발송함 : " + metaData(type.getName(), json.toString()) );
    }

    /*
    public <T extends UniqueDynamicFlowContext> void send(PrintWriter out, String msg, Class<T> type){
        System.out.println("OUT PTR : " + outPtr);

        out.println(metaData(type.getName(), msg));
        out.flush();
    }
    */

    private String metaData(String classTypeName, String msg){
        String co = "@||:||@";
        return classTypeName + co + msg;
    }

    private void closeResources() {
        try {
            if (outPtr != null) {
                outPtr.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
