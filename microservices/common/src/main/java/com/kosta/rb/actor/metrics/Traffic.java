package com.kosta.rb.actor.metrics;

import com.kosta.rb.actor.metrics.dynamicflow.ComputerFlowContext;
import com.kosta.rb.actor.metrics.dynamicflow.PtrComputer;
import com.kosta.rb.actor.metrics.dynamicflow.TrafficFlowContext;
import com.kosta.rb.actor.metrics.dynamicflow.abs.DynamicFlowActor;
import com.kosta.rb.comp.Graphic;
import com.kosta.rb.comp.Position;
import com.kosta.rb.def.BoardConfig;
import com.kosta.rb.def.Util;
import com.kosta.rb.def.abs.Config;

import java.awt.*;
import java.util.Map;

public class Traffic extends DynamicFlowActor {


    protected Graphic gp;
    protected Graphic stringComp;
    protected Position ps;

    private Computer startDirComputer;
    private Computer endDirComputer;

    private int trafficIndex = -1;

    private int sizeX = 55;
    private int sizeY = 55;

    public Traffic(){
        ps = attachComp(Position.class);
        /*
        gp = attachComp(Graphic.class);
        gp.setLayer(BoardConfig.ELayer.FLOOR_FRONT)
                .setImage("arrow.png")
                .setSize(0, 0)
                .setPivotXY(0, 0);*/

        stringComp = attachComp(Graphic.class);
        stringComp.setLayer(Config.ELayer.FLOOR_FRONT)
                .setDrawText(" ", new Font("궁서", Font.BOLD, 8))
                .setPivotXY(0,15)
                .setVisible(false);
    }

    public void setTrafficIndex(int index){
        trafficIndex = index;
        String s = String.valueOf(trafficIndex);
        stringComp.setDrawText( s );
    }

    public int getTrafficIndex(){
        return trafficIndex;
    }

    @Override
    public void visible(boolean value){
        //gp.setVisible(value);
        TrafficFlowContext ctx = getCTX();
        stringComp.setVisible(value);

    }

    public void setPtrComputer(Map<Integer, Computer> computerStore){
        TrafficFlowContext ctx = getCTX();
        Computer startComputer = getMatchPtrComputer(computerStore, ctx.getStartPtrComputer());
        Computer endComputer = getMatchPtrComputer(computerStore, ctx.getEndPtrComputer());
        setDirPosition(startComputer, endComputer);
    }

    private Computer getMatchPtrComputer(Map<Integer, Computer> computerStore, PtrComputer ptrComputer){
        return computerStore.values().stream()
                .filter( computer -> {
            return ((ComputerFlowContext)computer.getCTX())
                    .getDirKey()
                    .equals(ptrComputer.getLabel());
        }).findAny()
                .get();
    }

    private void setDirPosition(Computer sc, Computer ec) {
        Position scps = sc.getPosition();
        Position ecps = ec.getPosition();

        TrafficFlowContext ctx = this.getCTX();
        int endY = ecps.getY();


        int calc = Util.getRandomInt(1,4);
        float half = Util.getRandomFloat(1.5f, 2.5f);
        int calcX = (int) Math.abs( ( ecps.getX() + scps.getX()) / half );
        int calcY = (ctx.getReqs().equals("request")) ? (endY - calc) : (endY + calc);

        getPosition().setPosition(calcX, calcY);
        startDirComputer = sc;
        endDirComputer = ec;
    }

    public Computer getStartDirComputer(){
        return startDirComputer;
    }

    public Computer getEndDirComputer(){
        return endDirComputer;
    }


}
