package com.kosta.common.spring.data.dto.abs;

public abstract class DTO<T> {
    abstract public T castInboundEntity();
    protected Long id;

    public void setId(Long id){
        this.id = id;

    }
    public Long getId(){
        return this.id;
    }
}
