package com.kosta.rb.rule.def;

public class RuleProperty {

    public String getNextRule() {
        return nextRule;
    }

    public RuleProperty setNextRule(String nextRule) {
        this.nextRule = nextRule;
        return this;
    }

    private String nextRule;

}
