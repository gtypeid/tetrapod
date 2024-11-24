package com.kosta.rb;

import com.kosta.rb.core.Board;
import com.kosta.rb.core.Mode;
import com.kosta.rb.core.sw.Slider;
import com.kosta.rb.core.sw.ViewFrame;
import com.kosta.rb.rule.abs.RuleRollbackLogger;
import com.kosta.rb.rule.abs.RulRunLogger;
import com.kosta.rb.rule.abs.Rule;
import com.kosta.rb.rule.abs.RuleRollback;
import com.kosta.rb.rule.def.FlowStatus;
import com.kosta.rb.rule.def.RuleProperty;
import com.kosta.rb.rule.dice.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;

public class FlowCopy {

    private static final Logger log = LoggerFactory.getLogger(FlowCopy.class);
    private HashMap<String, Rule> loadRuleStore = new HashMap<>();
    private ArrayList<FlowStatus> statusStore = new ArrayList<>();
    private int flowSequence = -1;
    private int virtualCursor = -1;

    protected Board board;
    protected Mode mode;
    protected ViewFrame vf;

    public FlowCopy(){

        loadRules();
        board = Board.getInstance();
        mode = board.getMode();
        vf = board.getViewFrame();
    }

    public void flowStart(){

        if ( mode.isHost() ){
            changeState("RConstructor");
        }
        else {
            changeState("RSpawnActors");
        }

        Slider slider = vf.getSlider();
        slider.setSliderLength(flowSequence);
        slider.addChangeListener( (e) -> {
            setFlowIndexProgress( slider.getValue() );
        });

        slider.setValue(0);
    }

    public FlowStatus getCurFlow(){
        return statusStore.get(virtualCursor);
    }
    public FlowStatus getCalcFlow(int cursor){
        return statusStore.get(virtualCursor + cursor);
    }

    private void setFlowIndexProgress(int targetIndex){
        int calc = targetIndex - virtualCursor;
        if( calc > 0){
            for(int i = 0; i < calc; ++i){
                virtualNext();
            }
        } else {
            for( int i = 0; i < Math.abs(calc); ++i){
                virtualRollBack();
            }
        }
    }

    private void virtualNext(){
        if(checkPlusCursor()){
            FlowStatus flowStatus = getCurFlow();
            String ruleName = flowStatus.getRuleName();
            Rule rule = getRule(ruleName);
            wrapRunLogger(rule);
            ++virtualCursor;

            if(virtualCursor == flowSequence){
                RuleProperty property = rule.getRuleProperty();
                String next = property.getNextRule();
                if(next != null && !next.isEmpty()){
                    Rule nextRule = getRule(next);
                    wrapRunLogger(nextRule);
                }
            }
        }
    }

    private void virtualRollBack(){
        if(checkMinusCursor()){
            FlowStatus flowStatus = getCurFlow();
            String ruleName = flowStatus.getRuleName();
            Rule rule = getRule(ruleName);
            if(rule instanceof RuleRollback){
                wrapRollBackLogger( (RuleRollback) rule );
                --virtualCursor;
            }
        }
    }

    private boolean checkPlusCursor(){
        int calc = virtualCursor + 1;
        return calc <= flowSequence;
    }

    private boolean checkMinusCursor(){
        int calc = virtualCursor - 1;
        return calc >= 0;
    }


    private void changeState(Class t){
        changeState(t.getSimpleName());
    }

    private void changeState(String ruleType){
        Rule rule = getRule(ruleType);
        boolean isSameCursor = (flowSequence == virtualCursor);
        boolean isFlowSequenceProcessing = ( flowSequence == statusStore.size() - 1);
        if( isSameCursor && isFlowSequenceProcessing){
            sequenceProgressFlow(rule);
        }
    }

    private void sequenceProgressFlow(Rule rule){
        ++flowSequence;
        ++virtualCursor;

        String ruleName = rule.getClass().getSimpleName();
        FlowStatus flowStatus = new FlowStatus(flowSequence, ruleName);
        FlowStatus prevStatus = hasPrevFlowStatus() ? getCalcFlow(-1) : null;
        if(prevStatus != null)
            flowStatus.duplicateWithoutUniqueFields(prevStatus);
        statusStore.add( flowStatus );

        RuleProperty property = rule.getRuleProperty();
        wrapRunLogger(rule);
        String next = property.getNextRule();
        if(next != null && !next.isEmpty()){
            changeState(next);
        }
    }

    private void wrapRunLogger(Rule rule){
        if(rule instanceof RulRunLogger){
            ((RulRunLogger)rule).runPrevLog();
        }
        rule.run();
        if(rule instanceof RulRunLogger){
            ((RulRunLogger)rule).runCloseLog();
        }
    }

    private void wrapRollBackLogger(RuleRollback rule){
        if(rule instanceof RuleRollbackLogger){
            ((RuleRollbackLogger)rule).rollBackPrevLog();
        }
        rule.rollback();
        if(rule instanceof RuleRollbackLogger){
            ((RuleRollbackLogger)rule).rollBackCloseLog();
        }
    }

    private boolean hasPrevFlowStatus(){
        int cursor = virtualCursor - 1;
        if(cursor >= 0){
            return true;
        }
        return false;
    }

    private Rule getRule(String name){
        boolean hasKey = loadRuleStore.containsKey(name);
        if(hasKey){
            return loadRuleStore.get(name);
        }
        return null;
    }

    private Rule getRule(Class t){
        return getRule( t.getSimpleName() );
    }

    private <T> T loadRule(Class<T> t){
        try {
            Constructor<T> ct = t.getDeclaredConstructor();
            String name = t.getSimpleName();
            boolean hasKey = loadRuleStore.containsKey(name);
            if( !hasKey ){
                T newRule = ct.newInstance();
                loadRuleStore.put(name, (Rule) newRule );
            }
            return (T) loadRuleStore.get(name);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadRules(){
        loadRule(RConstructor.class);
        loadRule(RSpawnActors.class);
        loadRule(RUserActive.class);
        loadRule(RThrowDice.class);
        loadRule(RMoveFigure.class);
        loadRule(REnd.class);
    }

}
