package com.kosta.common.core.data.argctx;

import com.kosta.common.core.data.argctx.abs.ArgsContext;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class IdGeneratorContext implements ArgsContext {
    private String type;   /* none, sequence, ticket, snowflake */
}

