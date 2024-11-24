package com.kosta.rb.core.abs;
import com.kosta.rb.comp.Position;
import com.kosta.rb.core.Board;
import com.kosta.rb.core.Store;
import com.kosta.rb.def.BoardConfig;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Actor extends Entity {

    private int actorLayer;
    private Map<String, ArrayList<Comp>> compStore = new HashMap<>();

    public Actor(){
        actorLayer = BoardConfig.ELayer.NONE.getValue();
    }

    public void destroy(){
        Board board = Board.getInstance();
        Store store = board.getStore();
        store.destroy(this);
        compStore.clear();
    }

    public <T> T attachComp(Class<T> t){
        return attachComp(t, t.getSimpleName());
    }

    public <T> T attachComp(Class<T> t, String compName){
        try {
            Constructor<T> ct = t.getDeclaredConstructor();
            String name = t.getSimpleName();
            T newComp = ct.newInstance();
            Comp cast = (Comp) newComp;
            cast.setParent(this, compName);

            boolean hasKey = compStore.containsKey(name);
            if( !hasKey ){
                compStore.put(name, new ArrayList<Comp>());
            }
            compStore.get(name).add(cast);
            return newComp;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getComp(Class<T> t){
        String key = t.getSimpleName();
        return (T) getComp(key, 0);
    }

    public Position getPosition(){
        return getComp(Position.class);
    }

    public <T> T getComp(String compKey){
        return (T) getComp(compKey, 0);
    }

    public <T> ArrayList<T> getComps(String compKey){
        boolean hasKey = compStore.containsKey(compKey);
        if(hasKey){
            return (ArrayList<T>) compStore.get(compKey);
        }
        return null;
    }

    public Comp getComp(String compKey, String compName){
        boolean hasKey = compStore.containsKey(compKey);
        if( hasKey ){
            ArrayList<Comp> comps = compStore.get(compKey);
            return comps.stream()
                    .filter( it -> (it.getCompName().equals(compName) ) )
                    .findAny()
                    .get();
        }
        return null;
    }

    public Comp getComp(String compKey, int index)  {
        boolean hasKey = compStore.containsKey(compKey);
        if( !hasKey ){
            return null;
        }
        return compStore.get(compKey).get(index);
    }

    public int getActorLayer(){
        return actorLayer;
    }

    public void setActorLayer(int layer){
        this.actorLayer = layer;
    }

}
