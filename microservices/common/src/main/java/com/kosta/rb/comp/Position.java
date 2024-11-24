package com.kosta.rb.comp;

import com.kosta.rb.core.abs.Actor;
import com.kosta.rb.core.abs.Comp;
import com.kosta.rb.core.Board;
import com.kosta.rb.core.sw.ViewFrame;

public class Position extends Comp {

    private Position attachPosParent;
    private Position attachPosChild;

    private int x;
    private int y;

    public Position(){
        x = 0;
        y = 0;
    }

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int[] getXY(){
        return new int[]{x,y};
    }

    public int moveX(int x){
        moveSetXY(x, 0);
        return this.x;
    }

    public int moveY(int y){
        moveSetXY(0, y);
        return this.y;
    }

    public int[] moveXY(int x, int y){
        moveSetXY(x, y);
        return new int[]{ this.x, this.y };
    }

    public int[] moveXY(int[] xy){
        return moveXY(xy[0], xy[1]);
    }

    public void setPosition(int[] xy){
        setPosition(xy[0], xy[1]);
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
        reDrawCall();
    }

    public void setAttachPosParent(Actor parentActor){
        Position pos = getParent().getPosition();
        Position targetPos = parentActor.getComp(Position.class);
        if(pos != null && targetPos != null){
            attachPosParent = targetPos;
            attachPosParent.bindAttachChild(this);
        }
    }

    private void bindAttachChild(Position child){
        attachPosChild = child;
        reDrawCall();
    }

    private void moveSetXY(int x, int y){
        this.x += x;
        this.y += y;
        reDrawCall();
    }

    private void reDrawCall(){
        Board bd = Board.getInstance();
        ViewFrame vf = bd.getViewFrame();
        rePositionAttachActor();
        vf.reDraw();
    }

    public void rePositionAttachActor(){
        if(attachPosChild != null){
            attachPosChild.setPosition(x, y);
        }
    }

    @Override
    public String toString() {
        return "comp : " + "[ x : " + x + " ] [ y : " + y + " ]";
    }
}
