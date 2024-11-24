package com.kosta.hcr.arc.abs;

import com.kosta.hcr.arc.data.Reply;

public interface ReplyServer {
    public Reply createReply();
    public Reply getReply(Long id);
    public Reply updateReply(Long id, Reply reply);
    public boolean deleteReply(Long id);
}
