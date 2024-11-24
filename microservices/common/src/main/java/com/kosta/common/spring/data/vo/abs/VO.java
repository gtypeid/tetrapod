package com.kosta.common.spring.data.vo.abs;

import com.kosta.common.spring.data.dto.abs.DTO;

public interface VO<T>{

    public T copy(T vo);
    public DTO<T> castOutboundDTO(int type);

}
