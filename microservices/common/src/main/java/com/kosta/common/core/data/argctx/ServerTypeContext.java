package com.kosta.common.core.data.argctx;

import com.kosta.common.core.data.argctx.abs.ArgsContext;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ServerTypeContext implements ArgsContext {
    private String serverType;
    private String serverLabel;
    private Integer serverPort;
    private String serverAddress;
    private String gridX;
    private String gridY;

}
