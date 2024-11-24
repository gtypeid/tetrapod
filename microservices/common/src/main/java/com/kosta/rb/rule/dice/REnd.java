package com.kosta.rb.rule.dice;

import com.kosta.rb.actor.dice.Victory;
import com.kosta.rb.def.BoardConfig;
import com.kosta.rb.rule.abs.RuleRollbackLogger;
import com.kosta.rb.rule.abs.RulRunLogger;
import com.kosta.rb.rule.abs.Rule;
import com.kosta.rb.rule.def.FlowStatus;
import com.kosta.rb.rule.def.RuleProperty;
import com.kosta.rb.rule.abs.RuleRollback;

public class REnd extends Rule implements RuleRollback, RulRunLogger, RuleRollbackLogger {

    protected BoardConfig boardConfig;

    @Override
    protected RuleProperty ruleProperty() {
        boardConfig = (BoardConfig)bc;
        return new RuleProperty();
    }

    @Override
    public void run() {
        FlowStatus fs = getCurFlow();
        Victory victory = store.getFirstActor(BoardConfig.ELayer.FIGURE_FRONT, Victory.class);
        victory.getPosition().setPosition(6, 4);
        victory.visible(true);
        victory.toText(fs.getActiveUserIndex(), boardConfig.goal, fs.getSequence());
    }

    @Override
    public void rollback() {
        Victory victory = store.getFirstActor(BoardConfig.ELayer.FIGURE_FRONT, Victory.class);
        victory.visible(false);
    }

    @Override
    public void rollBackPrevLog() {

    }

    @Override
    public void rollBackCloseLog() {
        sb.appendText(getCurFlow(), " :: END ROLL BACK ");
    }

    @Override
    public void runPrevLog() {

    }

    @Override
    public void runCloseLog() {
        sb.appendText(getCurFlow(), " :: END ");
    }
}
