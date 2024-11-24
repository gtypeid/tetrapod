package com.kosta.common.core.module.generator;

import com.kosta.common.core.module.generator.abs.IdGenerator;
import org.springframework.stereotype.Component;


@Component
public class TicketServerGenerator implements IdGenerator {
    private Long seq = 0L;

    @Override
    public long generateId() {
        return seq++;
    }

    @Override
    public String getGenerateType() {
        return "ticket";
    }
}
