package com.kosta.rb.actor.metrics;

import com.kosta.rb.actor.metrics.dynamicflow.ComputerFlowContext;
import com.kosta.rb.actor.metrics.dynamicflow.TrafficFlowContext;
import com.kosta.rb.actor.metrics.dynamicflow.abs.DynamicFlowActor;
import com.kosta.rb.actor.metrics.dynamicflow.abs.UniqueDynamicFlowContext;
import com.kosta.rb.comp.Position;
import com.kosta.rb.core.Board;
import com.kosta.rb.core.Store;
import com.kosta.rb.core.abs.Actor;
import com.kosta.rb.core.sw.ViewFrame;
import com.kosta.rb.rule.def.FlowStatus;
import org.springframework.data.relational.core.sql.In;

import java.util.*;

public class ComputerGrid extends Actor {

    Map< Class<? extends DynamicFlowActor>, Map<Integer, ? extends DynamicFlowActor>>
            childStore = new HashMap<>();

    private int activeTrafficIndex = -1;
    private List<Traffic> trafficList = new ArrayList<>();

    protected Store store;
    protected ViewFrame vf;

    public ComputerGrid(){
        super();
        store = Board.getInstance().getStore();
        vf = Board.getInstance().getViewFrame();
    }

    public <T extends DynamicFlowActor> Map<Integer, T> getStore(Class<T> type) {
        return (Map<Integer, T>) childStore.get(type);
    }

    public <T extends DynamicFlowActor> T getChild(int seq, Class<T> type){
        Map<Integer, T> inStore = getStore(type);
        return inStore.get(seq);
    }

    public Computer spawnComputer(FlowStatus flowStatus){
        int seq = flowStatus.getSequence();
        ComputerFlowContext ctx = flowStatus.getUniqueDynamicFlow();
        casingSpawnChild(seq, ctx, Computer.class);

        Computer dynamicActor = getChild(seq, Computer.class);
        Position ps = dynamicActor.getPosition();
        ps.setPosition( getSpawnPosition(ctx) );
        dynamicActor.visible(true);
        return dynamicActor;
    }

    public Traffic spawnTraffic(FlowStatus flowStatus){
        int seq = flowStatus.getSequence();
        TrafficFlowContext ctx = flowStatus.getUniqueDynamicFlow();
        if ( !casingSpawnChild(seq, ctx, Traffic.class) ){
            Traffic spawnTraffic = getChild(seq, Traffic.class);
            spawnTraffic.setTrafficIndex( trafficList.size() );
            trafficList.add(spawnTraffic);
        }

        Traffic dynamicActor = getChild(seq, Traffic.class);
        dynamicActor.setPtrComputer( getStore(Computer.class) );
        dynamicActor.visible(true);
        activeTrafficIndex = dynamicActor.getTrafficIndex();

        return dynamicActor;
    }

    public void backTrafficItem(){
        activeTrafficIndex--;
    }

    public Traffic findTrafficByIndex(int index){
        return trafficList.get(index);
    }


    public void updateTrafficItemView(){
        vf.updateTrafficItemView(activeTrafficIndex, trafficList);
    }

    public <T extends DynamicFlowActor> T visibleChild(FlowStatus flowStatus, boolean value, Class<T> type){
        int seq = flowStatus.getSequence();
        T child = getChild(seq, type);
        child.visible(value);
        return child;
    }


    private <T extends DynamicFlowActor> boolean casingSpawnChild(int seq, UniqueDynamicFlowContext ctx, Class<T> type){
        boolean hasChildMap = childStore.containsKey(type);
        if(!hasChildMap){
            childStore.put(type, new HashMap<>() );
        }
        Map<Integer, T> inStore = getStore(type);
        boolean hasKey = inStore.containsKey(seq);
        if(!hasKey){
            T dynamicActor = store.dynamicFlowSpawnActor(type, ctx);
            inStore.put(seq, dynamicActor);
            return false;
        }

        return true;
    }

    private int[] getSpawnPosition(ComputerFlowContext ctx){
        int startX = 5;
        int startY = 5;
        int stepX = 8;
        int stepY = 10;
        int calcX = (startX) + (ctx.getGridX() * stepX);
        int calcY = (startY) + (ctx.getGridY() * stepY);
        return new int[]{
                calcX, calcY
        };
    }

    /*


    public void chainConnect(FlowStatus flowStatus){

    }

    public Traffic chainTraffic(FlowStatus flowStatus){
        int seq = flowStatus.getSequence();
        TrafficFlowContext ctx = flowStatus.getUniqueDynamicFlow();
        boolean hasKey = trafficStore.containsKey(seq);
        if(!hasKey){
            Traffic dynamicActor = store.dynamicFlowSpawnActor(Traffic.class, ctx);
            trafficStore.put(seq, dynamicActor);
        }

        Traffic dynamicActor = trafficStore.get(seq);
        Position ps = dynamicActor.getPosition();

        Computer sc = findComputerByDirKey(ctx.getStartDir());
        Computer ec = findComputerByDirKey(ctx.getEndDir());

        dynamicActor.setDirPosition(sc, ec);
        visibleTraffic(flowStatus, true);
        return dynamicActor;
    }

    public void visibleTraffic(FlowStatus flowStatus, boolean value){
        int seq = flowStatus.getSequence();
        Traffic traffic = trafficStore.get(seq);
        traffic.visible(value);
    }

    public void visibleChainConnect(FlowStatus flowStatus, boolean value){
        int seq = flowStatus.getSequence();
        Traffic traffic = trafficStore.get(seq);
        traffic.visible(value);
    }

    public Computer findComputerByDirKey(String key){
        return computerStore.values().stream().filter( computer->{
            ComputerFlowContext ctx = computer.getCTX();
            return ctx.getDirKey().equals(key);
        }).findAny().get();
    }

    */

}
