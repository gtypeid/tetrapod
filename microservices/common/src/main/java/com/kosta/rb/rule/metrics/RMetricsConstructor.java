package com.kosta.rb.rule.metrics;

import com.kosta.rb.actor.metrics.ComputerGrid;
import com.kosta.rb.core.abs.Actor;
import com.kosta.rb.rule.abs.RulRunLogger;
import com.kosta.rb.rule.abs.Rule;
import com.kosta.rb.rule.abs.RuleRollback;
import com.kosta.rb.rule.def.RuleProperty;

import java.util.ArrayList;
import java.util.List;

public class RMetricsConstructor extends Rule implements RulRunLogger {

    private ComputerGrid computerGrid;

    @Override
    protected RuleProperty ruleProperty() {
        return new RuleProperty();
    }

    @Override
    public void run() {
        computerGrid = store.spawnActor(ComputerGrid.class);
    }

    @Override
    public void runPrevLog() {
    }

    @Override
    public void runCloseLog() {
        sb.appendText(getCurFlow(), "1. 매트릭 시작");
    }
}
