package com.kosta.common.spring.data.vo;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kosta.common.spring.data.vo.abs.VO;
import com.kosta.common.spring.data.dto.ReplyDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "APP_REPLY")
@org.springframework.data.relational.core.mapping.Table(name = "APP_REPLY")
public class Reply implements VO<Reply> {
    public Reply(){

    }
    @Id()
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reply_seq_gen")
    @SequenceGenerator(name = "reply_seq_gen", sequenceName = "REPLY_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "COMMENTS")
    private String comments;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "REPLY_OWNER_BOARD")
    private Board replyOwnerBoard;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "REPLY_OWNER_USER")
    private User replyOwnerUser;

    @Override
    public Reply copy(Reply vo) {
        comments = vo.getComments();
        return this;
    }

    @Override
    public ReplyDTO castOutboundDTO(int type) {
        switch (type){
            case 1 :{

            }

            default: return new ReplyDTO(id, comments, replyOwnerBoard.getId(), replyOwnerUser.getId());
        }
    }

}

//    @Id()
//    @Column(name = "ID")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reply_seq_gen")
//    @SequenceGenerator(name = "reply_seq_gen", sequenceName = "REPLY_SEQ", allocationSize = 1)
//    private long id;
