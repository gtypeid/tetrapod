package com.kosta.hcr;

import com.kosta.common.spring.data.vo.Board;
import com.kosta.databaseserver.DatabaseAppConfig;
import com.kosta.databaseserver.DatabaseServerApplication;
import com.kosta.databaseserver.spring.repo.abs.sync.BoardRepository;
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
public class BoardCrudTest {

    @Autowired
    BoardRepository boardRepository;

    List<Board> boards;
    String[] boardTitles;
    String[] boardComments;
    Map<String, Long> boardIdMap;

   @BeforeEach
    public void setUp() {
        boardTitles = new String[]{
                "tester board0",
                "tester board1",
                "tester board2"
        };

        boardComments = new String[]{
                "tester board comments 0",
                "tester board comments 1",
                "tester board comments 2"
        };

        boards = new ArrayList<>();
        boardIdMap = new HashMap<>();
        for (int i = 0; i < boardTitles.length; ++i) {
            Board board = new Board();
            board.setTitle(boardTitles[i]);
            board.setComments(boardComments[i]);
            boards.add(board);
        }

        for(int i = 0; i < boardTitles.length; ++i){
            Board createdBoard = boardRepository.create(0L, boards.get(i));
            boardIdMap.put(createdBoard.getTitle(), createdBoard.getId());
        }
    }

    @Test
    public void createBoard(){

        for(int i = 0; i < boardTitles.length; ++i){
            Board createdBoard = boardRepository.create(0L, boards.get(i));

            assertNotNull(createdBoard, "Board Not Null");
            assertEquals(boardTitles[i], createdBoard.getTitle(), "Title should match");
            assertEquals(boardComments[i], createdBoard.getComments(), "Comments should match");
        }
    }

    @Test
    public void readBoard(){
        for(int i = 0; i < boardTitles.length; ++i){
            Long boardId = boardIdMap.get(boardTitles[i]);

            Board board = boardRepository.read(boardId);

            assertNotNull(board, "Board Not Null");
            assertEquals(boardTitles[i], board.getTitle(), "Title should match");
            assertEquals(boardComments[i], board.getComments(), "Comments should match");
        }
    }

    @Test
    public void updateBoard(){
        for(int i = 0; i < boardTitles.length; ++i){
            Long boardId = boardIdMap.get(boardTitles[i]);
            Board newBoard = new Board();
            newBoard.setTitle("update title");
            newBoard.setComments("update comments");

            Board board = boardRepository.update(boardId, newBoard);

            assertNotNull(board, "Board Not Null");
            assertNotEquals(boardTitles[i], board.getTitle(), "Title should match");
            assertNotEquals(boardComments[i], board.getComments(), "not match");
        }
    }

    @Test
    public void deleteUser(){
        for(int i = 0; i < boardTitles.length; ++i){
            Long boardId = boardIdMap.get(boardTitles[i]);
            boolean isDelete = boardRepository.delete(boardId);
            assertTrue(isDelete);
        }

    }
}

