package com.kosta.common.core.data.ctx;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class CmdContext {
    private String cmd;
    private String server;
    private String port;
    private List<Map<String, String >> params;
    public CmdContext(){

    }
}


