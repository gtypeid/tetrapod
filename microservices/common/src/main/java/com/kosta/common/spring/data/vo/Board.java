package com.kosta.common.spring.data.vo;


import com.fasterxml.jackson.annotation.*;
import com.kosta.common.spring.data.vo.abs.VO;
import com.kosta.common.spring.data.dto.BoardDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "APP_BOARD")
@org.springframework.data.relational.core.mapping.Table(name = "APP_BOARD")
public class Board implements VO<Board> {
    public Board(){

    }
    @Id()
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "board_seq_gen")
    @SequenceGenerator(name = "board_seq_gen", sequenceName = "BOARD_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "COMMENTS")
    private String comments;

    @ManyToOne
    @JoinColumn(name = "BOARD_OWNER_USER")
    @JsonBackReference
    private User boardOwnerUser;

    @JsonManagedReference
    @OneToMany(mappedBy = "replyOwnerBoard")
    List<Reply> replies = new ArrayList<>();

    public void addReply(Reply reply){
        replies.add(reply);
        reply.setReplyOwnerBoard(this);
    }

    @Override
    public Board copy(Board vo) {
        title = vo.getTitle();
        comments = vo.getComments();
        return this;
    }

    @Override
    public BoardDTO castOutboundDTO(int type) {
        switch (type){
            case 1 :{

            }

            default: return new BoardDTO(id, title, comments, boardOwnerUser.getId());
        }
    }

}

//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "board_seq_gen")
//    @SequenceGenerator(name = "board_seq_gen", sequenceName = "BOARD_SEQ", allocationSize = 1)
//    @Column(name = "ID")
//    private long id;
