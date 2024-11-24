package com.kosta.rb.core.sw;

import com.kosta.rb.core.Board;
import com.kosta.rb.core.Flow;
import com.kosta.rb.def.BoardConfig;
import com.kosta.rb.def.abs.Config;
import com.kosta.rb.def.abs.WindowContext;

import javax.swing.*;
import java.awt.*;

public class Slider extends JSlider {

    protected Board board;
    protected Config bc;
    protected Flow flow;
    public Slider(){
        super(HORIZONTAL, 0, 1, 1);
        setPaintTicks(true);
        setMajorTickSpacing(10);

        board = Board.getInstance();
        bc = board.getConfig();
        flow = board.getFlow();
        WindowContext wc = bc.getWindowContext();


        int x = 100;
        int y = wc.getWindowSizeHeight() - (125);
        int width = (int) ( wc.getWindowSizeWidth() - (x * 2) );
        int height = 50;

        setBounds(x, y, width, height);
        setBackground(new Color(222, 222, 222));
        addChangeListener((e)->{

        });
    }


    public void setSliderLength(int length){
        setMaximum(length);
        setValue(length);
    }


}
