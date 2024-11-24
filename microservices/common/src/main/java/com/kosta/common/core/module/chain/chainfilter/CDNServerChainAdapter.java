package com.kosta.common.core.module.chain.chainfilter;

import com.kosta.common.core.data.argctx.ServerChainContext;
import com.kosta.common.core.module.chain.chainfilter.abs.ServerChainAdapter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
public class CDNServerChainAdapter implements ServerChainAdapter {

    @Override
    public boolean match(HttpServletRequest request, ServerChainContext serverChainContext) {
        boolean isMatch = checkCDN(request);
        return isMatch;
    }

    private boolean checkCDN(HttpServletRequest httpRequest){
        boolean flag = false;
        String acceptHeader = httpRequest.getHeader("Accept");
        if (acceptHeader != null) {
            if (acceptHeader.contains("text/html")) {
                log.warn("Accept Type : HTML : {}" , httpRequest.getRequestURI());
                flag = true;
            } else if (acceptHeader.contains("text/css")) {
                log.warn("Accept Type : CSS : {}" , httpRequest.getRequestURI());
                flag = true;
            } else if (acceptHeader.contains("image/")) {
                log.warn("Accept Type : IMG : {}" , httpRequest.getRequestURI());
                flag = true;
            }
            else if (isJsURI( httpRequest.getRequestURI() )
                    || acceptHeader.contains("application/javascript")
                    || acceptHeader.contains("text/javascript")) {
                log.warn("Accept Type : JS : {}" , httpRequest.getRequestURI());
                flag = true;
            }
        }

        return flag;
    }

    private boolean isJsURI(String uri){
        return uri.endsWith(".js");
    }
}
