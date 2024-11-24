package com.kosta.rb.actor.metrics.dynamicflow;

import com.kosta.rb.actor.metrics.dynamicflow.abs.UniqueDynamicFlowContext;
import lombok.*;


@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChainConnectFlowContext extends UniqueDynamicFlowContext {
    private String startDir;
    private String endDir;
    private String type;
    private String json;

    @Override
    public String stateType(){
        return "RInsertChainConnect";
    }
}
