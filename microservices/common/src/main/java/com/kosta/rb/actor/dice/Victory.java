package com.kosta.rb.actor.dice;

import com.kosta.rb.comp.Graphic;
import com.kosta.rb.comp.Position;
import com.kosta.rb.core.abs.Actor;
import com.kosta.rb.def.BoardConfig;

import java.awt.*;

public class Victory extends Actor {

    protected Graphic pngComp;
    protected Graphic stringComp;

    public Victory(){
        attachComp(Position.class);
        pngComp = attachComp(Graphic.class);
        pngComp.setLayer(BoardConfig.ELayer.FIGURE_FRONT)
                .setImage("victory.png")
                .setSize(150, 150)
                .setPivotXY(0, 0)
                .setVisible(false);

        stringComp = attachComp(Graphic.class);
        stringComp.setDrawText(" ", new Font("궁서", Font.BOLD, 22))
                .setPivotXY(-50,150)
                .setVisible(false);
    }

    public void visible(boolean value){
        pngComp.setVisible(value);
        stringComp.setVisible(value);
    }

    public void toText(int index, int goal, int ipc){
        String text = "승리 : "
                + (index + 1) + " Player"
                + "\n목표 : "
                + goal
                + "\n처리 : "
                +ipc;

        stringComp.setDrawText(text);
    }
}
