package com.kosta.common.spring.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kosta.common.spring.data.dto.abs.DTO;
import com.kosta.common.spring.data.vo.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO extends DTO<User> {

    private String name;

    public UserDTO() {
    }

    public UserDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }


    @Override
    public User castInboundEntity() {
        User user = new User();
        user.setId(id);
        user.setName(name);
        return user;
    }
}