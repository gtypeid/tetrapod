package com.kosta.common.core.module.chain.chainfilter.abs;

import com.kosta.common.core.data.argctx.ServerChainContext;
import jakarta.servlet.http.HttpServletRequest;

public interface ServerChainAdapter {

    boolean match(HttpServletRequest request, ServerChainContext chainContext);
}
