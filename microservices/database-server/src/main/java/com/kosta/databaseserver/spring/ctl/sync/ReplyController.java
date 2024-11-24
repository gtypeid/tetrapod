package com.kosta.databaseserver.spring.ctl.sync;

import com.kosta.common.spring.data.dto.ReplyDTO;
import com.kosta.databaseserver.spring.repo.abs.sync.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reply")
@Profile("sync")
public class ReplyController {

    private final ReplyRepository replyRepository;

    @Autowired
    public ReplyController(ReplyRepository replyRepository){
        this.replyRepository = replyRepository;
    }

    @PostMapping("/create/{userId}/{boardId}")
    public ResponseEntity<ReplyDTO> createReply(@PathVariable("userId") Long userId,
                                                @PathVariable("boardId") Long boardId,
                                                @RequestBody ReplyDTO reply){
        return ResponseEntity.ok(
                replyRepository
                        .create(userId, boardId, reply.castInboundEntity())
                        .castOutboundDTO(0) );
    }

    @GetMapping("/{replyId}")
    public ResponseEntity<ReplyDTO> getReply(@PathVariable("replyId") long replyId){

        return ResponseEntity.ok(
                replyRepository
                        .read(replyId)
                        .castOutboundDTO(0) );
    }

    @PutMapping("/{replyId}")
    public ResponseEntity<ReplyDTO> updateReply(@PathVariable("replyId") long replyId,
                                           @RequestBody ReplyDTO reply){
        return ResponseEntity.ok(
                replyRepository
                        .update(replyId, reply.castInboundEntity())
                        .castOutboundDTO(0) );
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<Boolean> deleteReply(@PathVariable("replyId") long replyId){
        return ResponseEntity.ok( replyRepository.delete(replyId) );
    }
}
