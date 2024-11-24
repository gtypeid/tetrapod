package com.kosta.rb.actor.metrics.dynamicflow;

import com.kosta.rb.actor.metrics.dynamicflow.abs.UniqueDynamicFlowContext;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrafficFlowContext extends UniqueDynamicFlowContext {
    private PtrComputer startPtrComputer;
    private PtrComputer endPtrComputer;
    private String uri;
    private String method;
    private String body;
    private String reqs;
    private String date;

    /*
    private String clientIP;
    private String startDir;
    private String endDir;
    private String type;
    private String json;
    */

    @Override
    public String stateType(){
        return "RInsertTraffic";
    }

    public static String genDate(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return now.format(formatter);
    }

    public static Long msDuration(String genDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime parsedDateTime = LocalDateTime.parse(genDate, formatter);

        Duration duration = Duration.between(parsedDateTime, LocalDateTime.now());
        return duration.toMillis();
    }
}
