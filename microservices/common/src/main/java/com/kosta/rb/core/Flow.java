package com.kosta.rb.core;

import com.kosta.rb.core.abs.OnSequenceFlowStatusHandle;
import com.kosta.rb.core.sw.Slider;
import com.kosta.rb.core.sw.ViewFrame;
import com.kosta.rb.def.abs.Config;
import com.kosta.rb.rule.abs.RuleRollbackLogger;
import com.kosta.rb.rule.abs.RulRunLogger;
import com.kosta.rb.rule.abs.Rule;
import com.kosta.rb.rule.abs.RuleRollback;
import com.kosta.rb.rule.def.FlowStatus;
import com.kosta.rb.rule.def.RuleProperty;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Flow {

    private HashMap<String, Rule> loadRuleStore = new HashMap<>();
    private ArrayList<FlowStatus> statusStore = new ArrayList<>();
    private int flowSequence = -1;
    private int virtualCursor = -1;
    private FlowConnector flowConnector;

    protected Board board;
    protected Mode mode;
    protected ViewFrame vf;
    protected Config config;

    public Flow(){
        flowConnector = new FlowConnector(this, this::changeState);
        board = Board.getInstance();

        config = board.getConfig();
        loadRules(config.getRules());
        mode = board.getMode();
        vf = board.getViewFrame();
    }

    public void flowStart(){

        if ( mode.isHost() ){
            changeState(config.entryState());
        }
        else {
            //changeState("RSpawnActors");
        }

        Slider slider = vf.getSlider();
        slider.setSliderLength(flowSequence);
        slider.addChangeListener( (e) -> {
            if(flowConnector.isVirtualCursorFreeze()) return;
            setFlowIndexProgress( slider.getValue() );
        });

        slider.setValue(0);
    }

    public void refreshSlider(){
        Slider slider = vf.getSlider();
        slider.setSliderLength(flowSequence);
    }

    public FlowStatus getCurFlow(){
        return statusStore.get(virtualCursor);
    }

    public FlowStatus getCalcFlow(int cursor){
        return statusStore.get(virtualCursor + cursor);
    }

    public FlowConnector getFlowConnector(){
        return flowConnector;
    }

    public void showStatusStore(){
        System.out.println("--STORE");
        statusStore.forEach( it->{
            System.out.println(it.getRuleName() + ":" + it.getSequence());
        });
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
            // 현재 플로우 실행
            ++virtualCursor;
            //System.out.println("--Virtual Next");
            FlowStatus flowStatus = getCurFlow();
            String ruleName = flowStatus.getRuleName();
            Rule rule = getRule(ruleName);
            //System.out.println("Rule " + ruleName + " Flow SEQ : " + flowStatus.getSequence() + " Prev VCursor " + virtualCursor);
            wrapRunLogger(rule);

            //++virtualCursor;
            //System.out.println("Current Cursor : " + virtualCursor);
            
            // 체이닝된 다음 것 실행
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
            //System.out.println("--Virtual RollBack");
            FlowStatus flowStatus = getCurFlow();
            String ruleName = flowStatus.getRuleName();
            Rule rule = getRule(ruleName);
            //System.out.println("Rule " + ruleName + " Flow SEQ : " + flowStatus.getSequence() + " Prev VCursor " + virtualCursor);

            if(rule instanceof RuleRollback){
                wrapRollBackLogger( (RuleRollback) rule );
                --virtualCursor;
                //System.out.println("Current Cursor : " + virtualCursor);
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
        //System.out.println("--START FLOW ");
        //System.out.println("PREV - FLOW : " + flowSequence + " CURSOR : " + virtualCursor);
        ++flowSequence;
        ++virtualCursor;

        //System.out.println("CURRENT FLOW : " + flowSequence + " CURSOR : " + virtualCursor);

        String ruleName = rule.getClass().getSimpleName();
        FlowStatus flowStatus = new FlowStatus(flowSequence, ruleName);
        FlowStatus prevStatus = hasPrevFlowStatus() ? getCalcFlow(-1) : null;
        if(prevStatus != null)
            flowStatus.duplicateWithoutUniqueFields(prevStatus);
        statusStore.add( flowStatus );

        RuleProperty property = rule.getRuleProperty();
        OnSequenceFlowStatusHandle onSequenceFlowStatusHandle = flowConnector.getOnSequenceFlowStatusHandle();
        if(onSequenceFlowStatusHandle != null){
            onSequenceFlowStatusHandle.apply(rule, flowStatus);
        }

        //System.out.println("START RULE : " + rule);
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
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    private void loadRules(List<Class<? extends Rule>> rules){
        for(Class<? extends Rule> it : rules){
            loadRule(it);
        }
    }

}
