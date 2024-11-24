package com.kosta.rb.actor.dice;

import com.kosta.rb.comp.Graphic;
import com.kosta.rb.comp.Position;
import com.kosta.rb.def.BoardConfig;
import com.kosta.rb.core.abs.Actor;

public class Paper extends Actor {

    public Paper() {
        attachComp(Position.class);
        attachComp(Graphic.class)
                .setLayer(BoardConfig.ELayer.BACK)
                .setImage("paper.png")
                .setSize(400, 520);
    }
}
