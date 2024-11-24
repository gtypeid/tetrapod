package com.kosta.rb.actor.dice;

import com.kosta.rb.comp.Graphic;
import com.kosta.rb.comp.Position;
import com.kosta.rb.core.abs.Actor;
import com.kosta.rb.def.BoardConfig;

public class Arrow extends Actor {

    protected Graphic gp;
    public Arrow(){
        attachComp(Position.class);
        gp = attachComp(Graphic.class);
        gp.setLayer(BoardConfig.ELayer.FLOOR_FRONT)
                .setImage("arrow.png")
                .setSize(55, 55)
                .setPivotXY(-10, 45);

    }
}
