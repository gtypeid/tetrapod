package com.kosta.rb.actor.metrics;

import com.kosta.rb.actor.metrics.dynamicflow.ComputerFlowContext;
import com.kosta.rb.actor.metrics.dynamicflow.abs.DynamicFlowActor;
import com.kosta.rb.comp.Graphic;
import com.kosta.rb.comp.Position;
import com.kosta.rb.def.BoardConfig;
import com.kosta.rb.def.abs.Config;

import java.awt.*;

public class Computer extends DynamicFlowActor {

    protected Graphic gp;
    protected Graphic stringComp;
    protected Position ps;

    private int sizeX = 100;
    private int sizeY = 100;

    public Computer(){
        ps = attachComp(Position.class);
        gp = attachComp(Graphic.class);
        gp.setLayer(BoardConfig.ELayer.FIGURE)
                .setImage("client0.png")
                .setSize(sizeX, sizeY)
                .setPivotXY( -1 * (sizeX / 2 ), -1 * (sizeY / 2));

        stringComp = attachComp(Graphic.class);
        stringComp.setDrawText(" ", new Font("돋움", Font.BOLD, 18))
                .setPivotXY( -1 * ( sizeX / 2) , ( (int)(sizeY * 0.5) ))
                .setVisible(false);
    }

    @Override
    public void visible(boolean value){
        gp.setVisible(value);
        ComputerFlowContext ctx = getCTX();
        stringComp.setDrawText( toText(ctx) );
        stringComp.setVisible(value);

        if(value){
            changeImg(ctx.getServerType(), ctx.getDirKey());
        }
    }

    public String toText(ComputerFlowContext ctx){
        String text =
                "Type : " + ctx.getServerType()
                + "\nLabel : " + ctx.getDirKey()
                + "\nIp : " + ctx.getIp()
                + "\nPort :" + ctx.getPort();
        return text;
    }

    public void changeImg(String serverType, String serverLabel){
        if(serverType.equals("client")){

            if(serverLabel.equals("client-postman")){
                gp.setLayer(BoardConfig.ELayer.FIGURE)
                        .setImage("client1.png")
                        .setSize(sizeX, sizeY)
                        .setPivotXY( -1 * (sizeX / 2 ), -1 * (sizeY / 2));
            }
            if(serverLabel.equals("client-thomas")){
                gp.setLayer(BoardConfig.ELayer.FIGURE)
                        .setImage("client2.png")
                        .setSize(sizeX, sizeY)
                        .setPivotXY( -1 * (sizeX / 2 ), -1 * (sizeY / 2));
            }
        }
        else if(serverType.equals("api-server")){
            gp.setLayer(BoardConfig.ELayer.FIGURE)
                    .setImage("server0.png")
                    .setSize(sizeX, sizeY)
                    .setPivotXY( -1 * (sizeX / 2 ), -1 * (sizeY / 2));
        }
        else if(serverType.equals("db-server")){
            gp.setLayer(BoardConfig.ELayer.FIGURE)
                    .setImage("server1.png")
                    .setSize(sizeX, sizeY)
                    .setPivotXY( -1 * (sizeX / 2 ), -1 * (sizeY / 2));
        }
        else if(serverType.equals("third-party-api-server")){
            gp.setLayer(BoardConfig.ELayer.FIGURE)
                    .setImage("server2.png")
                    .setSize(sizeX, sizeY)
                    .setPivotXY( -1 * (sizeX / 2 ), -1 * (sizeY / 2));
        }
        /*
        if(type == 2){
            gp.setImage("floors.png")
                    .setSize(35, 35);
        }
        */
    }

}
