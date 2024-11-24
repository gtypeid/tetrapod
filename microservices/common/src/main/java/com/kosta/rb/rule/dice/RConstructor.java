package com.kosta.rb.rule.dice;

import com.kosta.rb.rule.abs.RulRunLogger;
import com.kosta.rb.rule.abs.Rule;
import com.kosta.rb.rule.def.RuleProperty;

public class RConstructor extends Rule implements RulRunLogger {

    @Override
    protected RuleProperty ruleProperty() {
        return new RuleProperty()
            .setNextRule("RSpawnActors");
    }

    @Override
    public void run() {
    }

    @Override
    public void runPrevLog() {
    }

    @Override
    public void runCloseLog() {
        sb.appendText(getCurFlow(), "1. 초기화 성공");
    }
}
