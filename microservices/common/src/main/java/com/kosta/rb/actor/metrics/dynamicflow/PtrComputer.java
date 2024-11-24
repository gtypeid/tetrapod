package com.kosta.rb.actor.metrics.dynamicflow;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class PtrComputer {

    public PtrComputer(){

    }

    private String ip;
    private String type;
    private String label;
    private String port;
}
