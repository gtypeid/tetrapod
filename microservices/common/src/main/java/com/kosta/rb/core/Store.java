package com.kosta.rb.core;

import com.kosta.rb.actor.dice.Dice;
import com.kosta.rb.actor.dice.Figure;
import com.kosta.rb.actor.dice.Node;
import com.kosta.rb.actor.metrics.dynamicflow.abs.DynamicFlowActor;
import com.kosta.rb.actor.metrics.dynamicflow.abs.UniqueDynamicFlowContext;
import com.kosta.rb.core.sw.ViewFrame;
import com.kosta.rb.def.BoardConfig;
import com.kosta.rb.core.abs.Actor;
import com.kosta.rb.def.abs.Config;
import com.kosta.rb.rule.def.FlowStatus;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

public class Store {

    private Map<Integer, ArrayList<Actor>> actorStore = new HashMap<>();

    protected Board board;
    protected Config bc;

    public Store(){
        board = Board.getInstance();
        bc = board.getConfig();
    }

    public void destroy(Actor actor){
        int rayer = actor.getActorLayer();
        ArrayList<Actor> actors = actorStore.get(rayer);
        actors.remove(actor);
    }

    public <T> T dynamicFlowSpawnActor(Class<T> t, UniqueDynamicFlowContext ctx){
        DynamicFlowActor actor = (DynamicFlowActor) spawnActor(t);
        actor.dynamicFlowBind(ctx);
        return (T) actor;
    }

    public <T> T spawnActor(Class<T> t){
        try {
            Constructor<T> ct = t.getDeclaredConstructor();
            T newActor = ct.newInstance();
            insertActor((Actor) newActor);
            return newActor;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getFirstActor(Config.ELayer layer, Class<T> t){
        return (T) getActors(layer.getValue(), t).get(0);
    }

    public ArrayList<Actor> getActors(){
        ArrayList<Actor> list = new ArrayList<>();
        actorStore.values().forEach( it ->{
            list.addAll(it);
        });

        return list;
    }

    public <T> ArrayList<T> getActors(BoardConfig.ELayer layer, Class<T> t){
        return getActors(layer.getValue(), t);
    }

    public <T> ArrayList<T> getActors(int layer, Class<T> t){
        ArrayList<T> list = getActors(layer);
        if(list == null) return null;

        return (ArrayList<T>) list.stream()
                .filter( it -> {
                    String lName = it.getClass().getSimpleName();
                    String rName = t.getSimpleName();
                    return lName.equals(rName);
                })
                .collect(Collectors.toList());
    }

    public <T> ArrayList<T> getActors(BoardConfig.ELayer layer){
        return getActors(layer.getValue());
    }

    public <T> ArrayList<T> getActors(int layer){
        boolean hasKey = actorStore.containsKey(layer);
        if(hasKey){
            return (ArrayList<T>) actorStore.get(layer);
        }
        return null;
    }

    public Figure getActiveFigure(FlowStatus status){
        int activeIndex = status.getActiveUserIndex();
        ArrayList<Figure> figures = getActors(BoardConfig.ELayer.FIGURE);
        return figures.get(activeIndex);
    }

    public Node getMainNode(){
        return getActors(BoardConfig.ELayer.NONE, Node.class).get(0);
    }

    public Dice getMainDice(){
        return getActors(BoardConfig.ELayer.FIGURE_FRONT, Dice.class).get(0);
    }


    private void insertActor(Actor newActor){
        ViewFrame vf = board.getViewFrame();
        int layer = newActor.getActorLayer();

        boolean hasKey = actorStore.containsKey(layer);
        if(!hasKey){
            actorStore.put(layer, new ArrayList<Actor>());
        }
        actorStore.get(layer).add(newActor);
        vf.reDraw();
    }


}
