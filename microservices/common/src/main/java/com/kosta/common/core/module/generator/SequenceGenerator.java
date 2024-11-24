package com.kosta.common.core.module.generator;

import com.kosta.common.core.module.generator.abs.IdGenerator;
import org.springframework.stereotype.Component;


@Component
public class SequenceGenerator implements IdGenerator {
    private Long seq = 0L;


    @Override
    public long generateId() {
        /*
        System.out.println(entityManager);
        Query query = entityManager.createNativeQuery("SELECT USER_SEQ.NEXTVAL FROM dual");
        Number number = (Number) query.getSingleResult();
        return number.longValue();
        */
        return seq++;
    }

    @Override
    public String getGenerateType() {
        return "sequence";
    }

}
