package com.kosta.hcr.arc.abs;

import com.kosta.hcr.arc.data.User;

public interface UserServer {
    public User createUser();
    public User getUser(long id);
}
