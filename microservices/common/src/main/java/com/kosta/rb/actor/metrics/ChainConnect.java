package com.kosta.rb.actor.metrics;

import com.kosta.rb.actor.metrics.dynamicflow.ComputerFlowContext;
import com.kosta.rb.actor.metrics.dynamicflow.abs.DynamicFlowActor;
import com.kosta.rb.comp.Graphic;
import com.kosta.rb.comp.Position;
import com.kosta.rb.def.BoardConfig;

import java.awt.*;

public class ChainConnect extends DynamicFlowActor {


    protected Graphic gp;
    protected Graphic stringComp;
    protected Position ps;

    public ChainConnect(){
        ps = attachComp(Position.class);
        gp = attachComp(Graphic.class);
        gp.setLayer(BoardConfig.ELayer.FIGURE)
                .setImage("server0.png")
                .setSize(125, 125)
                .setPivotXY(0, 0);

        stringComp = attachComp(Graphic.class);
        stringComp.setDrawText(" ", new Font("돋움", Font.BOLD, 18))
                .setPivotXY(0,125)
                .setVisible(false);
    }

    public void visible(boolean value){
        gp.setVisible(value);
        ComputerFlowContext ctx = getCTX();
        stringComp.setDrawText( toText(ctx) );
        stringComp.setVisible(value);

    }

    public String toText(ComputerFlowContext ctx){
        String text = ctx.getServerType()
                + "\n" + ctx.getIp()
                + "\n: " + ctx.getPort();
        return text;
    }

}
