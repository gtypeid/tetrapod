package com.kosta.common.spring.data.vo;


import com.fasterxml.jackson.annotation.*;
import com.kosta.common.spring.data.vo.abs.VO;
import com.kosta.common.spring.data.dto.UserDTO;
import com.kosta.common.spring.data.vo.abs.VOCopy;
import com.kosta.rb.def.Util;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
@ToString
@Entity
@Table(name = "APP_USER")
@org.springframework.data.relational.core.mapping.Table(name ="APP_USER")
public class User extends VOCopy<User> {

    public User(){

    }

    @Column(name = "NAME")
    private String name;

    @OneToMany(mappedBy = "boardOwnerUser")
    @JsonManagedReference
    List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "replyOwnerUser")
    @JsonManagedReference
    List<Reply> replies = new ArrayList<>();

    public void addBoard(Board board){
        boards.add(board);
        board.setBoardOwnerUser(this);
    }

    public void addReply(Reply reply){
        replies.add(reply);
        reply.setReplyOwnerUser(this);
    }

    @Override
    public User copy(User vo) {
        id = vo.getId();
        name = vo.getName();
        return this;
    }

    @Override
    public UserDTO castOutboundDTO(int type) {
        switch (type){
            case 1 : {
            }

            default: return new UserDTO(id, name);
        }
    }

}

//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_gen")
//@SequenceGenerator(name = "user_seq_gen", sequenceName = "USER_SEQ", allocationSize = 1)

// @JsonIgnoreProperties({"boards", "replies"})
// @OneToMany(mappedBy = "boardOwnerUser", cascade = CascadeType.ALL, orphanRemoval = true)