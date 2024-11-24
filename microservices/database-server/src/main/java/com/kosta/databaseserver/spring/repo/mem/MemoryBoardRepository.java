package com.kosta.databaseserver.spring.repo.mem;

import com.kosta.common.spring.data.vo.Board;
import com.kosta.databaseserver.spring.repo.abs.sync.BoardRepository;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.ConcurrentHashMap;

public class MemoryBoardRepository implements BoardRepository {
    private static long SEQ = 0;
    private ConcurrentHashMap<Long, Board> store = new ConcurrentHashMap<>();

    public Board create(Long userId, Board data) {
        Board newBoard = new Board().copy(data);
        newBoard.setId(SEQ);
        store.put(SEQ++, newBoard);

        System.out.println("Repository " + newBoard);

        return newBoard;
    }


    @Override
    public Board read(Long id) {
        return store.get(id);
    }

    @Override
    public Board update(Long id, Board data) {
        Board board = store.get(id).copy(data);
        return board;
    }

    @Override
    public Boolean delete(Long id) {
        store.remove(id);
        return true;
    }
}
