package com.kosta.rb.rule.dice;

import com.kosta.rb.actor.dice.*;
import com.kosta.rb.def.BoardConfig;
import com.kosta.rb.def.Util;
import com.kosta.rb.core.abs.Actor;
import com.kosta.rb.rule.abs.RuleRollbackLogger;
import com.kosta.rb.rule.abs.RulRunLogger;
import com.kosta.rb.rule.abs.Rule;
import com.kosta.rb.rule.def.RuleProperty;
import com.kosta.rb.rule.abs.RuleRollback;
import java.util.ArrayList;


public class RSpawnActors extends Rule implements RuleRollback, RulRunLogger, RuleRollbackLogger {

    protected ArrayList<Actor> spawnActors = new ArrayList<>();
    protected Node node;
    protected Dice dice;
    protected BoardConfig boardConfig;

    @Override
    protected RuleProperty ruleProperty(){
        boardConfig = (BoardConfig) bc;
        return new RuleProperty()
                .setNextRule("RUserActive");
    }

    @Override
    public void run() {
        getCurFlow().setSeed( System.currentTimeMillis() );
        spawnActors.add( spawnPaper() );
        spawnActors.addAll( spawnFloors() );
        spawnActors.add( spawnNode() );
        spawnActors.addAll( spawnFigures() );
        spawnActors.add( spawnArrow() );
        spawnActors.add( spawnVictory() );
        spawnActors.add( spawnDice() );
        board.getViewFrame().reDraw();

        if(!mode.isHost()){
            ruleProperty.setNextRule("");
        }
    }

    @Override
    public void rollback() {
        spawnActors.forEach( it -> it.destroy() );
        board.getViewFrame().reDraw();
        spawnActors.clear();
    }

    private Paper spawnPaper(){
        Paper paper = store.spawnActor(Paper.class);
        paper.getPosition().setPosition(1, 1);
        return paper;
    }

    private ArrayList<Actor> spawnFloors(){
        ArrayList<Actor> floors = new ArrayList<>();

        final byte BLANK = 0;
        int size = ((BoardConfig)bc).floorSize;
        for(int y = 0; y < size; ++y){
            for(int x = 0; x < size; ++x){
                int index = Util.floorIndex(x, y, size);
                byte data = boardConfig.floorData[index];
                if(data != BLANK) {
                    floors.add( spawnFloorActor(data, x, y) );
                }
            }
        }

        return floors;
    }

    private Floor spawnFloorActor(byte type, int x, int y){

        final int CALC = 2;
        int index = Util.floorIndex(x, y, boardConfig.floorSize);
        Floor floor = store.spawnActor(Floor.class);
        floor.setLinkIndex(index);
        floor.getPosition().setPosition(x + CALC, y + CALC);
        floor.changeFloor(type);

        return floor;
    }

    private Node spawnNode(){
        node = store.spawnActor(Node.class);
        return node;
    }

    private ArrayList<Actor> spawnFigures(){
        ArrayList<Actor> figures = new ArrayList<>();
        for(int i = 0; i < BoardConfig.MAX_PLAYER_SIZE; ++i){
            Figure figure = store.spawnActor(Figure.class);
            node.attachFigure(figure);
            figure.changeColor(i);
            figures.add(figure);
        }
        return figures;
    }

    private Actor spawnArrow(){
        return store.spawnActor(Arrow.class);
    }

    private Actor spawnVictory(){
        return store.spawnActor(Victory.class);
    }

    private Actor spawnDice(){
        dice = store.spawnActor(Dice.class);
        dice.getPosition().setPosition(5,3);
        return dice;
    }
    

    @Override
    public void rollBackPrevLog() {
        
    }

    @Override
    public void rollBackCloseLog() {
        sb.appendText(getCurFlow(), "액터 삭제");
    }

    @Override
    public void runPrevLog() {

    }

    @Override
    public void runCloseLog() {
        StringBuilder builder = new StringBuilder();
        for(Actor actor : spawnActors){
            builder.append( actor.getClass().getSimpleName() )
                    .append(" : ")
                    .append(actor.getUid())
                    .append("\n");
        }
        builder.append(" :: 액터 생성");
        sb.appendText(getCurFlow(), builder.toString());
    }
}
