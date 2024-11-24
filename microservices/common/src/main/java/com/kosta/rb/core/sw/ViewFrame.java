package com.kosta.rb.core.sw;

import com.kosta.rb.actor.metrics.Traffic;
import com.kosta.rb.actor.metrics.dynamicflow.TrafficFlowContext;
import com.kosta.rb.core.Board;
import com.kosta.rb.core.Mode;
import com.kosta.rb.def.BoardConfig;
import com.kosta.rb.def.MetricsConfig;
import com.kosta.rb.def.abs.Config;
import com.kosta.rb.def.abs.WindowContext;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ViewFrame extends JFrame {

    private Renderer renderer;
    private Slider slider;
    private ScrollBox scrollBox;
    private Mode mode;

    private final static int TRAFFIC_ITEM_VIEW_SIZE = 5;
    private TrafficItemView[] trafficItemView = new TrafficItemView[TRAFFIC_ITEM_VIEW_SIZE];

    public ViewFrame(Mode mode){
        this.mode = mode;
        renderer = new Renderer(this);
        slider = new Slider();
        scrollBox = new ScrollBox(new JTextArea(20, 20));
        inItJFrame();
    }

    public Renderer getRenderer(){
        return renderer;
    }

    public Slider getSlider(){
        return slider;
    }

    public ScrollBox getScrollBox(){
        return scrollBox;
    }

    public void reDraw(){
        if(renderer.getIsRender())
            repaint();
    }

    public void addSwingComp(Config bc){
        if(bc instanceof MetricsConfig){
            spawnTrafficVies();
        }
        if(mode.isHost()){
            add(slider);
            add(scrollBox);
        }
        add(renderer);
    }

    public void updateTrafficItemView(int activeTrafficIndex, List<Traffic> traffic){
        try {
            for (int i = 0; i < TRAFFIC_ITEM_VIEW_SIZE; ++i) {
                trafficItemView[i].setText();

                int curIndex = i + activeTrafficIndex;
                if (isValidIndex(traffic, curIndex)) {
                    Traffic t = traffic.get(curIndex);
                    TrafficFlowContext ctx = t.getCTX();
                    trafficItemView[i].setText(ctx);
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }

    }

    private boolean isValidIndex(List<Traffic> list, int index){
        return ( index >= 0 &&  index < list.size() );
    }

    private void spawnTrafficVies(){
        for(int i = 0; i < TRAFFIC_ITEM_VIEW_SIZE; ++i){
            trafficItemView[i] = new TrafficItemView(i, new JTextArea(20, 20));
            add(trafficItemView[i]);
        }
    }

    private void inItJFrame(){
        Board board = Board.getInstance();
        Config bc = board.getConfig();
        WindowContext wc = bc.getWindowContext();
        String title = wc.getWindowTitle() + " : " + board.getMode().getOwner();

        int width = wc.getWindowSizeWidth();
        int height = wc.getWindowSizeHeight();
        if(!mode.isHost()){
            width = (int)(wc.getWindowSizeWidth() * 0.45);
            height = (int)(wc.getWindowSizeHeight() * 0.82);
        }

        setTitle(title);
        setSize(width, height);
        setLocation(wc.getWindowStartPointX(), wc.getWindowStartPointY());
        setLayout(null);
        addSwingComp(bc);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

}
