package com.kosta.rb.core.sw;

import com.kosta.rb.core.Board;
import com.kosta.rb.def.BoardConfig;
import com.kosta.rb.def.abs.Config;
import com.kosta.rb.def.abs.WindowContext;
import com.kosta.rb.rule.def.FlowStatus;

import javax.swing.*;
import java.awt.*;

public class ScrollBox extends JScrollPane {

    private JTextArea textArea;
    protected Board board;
    protected Config bc;

    public ScrollBox(JTextArea textArea){
        super(textArea);
        this.textArea = textArea;

        board = Board.getInstance();
        bc = board.getConfig();
        WindowContext wc = bc.getWindowContext();

        int width = (wc.getWindowSizeWidth() / 4);
        int x = (int)(wc.getWindowSizeWidth() / 1.2) - 200;
        int y = 25;
        int height = 300;
        setBounds(x, y, width, height);
    }

    public void appendText(FlowStatus status, String text){
        StringBuilder strb = new StringBuilder();
        strb.append(" :: ")
                .append(status.toString())
                .append("\n LOG Text : ")
                .append(text)
                .append("\n\n");

        textArea.append(strb.toString());

        JScrollBar vertical = getVerticalScrollBar();
        vertical.setValue( vertical.getMaximum() );
    }
}
