package com.kosta.common.core.data.argsparser.abs;

import com.kosta.common.core.data.argctx.abs.ArgsContext;

import java.util.List;

public interface ArgsParser <T extends ArgsContext> {
    T casting(String value);
    String getArgsType();
}
