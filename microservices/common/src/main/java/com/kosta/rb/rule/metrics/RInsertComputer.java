package com.kosta.rb.rule.metrics;

import com.kosta.rb.actor.metrics.Computer;
import com.kosta.rb.actor.metrics.ComputerGrid;
import com.kosta.rb.def.BoardConfig;
import com.kosta.rb.rule.abs.RuleRollbackLogger;
import com.kosta.rb.rule.abs.RulRunLogger;
import com.kosta.rb.rule.abs.Rule;
import com.kosta.rb.rule.abs.RuleRollback;
import com.kosta.rb.rule.def.RuleProperty;

public class RInsertComputer extends Rule implements RuleRollback, RulRunLogger, RuleRollbackLogger {

    protected ComputerGrid computerGrid;

    @Override
    protected RuleProperty ruleProperty() {
        return new RuleProperty();
    }

    @Override
    public void run() {
        computerGrid = getComputerGrid();
        computerGrid.spawnComputer(getCurFlow());
    }

    @Override
    public void rollback() {
        computerGrid.visibleChild(getCurFlow(), false, Computer.class);
    }

    private ComputerGrid getComputerGrid(){
        return store.getActors(BoardConfig.ELayer.NONE, ComputerGrid.class).get(0);
    }

    @Override
    public void runPrevLog() {

    }

    @Override
    public void runCloseLog() {
        sb.appendText(getCurFlow(), "2. 인서트 컴퓨터");
    }

    @Override
    public void rollBackPrevLog() {

    }

    @Override
    public void rollBackCloseLog() {
        sb.appendText(getCurFlow(), "2. 인서트 컴퓨터 롤백");
    }
}
