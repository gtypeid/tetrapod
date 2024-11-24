package com.kosta.rb.core.sw;
import com.kosta.rb.actor.dice.Floor;
import com.kosta.rb.actor.dice.Node;
import com.kosta.rb.actor.metrics.Computer;
import com.kosta.rb.actor.metrics.Traffic;
import com.kosta.rb.actor.metrics.dynamicflow.TrafficFlowContext;
import com.kosta.rb.core.Board;
import com.kosta.rb.core.Store;
import com.kosta.rb.def.BoardConfig;
import com.kosta.rb.comp.Graphic;
import com.kosta.rb.comp.Position;
import com.kosta.rb.core.abs.Actor;
import com.kosta.rb.def.MetricsConfig;
import com.kosta.rb.def.abs.Config;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Renderer extends JPanel {

    private ViewFrame viewFrame;
    private Node mainNode;
    private boolean isRender = false;

    private boolean isDrawPivot = false;
    private boolean isDrawPosition = false;
    private boolean isDrawRect = false;

    protected Board board;
    protected Config bc;
    protected Store store;


    public Renderer(ViewFrame frame){
        viewFrame = frame;
        board = Board.getInstance();
        bc = board.getConfig();
        store = board.getStore();

        int width = bc.getWindowContext().getWindowSizeWidth() - 500;
        int height = bc.getWindowContext().getWindowSizeHeight() - 200;
        setBounds(0, 0, width, height);
    }


    public void render(Graphics g){
        if(!isRender) return;

        List<Actor> actors = store.getActors();
        for(Actor it : actors){
            actorDraw(it, g);
        }
    }

    public void setIsRender(boolean flag){
        isRender = flag;
        if(isRender){
            repaint();
        }
    }

    public boolean getIsRender(){
        return isRender;
    }

    public void setMainNode(Node node){
        mainNode = node;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(!isRender) return;

        List<Actor> actors = store.getActors();
        for(Actor it : actors){
            actorDraw(it, g);
        }

        if(bc instanceof BoardConfig){
            BoardConfig asBc = (BoardConfig) bc;
            if(mainNode.isDrawPath() && asBc.isDrawPath){
                ArrayList<Floor> path = mainNode.getDrawPathNode();
                drawPath(asBc, g, path);
            }
        }

        if(bc instanceof MetricsConfig){
            drawTrafficLine(g);
        }
    }

    private void drawTrafficLine(Graphics g){
        MetricsConfig asBc = (MetricsConfig) bc;
        List<Traffic> traffics = store.getActors(Config.ELayer.FLOOR_FRONT, Traffic.class);
        if(traffics == null) return;

        for(Traffic it : traffics){
            Graphic gp = it.getComp("Graphic");
            if ( gp.isVisible() ){
                Computer start = it.getStartDirComputer();
                Computer end = it.getEndDirComputer();

                int[] startXY = getActorRenderPositionXY(start);
                int[] middleXY = getActorRenderPositionXY(it);
                int[] endXY = getActorRenderPositionXY(end);

                int calc = 0;
                Graphics2D g2 = (Graphics2D)g;
                TrafficFlowContext ctx = it.getCTX();
                Color color = (ctx.getReqs().equals("request")) ? new Color(255, 0, 0, 25) : new Color(34, 255, 0, 25);
                g2.setStroke( new BasicStroke(3) );
                g2.setColor(color);
                g2.drawLine(startXY[0] + calc, startXY[1] + calc, middleXY[0] + calc, middleXY[1] + calc);
                g2.drawLine(middleXY[0] + calc, middleXY[1] + calc, endXY[0] + calc, endXY[1] + calc);
            }
        }
    }

    public void drawPath(BoardConfig bc, Graphics g, ArrayList<Floor> path){
        for(int i = 0; i < path.size(); i++){
            int next = i + 1;
            if(next >= path.size())
                next = i;

            Floor cf = path.get(i);
            Floor nf = path.get(next);

            int[] startXY = getActorRenderScreenXY(cf);
            int[] endXY = getActorRenderScreenXY(nf);
            int calc = 15;

            Graphics2D g2 = (Graphics2D)g;
            g2.setStroke( new BasicStroke(bc.drawPathLineStroke) );
            g2.setColor( bc.drawPathColor );
            g2.drawLine(startXY[0] + calc, startXY[1] + calc, endXY[0] + calc, endXY[1] + calc);
        }
    }

    private int[] getActorPositionScreenXY(Position pos, Graphic gp){
        int screenX = (pos.getX() * bc.floorWorldGrid);
        int screenY = (pos.getY() * bc.floorWorldGrid);
        return new int[]{ screenX, screenY };
    }

    private int[] getActorRenderPositionXY(Actor actor){
        Position pos = actor.getComp("Position");
        Graphic gp = actor.getComp("Graphic");
        return getActorPositionScreenXY(pos, gp);
    }

    private int[] getActorRenderScreenXY(Position pos, Graphic gp){
        int screenX = (pos.getX() * bc.floorWorldGrid) + gp.getPivotX();
        int screenY = (pos.getY() * bc.floorWorldGrid) + gp.getPivotY();
        return new int[]{ screenX, screenY };
    }

    private int[] getActorRenderScreenXY(Actor actor){
        Position pos = actor.getComp("Position");
        Graphic gp = actor.getComp("Graphic");
        return getActorRenderScreenXY(pos, gp);
    }

    private void actorDraw(Actor actor, Graphics g){
        Position pos = actor.getComp("Position");
        ArrayList<Graphic> gps = actor.getComps("Graphic");
        if(pos == null || gps == null) return;

        gps.forEach( gp ->{
            if(gp.isVisible()){
                int[] sXY = getActorRenderScreenXY(pos, gp);
                resourceDraw(g, gp, sXY[0], sXY[1]);
            }
        });
    }

    private void resourceDraw(Graphics g, Graphic gp, int screenX, int screenY){
        Image image = gp.getImage();
        if(image != null){
            int width = gp.getWidth();
            int height = gp.getHeight();
            g.drawImage(image, screenX, screenY, width, height, null);

            if(isDrawRect){
                Graphics2D g2 = (Graphics2D) g;
                Stroke originalStroke = g2.getStroke();
                Color originalColor = g2.getColor();
                g2.setStroke(new BasicStroke(2));
                g2.setColor(Color.blue);
                g.drawRect(screenX, screenY, width, height);
                g2.setStroke(originalStroke);
                g2.setColor(originalColor);
            }

            if(isDrawPosition){
                Graphics2D g2 = (Graphics2D) g;
                Stroke originalStroke = g2.getStroke();
                Color originalColor = g2.getColor();
                g2.setStroke(new BasicStroke(5));
                g2.setColor(Color.RED);
                g.drawRect(screenX - gp.getPivotX(), screenY - gp.getPivotX(), 7, 7);
                //g.drawRect(screenX, screenY, width, height);
                g2.setStroke(originalStroke);
                g2.setColor(originalColor);
            }

            if(isDrawPivot){
                Graphics2D g2 = (Graphics2D) g;
                Stroke originalStroke = g2.getStroke();
                Color originalColor = g2.getColor();
                g2.setStroke(new BasicStroke(5));
                g2.setColor(Color.CYAN);
                g.drawRect(screenX, screenY, 7, 7);
                //g.drawRect(screenX, screenY, width, height);
                g2.setStroke(originalStroke);
                g2.setColor(originalColor);
            }
        }

        String drawText = gp.getDrawText();
        if(drawText != null && !drawText.isEmpty()){
            Font font = gp.getFont();
            if(font != null){
                g.setFont(font);
            }
            for (String line : drawText.split("\n"))
                g.drawString(line, screenX, screenY += g.getFontMetrics().getHeight());

        }
    }

}
