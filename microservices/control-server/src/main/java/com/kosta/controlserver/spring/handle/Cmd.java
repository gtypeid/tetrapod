package com.kosta.controlserver.spring.handle;

import com.kosta.common.core.data.ctx.CmdContext;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Cmd {
    private Map<String, Process> process = new HashMap<>();

    private final String TASK_KILL = "taskkill /PID %s /F";
    private final String NET_STAT = "netstat -aon | findstr :%s";

    public void run(String key, String command, Consumer<String> inputHandle, Consumer<String> errorHandle){
        ProcessBuilder processBuilder = new ProcessBuilder(
                "cmd.exe", "/c", command
        );

        try {
            Process ps = processBuilder.start();
            process.put(key, ps);

            new Thread(() -> readStream( ps.getInputStream(), inputHandle) ).start();
            new Thread(() -> readStream( ps.getErrorStream(), errorHandle) ).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exitProcess(CmdContext cmdContext) {
        checkPort(cmdContext.getPort(), (pid) -> {
            System.out.println(pid);
            String command = String.format(TASK_KILL, pid);
            run("kill", command, (read) -> {

            }, (error) -> {

            });
        });

        Process ps = process.get("server");
        if (ps != null && ps.isAlive()) {
            ps.destroy();

            try {
                ps.getInputStream().close();
                ps.getErrorStream().close();
                ps.getOutputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("IDEA process terminated.");
        } else {
            System.out.println("No running process to terminate.");
        }
    }

    private void checkPort(String port, Consumer<String> handle){
        String command = String.format(NET_STAT, port);
        run("port", command, (read)-> {
            if (!read.isEmpty()) {
                Pattern pattern = Pattern.compile("\\s+(\\d+)\\s*$", Pattern.MULTILINE);
                Matcher matcher = pattern.matcher(read);
                if(matcher.find()){
                    String pidStr = matcher.group(1);
                    handle.accept(pidStr);
                }
            }
        }, (error)->{

        });
    }

    private void readStream(InputStream stream, Consumer<String> cb) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                cb.accept(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
