package com.kosta.hcr.arc.abs;

import com.kosta.hcr.arc.data.Board;

public interface BoardServer {
    public Board createBoard();
    public Board getBoard(Long id);
    public Board updateBoard(Long id, Board board);
    public boolean deleteBoard(Long id);
}
