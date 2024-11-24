package com.kosta.rb.rule.abs;

import com.kosta.rb.core.*;
import com.kosta.rb.core.sw.ScrollBox;
import com.kosta.rb.core.sw.ViewFrame;
import com.kosta.rb.def.BoardConfig;
import com.kosta.rb.core.abs.Entity;
import com.kosta.rb.def.abs.Config;
import com.kosta.rb.rule.def.FlowStatus;
import com.kosta.rb.rule.def.RuleProperty;
//import com.kosta.winter.core.Http;

public abstract class Rule extends Entity implements RuleRun{

    protected RuleProperty ruleProperty;
    protected Board board;
    protected Config bc;
    protected Store store;
    protected ViewFrame vf;
    protected ScrollBox sb;
    protected Flow flow;
    //protected Http http;
    protected Mode mode;

    public Rule(){
        board = Board.getInstance();
        bc = board.getConfig();
        store = board.getStore();
        vf = board.getViewFrame();
        sb = vf.getScrollBox();
        flow = board.getFlow();
        //http = board.getHttp();
        mode = board.getMode();
    }

    public void casing(){

    }

    public RuleProperty getRuleProperty(){
        if(ruleProperty == null){
            ruleProperty = ruleProperty();
        }
        return ruleProperty;
    }

    public FlowStatus getCurFlow(){
        board = Board.getInstance();
        flow = board.getFlow();
        return flow.getCurFlow();
    }

    public FlowStatus getCalcFlow(int calc){
        board = Board.getInstance();
        flow = board.getFlow();
        return flow.getCalcFlow(calc);
    }

    protected abstract RuleProperty ruleProperty();
}
