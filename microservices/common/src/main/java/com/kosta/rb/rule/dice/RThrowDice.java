package com.kosta.rb.rule.dice;

import com.kosta.rb.rule.abs.RulRunLogger;
import com.kosta.rb.rule.abs.Rule;
import com.kosta.rb.rule.def.FlowStatus;
import com.kosta.rb.rule.def.RuleProperty;
import com.kosta.rb.rule.abs.RuleRollback;

import java.util.Random;

public class RThrowDice extends Rule implements RuleRollback, RulRunLogger {
    @Override
    protected RuleProperty ruleProperty() {
        return new RuleProperty()
                .setNextRule("RMoveFigure");
    }

    @Override
    public void run() {
        FlowStatus flowStatus = getCurFlow();
        int dice = throwDice();
        flowStatus.setDice( dice );
        store.getMainDice().visible(true).toText(dice);
        store.getMainNode().pathVisible(true, flowStatus);
    }

    @Override
    public void rollback() {
        getCurFlow().setDice( getCalcFlow(-1).getDice() );
    }

    private int throwDice(){
        FlowStatus flowStatus = getCurFlow();
        final int MAX = 6;
        long seed = flowStatus.getSeed() + flowStatus.getSequence();
        Random random = new Random(seed);
        int dice = random.nextInt(MAX) + 1;
        return dice;
    }

    @Override
    public void runPrevLog() {
    }

    @Override
    public void runCloseLog() {
        sb.appendText(getCurFlow(), "Throw Dice : " + getCurFlow().getDice());
    }
}
