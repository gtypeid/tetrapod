package com.kosta.common.core.module.chain.chainfilter;

import com.kosta.common.core.data.argctx.ServerChainContext;
import com.kosta.common.core.module.chain.chainfilter.abs.ServerChainAdapter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AcceptLanguageUSAdapter implements ServerChainAdapter {

    @Override
    public boolean match(HttpServletRequest request, ServerChainContext serverChainContext) {
        return checkLanguage(request);
    }

    private boolean checkLanguage(HttpServletRequest httpRequest){
        boolean flag = false;
        String acceptHeader = httpRequest.getHeader("Accept-Language");
        if (acceptHeader != null) {
            flag = acceptHeader.equals("en-US");
        }
        return flag;
    }

}
