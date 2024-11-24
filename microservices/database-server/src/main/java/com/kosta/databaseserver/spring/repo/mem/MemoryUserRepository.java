package com.kosta.databaseserver.spring.repo.mem;

import com.kosta.common.spring.data.vo.User;
import com.kosta.databaseserver.spring.repo.abs.sync.UserRepository;

import java.util.concurrent.ConcurrentHashMap;

public class MemoryUserRepository implements UserRepository {
    private static long SEQ = 0;
    private ConcurrentHashMap<Long, User> store = new ConcurrentHashMap<>();

    @Override
    public User create(User data) {
        User newUser = new User().copy(data);
        newUser.setId(SEQ);
        store.put(SEQ++, newUser);

        return newUser;
    }

    @Override
    public User read(Long id) {
        return store.get(id);
    }

    @Override
    public User update(Long id, User data) {
        User user = store.get(id).copy(data);
        return user;
    }

    @Override
    public Boolean delete(Long id) {
        store.remove(id);
        return true;
    }
}
