package com.kosta.databaseserver.spring.repo.abs.sync;

import com.kosta.common.spring.data.vo.User;

public interface UserRepository {

    public User create(User data);
    public User read(Long id);
    public User update(Long id, User data);
    public Boolean delete(Long id);
}
