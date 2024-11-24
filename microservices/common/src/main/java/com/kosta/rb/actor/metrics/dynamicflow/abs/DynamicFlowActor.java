package com.kosta.rb.actor.metrics.dynamicflow.abs;

import com.kosta.rb.core.abs.Actor;

public abstract class DynamicFlowActor extends Actor {
    private UniqueDynamicFlowContext ctx;
    public void dynamicFlowBind(UniqueDynamicFlowContext ctx){
        this.ctx = ctx;
    }

    public <T extends UniqueDynamicFlowContext > T getCTX(){
        return (T) ctx;
    }
    abstract public void visible(boolean value);
}
