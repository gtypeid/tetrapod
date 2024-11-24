package com.kosta.databaseserver.spring.repo.jpa;

import com.kosta.common.spring.data.vo.Board;
import com.kosta.common.spring.data.vo.Reply;
import com.kosta.common.spring.data.vo.User;
import com.kosta.databaseserver.spring.repo.abs.sync.BoardRepository;
import com.kosta.databaseserver.spring.repo.abs.sync.ReplyRepository;
import com.kosta.databaseserver.spring.repo.abs.jpa.JPAReply;
import com.kosta.databaseserver.spring.repo.abs.sync.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public class JpaReplyRepository implements ReplyRepository {

    @Autowired
    private JPAReply jpaReply;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    public Reply create(Long userId, Long boardId, Reply data) {
        User user = userRepository.read(userId);
        Board board = boardRepository.read(boardId);
        Reply newReply = new Reply().copy(data);

        user.addReply(newReply);
        board.addReply(newReply);

        return jpaReply.save(newReply);
    }

    @Override
    public Reply read(Long id) {
        return jpaReply.findById(id).get();
    }

    @Override
    public Reply update(Long id, Reply data) {
        Reply reply = jpaReply.findById(id).get().copy(data);
        return jpaReply.save(reply);
    }

    @Override
    public Boolean delete(Long id) {
        jpaReply.deleteById(id);
        return true;
    }
}
