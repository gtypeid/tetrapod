package com.kosta.rb.core.abs;

public abstract class Comp extends Entity {

    private Actor parent;
    private String compName;

    public void setParent(Actor parent, String compName){
        this.parent = parent;
        this.compName = compName;
    }
    public Actor getParent(){
        return parent;
    }

    public String getCompName(){
        return compName;
    }

    @Override
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode())
                + " .. uid: " + getUid() + " pi: " + getPi() + " parent: " + parent + " compName " + compName;
    }
}
