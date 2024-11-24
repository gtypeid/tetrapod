package com.kosta.rb.rule.def;

import com.kosta.rb.actor.metrics.dynamicflow.abs.UniqueDynamicFlowContext;
import com.kosta.rb.def.BoardConfig;

public class FlowStatus {
    private int sequence;
    private String ruleName;

    private int activeUserIndex = -1;
    private long seed;
    private int dice;
    private int[] playerNodeIndex = new int[BoardConfig.MAX_PLAYER_SIZE];

    private UniqueDynamicFlowContext uniqueDynamicFlow;

    public FlowStatus (){

    }

    public FlowStatus(int sequence, String ruleName){
        this.sequence = sequence;
        this.ruleName = ruleName;
    }

    public void duplicateWithoutUniqueFields(FlowStatus target){
        if(target != null){
            activeUserIndex = target.getActiveUserIndex();
            seed = target.getSeed();
            dice = target.getDice();
            int[] nodes = target.getPlayerNodesIndexs();
            for(int i = 0; i < nodes.length; ++i){
                playerNodeIndex[i] = nodes[i];
            }
        }
    }

    public <T extends UniqueDynamicFlowContext> void setUniqueDynamicFlow(T uniqueDynamicFlow){
        this.uniqueDynamicFlow = uniqueDynamicFlow;
    }

    public <T extends UniqueDynamicFlowContext> T getUniqueDynamicFlow(){
        return (T) uniqueDynamicFlow;
    }


    public void setPlayerNodeIndex(int nodeIndex){
        setPlayerNodeIndex(activeUserIndex, nodeIndex);
    }

    public void setPlayerNodeIndex(int activeUserIndex, int nodeIndex){
        playerNodeIndex[activeUserIndex] = nodeIndex;
    }

    public int getPlayerNodeIndex(){
        return playerNodeIndex[activeUserIndex];
    }
    public int getPlayerNodeIndex(int activeUserIndex){
        return playerNodeIndex[activeUserIndex];
    }

    public int[] getPlayerNodesIndexs(){
        return playerNodeIndex;
    }

    public int getDice() {
        return dice;
    }

    public FlowStatus setDice(int dice) {
        this.dice = dice;
        return this;
    }

    public long getSeed() {
        return seed;
    }

    public FlowStatus setSeed(long seed) {
        this.seed = seed;
        return this;
    }

    public int getActiveUserIndex() {
        return activeUserIndex;
    }

    public FlowStatus setActiveUserIndex(int activeUserIndex) {
        this.activeUserIndex = activeUserIndex;
        return this;
    }

    public String getRuleName() {
        return ruleName;
    }

    public int getSequence(){
        return sequence;
    }
    @Override
    public String toString() {
        String s = "[[ ruleName : " + ruleName + " ]]" + "\n"
                + " | sequence : " + sequence
                + " | activeUserIndex : " + activeUserIndex
                + " | seed : " + seed
                + " | dice : " + dice
                + "\n | playerNodeIndex[0] : " + playerNodeIndex[0]
                + "\n | playerNodeIndex[1] : " + playerNodeIndex[1]
                + "\n | playerNodeIndex[2] : " + playerNodeIndex[2]
                + "\n | playerNodeIndex[3] : " + playerNodeIndex[3]
                + " ]";
        ;
        return s;
    }
}
