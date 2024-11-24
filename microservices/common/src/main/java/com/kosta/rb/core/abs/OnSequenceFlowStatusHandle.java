package com.kosta.rb.core.abs;

import com.kosta.rb.rule.abs.Rule;
import com.kosta.rb.rule.def.FlowStatus;

@FunctionalInterface
public interface OnSequenceFlowStatusHandle {
    void apply(Rule rule, FlowStatus flowStatus);
}