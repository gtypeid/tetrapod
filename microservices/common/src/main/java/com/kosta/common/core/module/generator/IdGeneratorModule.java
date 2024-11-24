package com.kosta.common.core.module.generator;

import com.kosta.common.CommandLineAppStartupRunner;
import com.kosta.common.core.data.argctx.IdGeneratorContext;
import com.kosta.common.core.module.generator.abs.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class IdGeneratorModule {

    private IdGeneratorContext ctx;
    private IdGenerator applicationGenerator;
    private final CommandLineAppStartupRunner commandLineAppStartupRunner;
    private List<IdGenerator> idGeneratorList = new ArrayList<>();

    @Autowired
    public IdGeneratorModule(CommandLineAppStartupRunner commandLineAppStartupRunner,
                            List<IdGenerator> idGeneratorList){
        this.commandLineAppStartupRunner = commandLineAppStartupRunner;
        this.idGeneratorList = idGeneratorList;
    }

    public Long getGenerateId(Long inDtoId){
        IdGeneratorContext ctx = getCtx();
        log.warn("Id Gen CTX: {}", ctx);
        if(ctx.getType().equals("none") || ctx.getType().isEmpty()){
            log.warn("In None");
            return inDtoId;
        }
        IdGenerator idGenerator = getApplicationGenerator(ctx);
        log.warn("Id Gen Type : {}", idGenerator);
        return idGenerator.generateId();
    }

    public IdGeneratorContext getCtx(){
        if(ctx == null){
            ctx = commandLineAppStartupRunner.getArgsConText(IdGeneratorContext.class);
            if(ctx == null){
                ctx = new IdGeneratorContext();
                ctx.setType("none");
            }
        }

        return ctx;
    }

    public IdGenerator getApplicationGenerator(IdGeneratorContext ctx){
        if(applicationGenerator == null){
            applicationGenerator = idGeneratorList.stream().filter( it->{
                return it.getGenerateType().equals(ctx.getType());
            }).findAny().get();
        }
        return applicationGenerator;
    }
}
