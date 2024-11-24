package com.kosta.databaseserver.spring.ctl.async;

import com.kosta.common.spring.data.vo.Board;
import com.kosta.databaseserver.spring.repo.abs.async.AsyncBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/board")
@Profile("async")
public class AsyncBoardController {

    private final AsyncBoardRepository boardRepository;


    @Autowired
    public AsyncBoardController(AsyncBoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<Board>> createBoard(@RequestBody Board board){
        Mono<ResponseEntity<Board>> result = ( boardRepository.create(board) );

        return result;
    }

    @GetMapping("/{boardId}")
    public Mono<ResponseEntity<Board>> getBoard(@PathVariable("boardId") long boardId){
        return ( boardRepository.read(boardId) );
    }

    @PutMapping("/{boardId}")
    public Mono<ResponseEntity<Board>> updateBoard(@PathVariable("boardId") long boardId,
                                             @RequestBody Board board){
        return ( boardRepository.update(boardId, board) );
    }

    @DeleteMapping("/{boardId}")
    public Mono<ResponseEntity<Boolean>> deleteBoard(@PathVariable("boardId") long boardId){
        return ( boardRepository.delete(boardId) );
    }
}
