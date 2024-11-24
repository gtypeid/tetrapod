package com.kosta.databaseserver;
import com.kosta.databaseserver.spring.repo.abs.async.AsyncBoardRepository;
import com.kosta.databaseserver.spring.repo.abs.async.AsyncReplyRepository;
import com.kosta.databaseserver.spring.repo.abs.async.AsyncUserRepository;
import com.kosta.databaseserver.spring.repo.abs.sync.UserRepository;
import com.kosta.databaseserver.spring.repo.jpa.JpaBoardRepository;
import com.kosta.databaseserver.spring.repo.jpa.JpaReplyRepository;
import com.kosta.databaseserver.spring.repo.jpa.JpaUserRepository;
import com.kosta.databaseserver.spring.repo.abs.sync.BoardRepository;
import com.kosta.databaseserver.spring.repo.abs.sync.ReplyRepository;
import com.kosta.databaseserver.spring.repo.postgr.PostgrBoardRepository;
import com.kosta.databaseserver.spring.repo.postgr.PostgrReplyRepository;
import com.kosta.databaseserver.spring.repo.postgr.PostgrUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseAppConfig {


    @Bean
    public UserRepository userRepository() {
        //return new MemoryUserRepository();
        return new JpaUserRepository();
    }

    @Bean
    public BoardRepository boardRepository() {
        //return new MemoryBoardRepository();
        return new JpaBoardRepository();
    }

    @Bean
    public ReplyRepository replyRepository() {
        //return new MemoryReplyRepository();
        return new JpaReplyRepository();
    }

    @Bean
    public AsyncUserRepository asyncUserRepository() {
        return new PostgrUserRepository();
    }

    @Bean
    public AsyncBoardRepository asyncBoardRepository() {
        return new PostgrBoardRepository();
    }

    @Bean
    public AsyncReplyRepository asyncReplyRepository() {
        return new PostgrReplyRepository();
    }
}
