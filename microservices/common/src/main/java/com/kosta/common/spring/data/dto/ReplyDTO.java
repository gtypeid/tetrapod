package com.kosta.common.spring.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kosta.common.spring.data.dto.abs.DTO;
import com.kosta.common.spring.data.vo.Reply;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReplyDTO extends DTO<Reply> {

    private String comments;
    private Long replyOwnerBoardId;
    private Long replyOwnerUserId;

    public ReplyDTO() {
    }

    public ReplyDTO(long id, String comments, Long replyOwnerBoardId, Long replyOwnerUserId) {
        this.id = id;
        this.comments = comments;
        this.replyOwnerBoardId = replyOwnerBoardId;
        this.replyOwnerUserId = replyOwnerUserId;
    }

    @Override
    public Reply castInboundEntity() {
        Reply reply = new Reply();
        reply.setId(id);
        reply.setComments(comments);
        return reply;
    }
}