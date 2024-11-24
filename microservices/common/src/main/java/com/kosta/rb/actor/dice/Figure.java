package com.kosta.rb.actor.dice;

import com.kosta.rb.def.BoardConfig;
import com.kosta.rb.comp.Graphic;
import com.kosta.rb.comp.Position;
import com.kosta.rb.core.abs.Actor;

public class Figure extends Actor {

    private int localIndex;
    protected Graphic gp;
    public Figure(){
        attachComp( Position.class );
        gp = attachComp( Graphic.class );
        gp.setLayer(BoardConfig.ELayer.FIGURE);
    }

    public void changeColor(int index){
        localIndex = index;
        String[] colors = { "figure-red.png", "figure-blue.png", "figure-green.png", "figure-yellow.png"};
        gp.setImage(colors[index])
                .setSize(50, 50)
                .setPivotXY(-9, -9);
    }

    @Override
    public String toString() {
        return "Figure Index : " + localIndex + " [ position : " + getComp(Position.class) + "] ";
    }
}
