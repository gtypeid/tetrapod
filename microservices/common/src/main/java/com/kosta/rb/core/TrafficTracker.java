package com.kosta.rb.core;

import com.kosta.rb.actor.metrics.dynamicflow.PtrComputer;
import com.kosta.rb.actor.metrics.dynamicflow.TrafficFlowContext;
import com.kosta.rb.core.abs.Comp;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class TrafficTracker {

    private FlowConnector flowConnector;
    private TrafficFlowContext inFirstClientTraffic;
    private Queue<TrafficFlowContext> queue = new ConcurrentLinkedQueue<>();

    public TrafficTracker(FlowConnector flowConnector){
        this.flowConnector = flowConnector;
    }

    public void enqueue(TrafficFlowContext ctx){
        flowConnector.insertFlow(ctx);
        return;

        // 짝수같은걸로 비교해보거나 커스텀 헤더 경유자 라던지 같은걸로 비교해보자
        // 체인 커넥터 발송일때 뭔가 체이닝 마다 토탈 리퀘, 리스폰스 요청 카운트를 n * 2만큼 늘려도 되지 않을까 생각했지만
        // 체인이 걸려있지 않은, 기본 컨트롤러 로직의 requet도 있을 수 있다 그러니 뭔가 적합하지 않음

        /*
        if(inFirstClientTraffic == null){
            inFirstClientTraffic = ctx;
            queue.add(inFirstClientTraffic);

            System.out.println(":: FIRST CLIENT TRAFFIC ");
            System.out.println(inFirstClientTraffic);
            return;
        }

        System.out.println("- INSERT QUEUE");
        System.out.println(ctx);

        queue.add(ctx);
        PtrComputer client = inFirstClientTraffic.getStartPtrComputer();
        PtrComputer responseClient = ctx.getEndPtrComputer();

        if( client.getLabel().equals(responseClient.getLabel())){

            System.out.println("- POLL QUEUE");
            Queue<TrafficFlowContext> sQueue = queue.stream()
                    .sorted(Comparator.comparing(it -> {
                        return TrafficFlowContext.msDuration( ((TrafficFlowContext)it).getDate() );
                    }).reversed())
                    .collect(Collectors.toCollection(LinkedList::new));

            while (!sQueue.isEmpty()) {
                TrafficFlowContext item = sQueue.poll();
                System.out.println(item);
                flowConnector.insertFlow(item);
            }

            inFirstClientTraffic = null;
            queue.clear();
        }

         */
    }

}
