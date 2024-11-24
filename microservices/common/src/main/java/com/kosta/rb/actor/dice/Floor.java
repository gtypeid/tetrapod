package com.kosta.rb.actor.dice;
import com.kosta.rb.def.BoardConfig;
import com.kosta.rb.comp.Position;
import com.kosta.rb.comp.Graphic;
import com.kosta.rb.core.abs.Actor;

public class Floor extends Actor {

    private int linkIndex = -1;

    protected Graphic gp;
    public Floor(){
        attachComp( Position.class );
        gp = attachComp( Graphic.class );
        gp.setLayer(BoardConfig.ELayer.FLOOR)
                .setImage("floor.png")
                .setSize(35, 35);
    }

    public void changeFloor(byte type){
        if(type == 2){
            gp.setImage("floors.png")
                    .setSize(35, 35);
        }
    }

    public void setLinkIndex(int index){
        linkIndex = index;
    }

    public int getLinkIndex(){
        return linkIndex;
    }

    @Override
    public String toString() {
        return getClass().getName() + "@"
                + Integer.toHexString(hashCode())
                + " .. uid: " + getUid() + " pi: " + getPi()
                + " .. position: " + getPosition().getX() + ", " + getPosition().getY()
                + " .. linkIndex " + linkIndex;
    }
}
