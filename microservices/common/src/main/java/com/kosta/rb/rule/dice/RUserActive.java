package com.kosta.rb.rule.dice;

import com.kosta.rb.actor.dice.Arrow;
import com.kosta.rb.actor.dice.Figure;
import com.kosta.rb.comp.Position;
import com.kosta.rb.def.BoardConfig;
import com.kosta.rb.rule.abs.RulRunLogger;
import com.kosta.rb.rule.abs.Rule;
import com.kosta.rb.rule.def.FlowStatus;
import com.kosta.rb.rule.def.RuleProperty;
import com.kosta.rb.rule.abs.RuleRollback;

public class RUserActive extends Rule implements RuleRollback, RulRunLogger {
    @Override
    protected RuleProperty ruleProperty() {
        return new RuleProperty()
                .setNextRule("RThrowDice");
    }

    @Override
    public void run() {
        FlowStatus flowStatus = getCurFlow();
        int nextIndex = nextUserIndex( flowStatus.getActiveUserIndex() );
        flowStatus.setActiveUserIndex(nextIndex);
        arrowUpdate(flowStatus);
    }

    @Override
    public void rollback() {
        FlowStatus flowStatus = getCurFlow();
        FlowStatus prevStatus = getCalcFlow(-1);
        int prevUserIndex = prevStatus.getActiveUserIndex();
        flowStatus.setActiveUserIndex( prevUserIndex );
        arrowUpdate(prevStatus);
    }

    private int nextUserIndex(int prevUserIndex){
        int calc = prevUserIndex + 1;
        if(calc >= BoardConfig.MAX_PLAYER_SIZE){
            calc = 0;
        }
        return calc;
    }

    private void arrowUpdate(FlowStatus status){
        if ( status.getActiveUserIndex() == -1 ) return;

        Arrow arrow = store.getFirstActor(BoardConfig.ELayer.FLOOR_FRONT, Arrow.class);
        Position arrowPos = arrow.getPosition();
        Figure figure = store.getActiveFigure(status);
        arrowPos.setAttachPosParent(figure);
    }

    @Override
    public void runPrevLog() {
    }

    @Override
    public void runCloseLog() {
        int index = getCurFlow().getActiveUserIndex();
        String s = "Active UserIndex :"
                + index;
        sb.appendText(getCurFlow(), s);
    }
}
