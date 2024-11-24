package com.kosta.rb.core;

import com.kosta.rb.actor.dice.Node;
//import com.kosta.winter.core.Http;
//import com.kosta.winter.def.HttpRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class Mode {

    public static final String HOST = "host";
    public static final String CLIENT = "client";
    private final String owner;

    protected Board board;
    protected Store store;
    //protected Http http;
    //protected HttpRequest httpRequest;

    public Mode(String type){
        owner = type;
    }

    public String getOwner(){
        return owner;
    }

    public boolean isHost(){
        return (owner.equals(HOST));
    }

    public void startClientListening(){
        int timeRate = 500;

        board = Board.getInstance();
        //http = board.getHttp();
        store = board.getStore();

//        httpRequest = new HttpRequest();
//        httpRequest.setUrl("http://localhost:8081/flow");
//        httpRequest.setMethod(Http.GET);

        if(!isHost()){
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
//                    request(httpRequest);
                }
            }, 0, timeRate);
        }
    }

//    private void request(HttpRequest httpRequest){
//        http.doRequest(httpRequest, (statusCode, response) -> {
//            JSONObject responseData = new JSONObject(response);
//            String code = responseData.get("statusCode").toString();
//            if(code.equals("200")){
//                JSONObject json = new JSONObject( responseData.get("data").toString() );
//                Node node = store.getMainNode();
//                JSONArray playerNodesIndexs = json.getJSONArray("playerNodesIndexs");
//                for(int i = 0; i < playerNodesIndexs.length(); ++i){
//                    int nodeIndex = playerNodesIndexs.getInt(i);
//                    node.posFixedSet(i, nodeIndex);
//                }
//            }
//        });
//    }
}
