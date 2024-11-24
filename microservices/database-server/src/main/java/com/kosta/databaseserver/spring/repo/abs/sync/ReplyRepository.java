package com.kosta.databaseserver.spring.repo.abs.sync;

import com.kosta.common.spring.data.vo.Reply;
import org.springframework.http.ResponseEntity;

public interface ReplyRepository {

    public Reply create(Long userId, Long boardId, Reply data);
    public Reply read(Long id);
    public Reply update(Long id, Reply data);
    public Boolean delete(Long id);
}
