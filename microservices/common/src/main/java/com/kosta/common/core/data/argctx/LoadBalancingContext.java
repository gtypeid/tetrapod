package com.kosta.common.core.data.argctx;

import com.kosta.common.core.data.argctx.abs.ArgsContext;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoadBalancingContext implements ArgsContext {
    private String balancingType;   /* none, random, roundrobin, sharding */
    private String statusCheckLayer; /* none, layer4, layer7 */
}
