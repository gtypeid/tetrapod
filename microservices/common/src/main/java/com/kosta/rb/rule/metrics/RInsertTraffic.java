package com.kosta.rb.rule.metrics;

import com.kosta.rb.actor.metrics.ComputerGrid;
import com.kosta.rb.actor.metrics.Traffic;
import com.kosta.rb.actor.metrics.dynamicflow.TrafficFlowContext;
import com.kosta.rb.def.BoardConfig;
import com.kosta.rb.rule.abs.RuleRollbackLogger;
import com.kosta.rb.rule.abs.RulRunLogger;
import com.kosta.rb.rule.abs.Rule;
import com.kosta.rb.rule.abs.RuleRollback;
import com.kosta.rb.rule.def.RuleProperty;

public class RInsertTraffic extends Rule implements RuleRollback, RulRunLogger, RuleRollbackLogger {
    protected ComputerGrid computerGrid;

    @Override
    protected RuleProperty ruleProperty() {
        return new RuleProperty();
    }

    @Override
    public void run() {
        computerGrid = getComputerGrid();
        computerGrid.spawnTraffic(getCurFlow());
        computerGrid.updateTrafficItemView();
    }

    @Override
    public void rollback() {
        computerGrid.visibleChild(getCurFlow(), false, Traffic.class);
        computerGrid.backTrafficItem();
        computerGrid.updateTrafficItemView();
    }

    @Override
    public void runPrevLog() {
    }

    @Override
    public void runCloseLog() {
        sb.appendText(getCurFlow(), "3. 인서트 트래픽");
    }

    public ComputerGrid getComputerGrid(){
        return store.getActors(BoardConfig.ELayer.NONE, ComputerGrid.class).get(0);
    }

    @Override
    public void rollBackPrevLog() {

    }

    @Override
    public void rollBackCloseLog() {
        sb.appendText(getCurFlow(), "3. 트래픽 롤백");
    }
}
