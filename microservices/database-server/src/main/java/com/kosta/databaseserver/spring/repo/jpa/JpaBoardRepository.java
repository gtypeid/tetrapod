package com.kosta.databaseserver.spring.repo.jpa;

import com.kosta.common.spring.data.vo.Board;
import com.kosta.common.spring.data.vo.User;
import com.kosta.databaseserver.spring.repo.abs.sync.BoardRepository;
import com.kosta.databaseserver.spring.repo.abs.jpa.JPABoard;
import com.kosta.databaseserver.spring.repo.abs.sync.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public class JpaBoardRepository implements BoardRepository {

    @Autowired
    private JPABoard jpaBoard;

    @Autowired
    private UserRepository userRepository;


    public Board create(Long userId, Board data) {
        User user = userRepository.read(userId);
        Board newBoard = new Board().copy(data);
        user.addBoard(newBoard);

        return jpaBoard.save(newBoard);
    }


    @Override
    public Board read(Long id) {
        return jpaBoard.findById(id).get();
    }

    @Override
    public Board update(Long id, Board data) {
        Board board = jpaBoard.findById(id).get().copy(data);
        return jpaBoard.save(board);
    }

    @Override
    public Boolean delete(Long id) {
        jpaBoard.deleteById(id);
        return true;
    }
}
