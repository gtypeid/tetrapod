package com.kosta.rb.actor.dice;

import com.kosta.rb.comp.Position;
import com.kosta.rb.core.Board;
import com.kosta.rb.core.Store;
import com.kosta.rb.core.sw.ViewFrame;
import com.kosta.rb.def.BoardConfig;
import com.kosta.rb.def.Util;
import com.kosta.rb.core.abs.Actor;
import com.kosta.rb.rule.def.FlowStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class Node extends Actor {

    private ArrayList<Floor> nodePath = new ArrayList<>();
    private HashMap<Integer, NodeStatus> nodeStatusStore = new HashMap<>();
    private ArrayList<Floor> drawPathNode = new ArrayList<>();

    private boolean isDrawPath = false;

    protected Board board;
    protected BoardConfig bc;
    protected Store store;
    protected int size;

    public Node(){
        board = Board.getInstance();
        bc = (BoardConfig) board.getConfig();
        store = board.getStore();
        size = bc.floorSize;
        board.getViewFrame().getRenderer().setMainNode(this);
        setPath();
    }

    public void attachFigure(Figure figure){
        int pid = figure.getPi();
        boolean hasKey = nodeStatusStore.containsKey(pid);
        if(!hasKey){
            NodeStatus nodeStatus = new NodeStatus()
                    .setFigure(figure);
            nodeStatusStore.put(pid, nodeStatus);
            posFixedSet(nodeStatus, 0);
        }
    }

    public void next(Figure figure, FlowStatus flowStatus){
        int pid = figure.getPi();
        NodeStatus nodeStatus = nodeStatusStore.get(pid);
        int nIndex = getNextFloorIndex( nodeStatus, flowStatus );
        flowStatus.setDice(0);
        flowStatus.setPlayerNodeIndex(nIndex);
        posFixedSet(nodeStatus, nIndex);
    }

    public void prev(Figure figure, FlowStatus flowStatus, FlowStatus prevStatus) {
        int pid = figure.getPi();
        NodeStatus nodeStatus = nodeStatusStore.get(pid);
        int curIndex = flowStatus.getPlayerNodeIndex();
        int prevIndex = prevStatus.getPlayerNodeIndex();
        boolean isMinusGoalCheck = ((curIndex - prevIndex) < 0);
        if( isMinusGoalCheck )
            nodeStatus.minusGoal();
        flowStatus.setPlayerNodeIndex(prevIndex);
        flowStatus.setDice(prevStatus.getDice());
        posFixedSet(nodeStatus, prevIndex);
    }

    public boolean isGoalCheck(Figure figure, int goal){
        boolean flag = false;
        int pid = figure.getPi();
        NodeStatus status = nodeStatusStore.get(pid);
        if ( status.getGoalCount() >= goal ) {
            flag = true;
        }

        return flag;
    }

    public void pathVisible(boolean flag, FlowStatus flowStatus){
        ViewFrame vf = board.getViewFrame();
        setDrawPath(flag);
        if(flag) {

            int curNodeIndex = flowStatus.getPlayerNodeIndex();
            int dice = flowStatus.getDice();
            int nodeSize = nodePath.size();

            for (int i = 0; i < dice + 1; ++i) {
                int calc = curNodeIndex + i;
                if (calc >= nodeSize) {
                    calc = calc - nodeSize;
                }
                drawPathNode.add(nodePath.get(calc));
            }
        }
        else {
            drawPathNode.clear();
            vf.reDraw();
        }
    }

    public ArrayList<Floor> getDrawPathNode(){
        return drawPathNode;
    }

    public boolean isDrawPath() {
        return isDrawPath;
    }

    public void setDrawPath(boolean drawPath) {
        this.isDrawPath = drawPath;
    }


    private int getNextFloorIndex(NodeStatus nodeStatus, FlowStatus flowStatus){
        int nodeSize = nodePath.size();
        int calc = flowStatus.getPlayerNodeIndex() + flowStatus.getDice();
        if(calc >= nodeSize){
            calc = calc - nodeSize;
            nodeStatus.plusGoal();
        }
        return calc;
    }

    private void posFixedSet(NodeStatus nodeStatus, int floorIndex){
        Position figurePosition = nodeStatus.getFigure().getPosition();
        Position floorPosition = nodePath.get(floorIndex).getPosition();
        figurePosition.setPosition( floorPosition.getXY() );
    }

    public void posFixedSet(int figureIndex, int floorIndex){
        ArrayList<Figure> figures = store.getActors(BoardConfig.ELayer.FIGURE, Figure.class);
        Figure figure = figures.get(figureIndex);
        Position figurePosition = figure.getPosition();
        Position floorPosition = nodePath.get(floorIndex).getPosition();
        figurePosition.setPosition( floorPosition.getXY() );
    }

    private void setPath(){
        ArrayList<Floor> floors = store.getActors(BoardConfig.ELayer.FLOOR);

        for(int x = 0; x < size; x++){
            int y = (size - 1);
            int index = Util.floorIndex(x, y, size);
            addPath(floors, index);
        }

        for (int y = size; y > 0; --y){
            int x = (size - 1);
            int index = Util.floorIndex(x, y, size);
            addPath(floors, index);
        }

        for(int x = size; x > 0; --x){
            int y = 0;
            int index = Util.floorIndex(x, y, size);
            addPath(floors, index);
        }

        for (int y = 0; y < size; ++y){
            int x = 0;
            int index = Util.floorIndex(x, y, size);
            addPath(floors, index);
        }
    }

    private void addPath(ArrayList<Floor> floors, int index){
        for(Floor it : floors){
            boolean isFind = (it.getLinkIndex() == index);
            if(isFind){
                boolean hasFloor = nodePath.contains(it);
                if(!hasFloor){
                    nodePath.add(it);
                }
            }
        }
    }

    private static class NodeStatus{

        private Figure figure;
        private int goalCount;

        public int getGoalCount(){
            return goalCount;
        }

        public NodeStatus plusGoal(){
            ++goalCount;
            return this;
        }

        public NodeStatus minusGoal(){
            --goalCount;
            return this;
        }

        public Figure getFigure() {
            return figure;
        }

        public NodeStatus setFigure(Figure figure) {
            this.figure = figure;
            return this;
        }

    }
}
