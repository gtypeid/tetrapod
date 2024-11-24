package com.kosta.rb.core;

import com.kosta.rb.actor.metrics.dynamicflow.ComputerFlowContext;
import com.kosta.rb.actor.metrics.dynamicflow.TrafficFlowContext;
import com.kosta.rb.actor.metrics.dynamicflow.abs.UniqueDynamicFlowContext;
import com.kosta.rb.core.abs.FlowConnectorExecutor;
import com.kosta.rb.core.abs.OnSequenceFlowStatusHandle;

public class FlowConnector {

    private FlowServer flowServer;
    private Flow flow;
    private FlowConnectorExecutor flowConnectorExecutor;
    private boolean isVirtualCursorFreeze = false;
    private OnSequenceFlowStatusHandle onSequenceFlowStatusHandle;
    protected Board board;
    protected Store store;
    public FlowConnector(Flow flow, FlowConnectorExecutor flowConnectorExecutor){
        this.flow = flow;
        this.flowConnectorExecutor = flowConnectorExecutor;
        board = Board.getInstance();
        store = board.getStore();
        flowServer = new FlowServer(this);
        flowServer.start();
    }

    public void insertFlow(UniqueDynamicFlowContext flowCTX) {
        try {
            insertFlow(flowCTX.stateType(), flowCTX);
        }
        catch (Exception e){
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
            System.out.println(flowCTX);
        }
    }

    public FlowServer getFlowServer(){
        return flowServer;
    }

    private <T extends UniqueDynamicFlowContext> void insertFlow(String state, T data){
        /*
            버츄얼 커서가 맥스가 된 상태에서, 동적으로 값이 추가되면
            가상 버츄얼로 증가 및 감소가 동시에 발생하기에
            동적 생성중 가상 커서 행동 중지시킴
         */

        isVirtualCursorFreeze = true;
        onSequenceFlowStatusHandle = (rule, flowStatus)->{
            //System.out.println("--- OnFlowHandle");
            flowStatus.setUniqueDynamicFlow(data);
            //System.out.println(flowStatus);
            //System.out.println(flowStatus.getUniqueDynamicFlow());
        };

        flowConnectorExecutor.apply(state);
        flow.refreshSlider();
        isVirtualCursorFreeze = false;
        // flow.showStatusStore();
    }

    public OnSequenceFlowStatusHandle getOnSequenceFlowStatusHandle(){
        return onSequenceFlowStatusHandle;
    }

    public boolean isVirtualCursorFreeze(){
        return isVirtualCursorFreeze;
    }


    public void testDEF(){


        /*
        ComputerFlowContext clientCTX0 =
                ComputerFlowContext.builder()
                        .serverType("api-server")
                        .ip("172.10.3.0")
                        .port("8080")
                        .dirKey("api-server-a")
                        .gridX(2)
                        .gridY(0)
                        .build();
        insertFlow("RInsertComputer", clientCTX0);*/

        /*
        ComputerFlowContext clientCTX1 =
                ComputerFlowContext.builder()
                        .serverType("db-server")
                        .ip("172.10.3.0")
                        .port("8080")
                        .dirKey("client")
                        .gridX(2)
                        .gridY(0)
                        .build();
        insertFlow("RInsertComputer", clientCTX1);


        ComputerFlowContext clientCTX2 =
                ComputerFlowContext.builder()
                        .serverType("third-party-api-server")
                        .ip("172.10.3.0")
                        .port("8080")
                        .dirKey("client")
                        .gridX(3)
                        .gridY(0)
                        .build();
        insertFlow("RInsertComputer", clientCTX2);
        */

        /*
        ComputerFlowContext apiServerCTX =
                ComputerFlowContext.builder()
                        .serverType("api-server")
                        .ip("172.10.3.1")
                        .port("8081")
                        .dirKey("api-server")
                        .build();

        ComputerFlowContext otherApiServerCTX =
                ComputerFlowContext.builder()
                        .serverType("api-server")
                        .ip("172.10.3.1")
                        .port("8081")
                        .dirKey("other-api-server")
                        .build();

        ComputerFlowContext dbServerCTX =
                ComputerFlowContext.builder()
                        .serverType("db-server")
                        .ip("172.10.3.2")
                        .port("8081")
                        .dirKey("db-server")
                        .build();

        TrafficFlowContext trafficCTX0 =
                TrafficFlowContext.builder()
                        .startDir("client")
                        .endDir("api-server")
                        .json("HELLO")
                        .type("request")
                        .build();

        TrafficFlowContext trafficCTX1 =
                TrafficFlowContext.builder()
                        .startDir("api-server")
                        .endDir("other-api-server")
                        .json("HELLO RESPONSE")
                        .type("request")
                        .build();

        TrafficFlowContext trafficCTX2 =
                TrafficFlowContext.builder()
                        .startDir("other-api-server")
                        .endDir("api-server")
                        .json("HELLO REQUEST")
                        .type("response")
                        .build();

        TrafficFlowContext trafficCTX3 =
                TrafficFlowContext.builder()
                        .startDir("api-server")
                        .endDir("db-server")
                        .json("HELLO RESPONSE")
                        .type("request")
                        .build();

        TrafficFlowContext trafficCTX4 =
                TrafficFlowContext.builder()
                        .startDir("db-server")
                        .endDir("api-server")
                        .json("HELLO RESPONSE")
                        .type("response")
                        .build();

        TrafficFlowContext trafficCTX5 =
                TrafficFlowContext.builder()
                        .startDir("api-server")
                        .endDir("client")
                        .json("HELLO RESPONSE")
                        .type("response")
                        .build();

        insertFlow("RInsertComputer", clientCTX);
        insertFlow("RInsertComputer", apiServerCTX);
        insertFlow("RInsertComputer", otherApiServerCTX);
        insertFlow("RInsertComputer", dbServerCTX);

        insertFlow("RInsertTraffic", trafficCTX0);
        insertFlow("RInsertTraffic", trafficCTX1);
        insertFlow("RInsertTraffic", trafficCTX2);
        insertFlow("RInsertTraffic", trafficCTX3);
        insertFlow("RInsertTraffic", trafficCTX4);
        insertFlow("RInsertTraffic", trafficCTX5);
        */
    }
}
