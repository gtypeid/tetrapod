package com.kosta.rb.comp;

import com.kosta.rb.core.Board;
import com.kosta.rb.core.abs.Comp;
import com.kosta.rb.core.sw.ViewFrame;
import com.kosta.rb.def.BoardConfig;
import com.kosta.rb.core.abs.Actor;
import com.kosta.rb.def.Util;

import javax.swing.*;
import java.awt.*;

public class Graphic extends Comp {

    private boolean isVisible = true;
    private int pivotX;
    private int pivotY;

    private int width;
    private int height;
    private ImageIcon image;
    private String drawText;
    private Font drawFont;

    public Graphic(){
    }

    public Graphic setLayer(BoardConfig.ELayer layer){
        Actor parent = getParent();
        parent.setActorLayer( layer.getValue() );
        return this;
    }

    public Graphic setImage(String src){
        image = new ImageIcon( Util.getResourcePath(src) );
        width = image.getIconWidth();
        height = image.getIconHeight();
        pivotX = 0;
        pivotY = 0;
        return this;
    }

    public Graphic setSize(int width, int height){
        this.width = width;
        this.height = height;
        return this;
    }

    public Graphic setSize(int[] size){
        return setSize(size[0], size[1]);
    }

    public Graphic setPivotX(int pivotX) {
        this.pivotX = pivotX;
        return this;
    }

    public Graphic setPivotY(int pivotY) {
        this.pivotY = pivotY;
        return this;
    }

    public Graphic setPivotXY(int pivotX, int pivotY) {
        this.pivotX = pivotX;
        this.pivotY = pivotY;
        return this;
    }

    public Graphic setPivotXY(int[] pivotXY) {
        this.pivotX = pivotXY[0];
        this.pivotY = pivotXY[1];
        return this;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public int[] getWidthHeight(){
        return new int[]{ width, height };
    }

    public int getPivotX(){
        return pivotX;
    }

    public int getPivotY(){
        return pivotY;
    }

    public int[] getPivotXY(){
        return new int[]{ pivotX, pivotY };
    }

    public int getLayer(){
        return getParent().getActorLayer();
    }

    public Image getImage(){
        if(image != null){
            return image.getImage();
        }
        return null;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public Graphic setVisible(boolean visible) {
        isVisible = visible;
        Board bd = Board.getInstance();
        ViewFrame vf = bd.getViewFrame();
        vf.reDraw();
        return this;
    }

    public Graphic setDrawText(String text){
        drawText = text;
        return this;
    }

    public Graphic setDrawText(String text, Font font){
        drawText = text;
        drawFont = font;
        return this;
    }

    public String getDrawText(){
        return drawText;
    }

    public Font getFont(){
        return drawFont;
    }

}
