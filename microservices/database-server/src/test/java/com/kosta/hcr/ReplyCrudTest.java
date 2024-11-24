package com.kosta.hcr;

import com.kosta.common.spring.data.vo.Reply;
import com.kosta.databaseserver.DatabaseAppConfig;
import com.kosta.databaseserver.DatabaseServerApplication;
import com.kosta.databaseserver.spring.repo.abs.sync.ReplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig
@Import(DatabaseAppConfig.class)
@SpringBootTest(classes = DatabaseServerApplication.class)
public class ReplyCrudTest {

    @Autowired
    ReplyRepository replyRepository;

    List<Reply> replies;
    String[] comments;
    Map<String, Long> repliesIdMap;

    @BeforeEach
    public void setUp() {
        comments = new String[]{
                "tester comments0",
                "tester comments1",
                "tester comments2"
        };

        replies = new ArrayList<>();
        repliesIdMap = new HashMap<>();
        for (String comment : comments) {
            Reply reply = new Reply();
            reply.setComments(comment);
            replies.add(reply);
        }

        for(int i = 0; i < comments.length; ++i){
            Reply createdReply = replyRepository.create(0L, 0L, replies.get(i));
            repliesIdMap.put(createdReply.getComments(), createdReply.getId());
        }
    }

    @Test
    public void createReply(){

        for(int i = 0; i < comments.length; ++i){
            Reply createdReply = replyRepository.create(0L, 0L, replies.get(i));

            assertNotNull(createdReply, "Not Null");
            assertEquals(comments[i], createdReply.getComments(), "should match");
        }
    }

    @Test
    public void readReply(){
        for (String comment : comments) {
            Long replyId = repliesIdMap.get(comment);
            Reply readReply = replyRepository.read(replyId);

            assertNotNull(readReply, "Not Null");
            assertEquals(comment, readReply.getComments(), "should match");
        }
    }

    @Test
    public void updateReply(){
        for (String comment : comments) {
            Long replyId = repliesIdMap.get(comment);
            Reply newReply = new Reply();
            newReply.setComments("updateComment");

            Reply updateReply = replyRepository.update(replyId, newReply);

            assertNotNull(updateReply, "Not Null");
            assertNotEquals(comment, updateReply.getComments(), "should not match");
        }
    }

    @Test
    public void deleteUser(){
        for (String comment : comments) {
            Long replyId = repliesIdMap.get(comment);
            boolean isDelete = replyRepository.delete(replyId);
            assertTrue(isDelete);
        }
    }

}

