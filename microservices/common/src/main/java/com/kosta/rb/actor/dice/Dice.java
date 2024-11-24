package com.kosta.rb.actor.dice;

import com.kosta.rb.comp.Graphic;
import com.kosta.rb.comp.Position;
import com.kosta.rb.core.abs.Actor;
import com.kosta.rb.def.BoardConfig;
import java.awt.*;

public class Dice extends Actor {

    protected Graphic gp;
    protected Graphic stringComp;

    public Dice(){
        attachComp(Position.class);
        gp = attachComp(Graphic.class);
        gp.setLayer(BoardConfig.ELayer.FIGURE_FRONT)
                .setImage("dice.gif")
                .setSize(180, 180)
                .setPivotXY(0, 0)
                .setVisible(false);

        stringComp = attachComp(Graphic.class);
        stringComp.setDrawText(" ", new Font("궁서", Font.BOLD, 40))
                .setPivotXY(80,135)
                .setVisible(false);
    }

    public Dice visible(boolean value){
        gp.setVisible(value);
        stringComp.setVisible(value);
        return this;
    }

    public void toText(int index){
        String text = index + "";

        stringComp.setDrawText(text);
    }
}
