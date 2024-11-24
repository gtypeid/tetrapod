package com.kosta.databaseserver.spring.repo.abs.sync;

import com.kosta.common.spring.data.vo.Board;
import org.springframework.http.ResponseEntity;

public interface BoardRepository {

    public Board create(Long userId, Board data);
    public Board read(Long id);
    public Board update(Long id, Board data);
    public Boolean delete(Long id);
}
