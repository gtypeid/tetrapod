package com.kosta.common.spring.data.vo.abs;

import com.kosta.common.spring.data.dto.abs.DTO;
import com.kosta.common.spring.data.VoEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
@EntityListeners(VoEntityListener.class)
public abstract class VOCopy<T>{


    @Id()
    @Column(name = "ID")
    protected Long id;

    abstract public T copy(T vo);
    abstract public DTO<T> castOutboundDTO(int type);

    public Long getId() {
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }

}
