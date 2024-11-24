package com.kosta.databaseserver.spring.repo.mem;

import com.kosta.common.spring.data.vo.Reply;
import com.kosta.databaseserver.spring.repo.abs.sync.ReplyRepository;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.ConcurrentHashMap;

public class MemoryReplyRepository implements ReplyRepository {
    private static long SEQ = 0;
    private ConcurrentHashMap<Long, Reply> store = new ConcurrentHashMap<>();

    public Reply create(Long userId, Long boardId, Reply data) {
        Reply newReply = new Reply().copy(data);
        newReply.setId(SEQ);
        store.put(SEQ++, newReply);

        return newReply;
    }

    @Override
    public Reply read(Long id) {
        return store.get(id);
    }

    @Override
    public Reply update(Long id, Reply data) {
        Reply reply = store.get(id).copy(data);
        return reply;
    }

    @Override
    public Boolean delete(Long id) {
        store.remove(id);
        return true;
    }
}
