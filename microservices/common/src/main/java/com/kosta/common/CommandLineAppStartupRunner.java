package com.kosta.common;

import com.kosta.common.core.data.argctx.abs.ArgsContext;
import com.kosta.common.core.data.argsparser.abs.ArgsParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class CommandLineAppStartupRunner implements ApplicationRunner {

    private final List<ArgsParser<?>> argsParserList;
    private Map<Class<? extends ArgsContext>, ArgsContext> argsContextStore
            = new ConcurrentHashMap<>();
    public Runnable onRun;

    @Autowired
    public CommandLineAppStartupRunner(List<ArgsParser<?>> argsParserList){
        this.argsParserList = argsParserList;
        log.warn(this.argsParserList.toString());
    }

    @Override
    public void run(ApplicationArguments args) {

        // 옵션 인수와 비옵션 인수 접근하기
        Set<String> optionNames = args.getOptionNames();
        for (String optionName : optionNames) {
            List<String> optionValues = args.getOptionValues(optionName);
            log.warn("OPTION {} :: {}", optionName, optionValues.get(0));
        }

        if( validArgs(args) ){

        }
        else {
            log.warn("유효한 인자 값 찾지 못함");
        }

        argsContextStore.forEach( (key, value)->{
            log.warn(value.toString());
        });

        log.warn("CommandLine onRun ::");
        onRun.run();
    }

    public <T> T getArgsConText(Class<T> type){
        if(argsContextStore.containsKey(type)){
            return (T) argsContextStore.get(type);
        }
        return null;
    }

    private boolean validArgs(ApplicationArguments args){
        boolean isAnyFound = false;
        for (ArgsParser<?> parser : argsParserList){
            String arg = parser.getArgsType();
            boolean validKey = args.containsOption(arg);
            if(validKey){
                String value = args.getOptionValues(arg).get(0);
                ArgsContext ctx = parser.casting(value);
                argsContextStore.put(ctx.getClass(), ctx);
                isAnyFound = true;
            }
        }
        return isAnyFound;
    }
}