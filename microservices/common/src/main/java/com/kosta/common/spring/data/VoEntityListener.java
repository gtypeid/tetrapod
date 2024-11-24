package com.kosta.common.spring.data;

import org.springframework.stereotype.Component;

@Component
public class VoEntityListener {

    // JPA Listner는
    // 일반 생명주기하고 다름 그러니 외부에서 의존성 주입 및
    // 요구하는 생성자 호출 안되고 기본 생성자만 호출 됨
    // 또한 스프링들의 빈이 등록되기 전에 먼저 호출되기에 적합한 Autowire 처리 불가
    public VoEntityListener(){

    }

    /*
    @PrePersist
    public void prePersist(VO<?> vo) {
        if (vo.getId() == null) {
            System.out.println("VO ENTITIY prePersist");
            System.out.println(this);
            //System.out.println(this.idGenerator);
            //vo.setId(idGenerator.generateId());
        }
    }
    */
}
