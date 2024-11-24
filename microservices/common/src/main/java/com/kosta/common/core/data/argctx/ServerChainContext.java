package com.kosta.common.core.data.argctx;

import com.kosta.common.core.data.argctx.abs.ArgsContext;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ServerChainContext implements ArgsContext {
    private String path;
    private String url;
    private int port;
    private String key;
    private String chainAdapter;
}
