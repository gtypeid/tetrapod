package com.kosta.common.spring.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kosta.common.spring.data.dto.abs.DTO;
import com.kosta.common.spring.data.vo.Board;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardDTO extends DTO<Board> {

    private String title;
    private String comments;
    private Long boardOwnerUserId;

    public BoardDTO() {

    }

    public BoardDTO(long id, String title, String comments, Long boardOwnerUserId) {
        this.id = id;
        this.title = title;
        this.comments = comments;
        this.boardOwnerUserId = boardOwnerUserId;
    }

    @Override
    public Board castInboundEntity() {
        Board board = new Board();
        board.setId(id);
        board.setTitle(title);
        board.setComments(comments);
        return board;
    }
}