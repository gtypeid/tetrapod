package com.kosta.rb.actor.metrics.dynamicflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kosta.rb.actor.metrics.dynamicflow.abs.UniqueDynamicFlowContext;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ComputerFlowContext extends UniqueDynamicFlowContext {
    private String serverType;
    private String ip;
    private String port;
    private String dirKey;
    private int gridX;
    private int gridY;

    @Override
    public String stateType(){
        return "RInsertComputer";
    }
}
