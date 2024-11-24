package com.kosta.rb.core;

import com.kosta.rb.core.sw.Renderer;
import com.kosta.rb.core.sw.ViewFrame;
import com.kosta.rb.def.BoardConfig;
import com.kosta.rb.def.abs.Config;
//import com.kosta.winter.core.Http;

public class Board {

    private static Board instance;
    public static Board getInstance(){
        if(instance == null)
            instance = new Board();
        return instance;
    }

    private Config config;
    private Mode mode;
    private ViewFrame viewFrame;
    private Store store;
    private Flow flow;
    //private Http http;

    private Board() { }
    public void run(String type, Config config){
        this.config = config;
        mode = new Mode(type);
        store = new Store();
        viewFrame = new ViewFrame(mode);
        //http = new Http();
        flow = new Flow();
        flow.flowStart();
        viewFrame.getRenderer()
                .setIsRender(true);

        mode.startClientListening();
        flow.getFlowConnector().getFlowServer().onRun();
    }

    public Config getConfig(){
        return config;
    }

    public Store getStore(){
        return store;
    }

    public ViewFrame getViewFrame(){
        return viewFrame;
    }

    public Renderer getRenderer(){
        return viewFrame.getRenderer();
    }

    public Flow getFlow(){
        return flow;
    }

    public FlowConnector getFlowConnector(){
        return flow.getFlowConnector();
    }

    //public Http getHttp(){ return http; }

    public Mode getMode() {
        return mode;
    }

}
