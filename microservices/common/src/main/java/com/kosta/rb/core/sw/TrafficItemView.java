package com.kosta.rb.core.sw;

import com.kosta.rb.actor.metrics.dynamicflow.PtrComputer;
import com.kosta.rb.actor.metrics.dynamicflow.TrafficFlowContext;
import com.kosta.rb.core.Board;
import com.kosta.rb.def.abs.Config;
import com.kosta.rb.def.abs.WindowContext;
import javax.swing.*;

public class TrafficItemView extends JScrollPane {

    protected Board board;
    protected Config bc;

    private JTextArea textArea;

    public TrafficItemView(int index, JTextArea textArea){
        super(textArea);
        this.textArea = textArea;

        board = Board.getInstance();
        bc = board.getConfig();
        WindowContext wc = bc.getWindowContext();

        int width = 250;
        int height = 225;

        int step = 35;
        int x = ( step + (index * width) + (index * step) );
        int y = wc.getWindowSizeHeight() - (150 + height);

        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        setBounds(x, y, width, height);
    }
    public void setText(){
        textArea.setText("");
    }
    public void setText(TrafficFlowContext ctx){
        StringBuilder strb = new StringBuilder();
        PtrComputer start = ctx.getStartPtrComputer();
        PtrComputer end = ctx.getEndPtrComputer();
        strb
                // .append("Index : ").append(activeTrafficIndex)
                .append("\n Date : ").append(ctx.getDate())
                .append("\n Reqs : ").append(ctx.getReqs())
                .append("\n Start : ").append(start.getIp()).append(":").append(start.getPort())
                .append("\n Type : ").append(start.getType()).append(":").append(start.getLabel())
                .append("\n : ")
                .append("\n\t ").append(ctx.getUri())
                .append("\n\t ").append(ctx.getMethod())
                .append("\n\t ").append(ctx.getBody())
                .append("\n End : ").append(end.getIp()).append(":").append(end.getPort())
                .append("\n Type : ").append(end.getType()).append(":").append(end.getLabel());
        textArea.setText(strb.toString());
    }
}
