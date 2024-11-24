package com.kosta.common.core.data.argsparser;


import com.kosta.common.core.data.argctx.IdGeneratorContext;
import com.kosta.common.core.data.argctx.LoadBalancingContext;
import com.kosta.common.core.data.argsparser.abs.ArgsParser;
import org.springframework.stereotype.Component;


@Component
public class IdGeneratorArgsParser implements ArgsParser<IdGeneratorContext>{

    @Override
    public IdGeneratorContext casting(String value){
        IdGeneratorContext ctx = new IdGeneratorContext();
        String[] split = value.split(",");
        ctx.setType(split[0]);
        return ctx;
    }

    @Override
    public String getArgsType() {
        return "idgenerator.type";
    }
}
