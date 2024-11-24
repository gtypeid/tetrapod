package com.kosta.rb.rule.dice;

import com.kosta.rb.actor.dice.Figure;
import com.kosta.rb.actor.dice.Node;
import com.kosta.rb.def.BoardConfig;
//import com.kosta.winter.core.Http;
//import com.kosta.winter.def.HttpRequest;
import com.kosta.rb.rule.abs.RuleRollbackLogger;
import com.kosta.rb.rule.abs.RulRunLogger;
import com.kosta.rb.rule.abs.Rule;
import com.kosta.rb.rule.def.FlowStatus;
import com.kosta.rb.rule.def.RuleProperty;
import com.kosta.rb.rule.abs.RuleRollback;
import org.json.JSONObject;

public class RMoveFigure extends Rule implements RuleRollback, RulRunLogger, RuleRollbackLogger {

    protected RuleProperty ruleProperty;
    protected BoardConfig boardConfig;

    @Override
    protected RuleProperty ruleProperty() {
        boardConfig = (BoardConfig) bc;
        ruleProperty = new RuleProperty()
                .setNextRule("RUserActive");
        return ruleProperty;
    }

    @Override
    public void run() {
        Node node = store.getMainNode();
        FlowStatus flowStatus = getCurFlow();
        Figure figure = store.getActiveFigure(flowStatus);
        node.next(figure, flowStatus);
        store.getMainDice().visible(false);
        store.getMainNode().pathVisible(false, flowStatus);
        if ( node.isGoalCheck(figure, boardConfig.goal) ){
            ruleProperty.setNextRule("REnd");
        }

        /*
        HttpRequest request = new HttpRequest();
        request.setMethod(Http.POST);
        request.setUrl("http://localhost:8081/flow");
        request.setBody( new JSONObject(flowStatus).toString() );
        http.doRequest(request, (code, response)->{

        });
        */

    }

    @Override
    public void rollback() {
        Node node = store.getMainNode();
        FlowStatus flowStatus = getCurFlow();
        FlowStatus prevFindStatus = getCalcFlow(-1);
        Figure figure = store.getActiveFigure(flowStatus);
        
        node.prev(figure, flowStatus, prevFindStatus);
        store.getMainDice().visible(false);
        store.getMainNode().pathVisible(false, getCurFlow());
        ruleProperty.setNextRule("RUserActive");

        /*
        HttpRequest request = new HttpRequest();
        request.setMethod(Http.POST);
        request.setUrl("http://localhost:8081/flow");
        request.setBody( new JSONObject(flowStatus).toString() );
        http.doRequest(request, (code, response)->{

        });
        */
    }

    @Override
    public void rollBackPrevLog() {
    }

    @Override
    public void rollBackCloseLog() {
        FlowStatus curFlow = getCurFlow();
        Figure figure = store.getActiveFigure(curFlow);
        sb.appendText(getCurFlow(), "Figure Move RollBack - " + figure + " " + curFlow.getPlayerNodeIndex() );
    }

    @Override
    public void runPrevLog() {

    }

    @Override
    public void runCloseLog() {
        FlowStatus curFlow = getCurFlow();
        Figure figure = store.getActiveFigure(curFlow);
        sb.appendText(getCurFlow(), "Figure Move - " + figure + " " + curFlow.getPlayerNodeIndex() );
    }
}
