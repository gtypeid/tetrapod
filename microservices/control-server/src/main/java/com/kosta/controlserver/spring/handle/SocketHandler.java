package com.kosta.controlserver.spring.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosta.common.core.data.ctx.CmdContext;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;

public class SocketHandler extends TextWebSocketHandler {

    private final Cmd cmd;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String PATH = "C:\\IT\\workspace_java\\hcr\\microservices\\%s\\build\\libs\\%s-0.0.1-SNAPSHOT.jar";
    private final String PORT = "--server.port=%s";


    public SocketHandler(Cmd cmd){
        this.cmd = cmd;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connection established with session ID: " + session.getId());
        System.out.println( session.getLocalAddress().getAddress() );
        // Connection established logic here
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String msg = message.getPayload();
        CmdContext cmdContext = objectMapper.readValue(msg, CmdContext.class);
        System.out.println("Convert: " + cmdContext);
        StringBuilder strb = new StringBuilder();

        if(cmdContext.getCmd().equals("run")){
            String jarPath = String.format(PATH, cmdContext.getServer(), cmdContext.getServer());
            String arg = String.format(PORT, cmdContext.getPort());
            String strbArg = getParams(cmdContext, strb, arg);
            String runCommand = String.format("java -jar %s %s", jarPath, strbArg);

            System.out.println("runCommand: " + runCommand);

            cmd.run("server", runCommand, (read)-> {
                System.out.println(read);
                try {
                    session.sendMessage(new TextMessage(read));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, (error)->{

            } );
        }
        else if(cmdContext.getCmd().equals("exit")){
            cmd.exitProcess(cmdContext);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Connection closed with session ID: " + session.getId());
        // Connection closed logic here
    }

    public String getParams(CmdContext cmdContext, StringBuilder strb, String arg){
        strb.append(arg);
        for(Map<String, String> map : cmdContext.getParams()){
            map.forEach( (key, value)->{
                strb.append(" ").append(key).append(value);
            });
        }

        return strb.toString();
    }

}
