package com.kosta.databaseserver.spring.ctl.sync;

import com.kosta.common.spring.data.dto.BoardDTO;
import com.kosta.databaseserver.spring.repo.abs.sync.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@Profile("sync")
public class BoardController {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardController(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<BoardDTO> createBoard(@PathVariable("userId") long userId,
                                                @RequestBody BoardDTO board){
        return ResponseEntity.ok(
                boardRepository
                        .create(userId, board.castInboundEntity())
                        .castOutboundDTO(0) );
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDTO> getBoard(@PathVariable("boardId") long boardId){
        return ResponseEntity.ok(
                boardRepository
                        .read(boardId)
                        .castOutboundDTO(0) );
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<BoardDTO> updateBoard(@PathVariable("boardId") long boardId,
                                             @RequestBody BoardDTO board){
        return ResponseEntity.ok(
                boardRepository
                        .update(boardId, board.castInboundEntity())
                        .castOutboundDTO(0) );
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Boolean> deleteBoard(@PathVariable("boardId") long boardId){
        return ResponseEntity.ok( boardRepository.delete(boardId) );
    }
}


/*
        if (dataSource instanceof HikariDataSource) {
            HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
            HikariPoolMXBean poolMXBean = hikariDataSource.getHikariPoolMXBean();

            int activeConnections = poolMXBean.getActiveConnections();
            int idleConnections = poolMXBean.getIdleConnections();
            int totalConnections = poolMXBean.getTotalConnections();
            int awaiting = poolMXBean.getThreadsAwaitingConnection();
            String con = "Active connections: " + activeConnections +
                    ", Idle connections: " + idleConnections +
                    ", Total connections: " + totalConnections +
                    ", Queued threads: " + awaiting;
            System.out.println(con);
        }
 */