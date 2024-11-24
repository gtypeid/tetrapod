package com.kosta.rb.core.abs;
import java.util.UUID;

public abstract class Entity {

    private static int sequence;
    private final int pi;
    private final String uid;

    public Entity(){
        pi = sequence++;
        uid = UUID.randomUUID().toString();
    }

    public String getUid(){
        return uid;
    }

    public int getPi(){
        return pi;
    }

    public int getSequence(){
        return sequence;
    }

    @Override
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode()) + " .. uid: " + uid + " pi: " + pi;
    }

}
