package board.board.controller;

import java.util.List;

import board.board.dto.BoardDto;
import board.board.service.BoardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestBoardApiController {
  @Autowired
  private BoardService boardService;

  @GetMapping("/api/board")
  public List<BoardDto> openBoardList() throws Exception {
    return boardService.selectBoardList();
  }

  @PostMapping("/api/board/write")
  public void insertBoard(@RequestBody BoardDto board) throws Exception {
    boardService.insertBoard(board, null);
  }

  @GetMapping("/api/board/{boardIdx}")
  public BoardDto openBoardDetail(@PathVariable("boardIdx") int boardIdx) throws Exception {
    return boardService.selectBoardDetail(boardIdx);
  }

  @PutMapping("/api/board/{boardIdx}")
  public String updateBoard(@RequestBody BoardDto board) throws Exception {
    boardService.updateBoard(board);
    return "redirect:/board";
  }

  @DeleteMapping("/api/board/{boardIdx}")
  public String deleteBoard(@PathVariable("boardIdx") int boardIdx) throws Exception {
    boardService.deleteBoard(boardIdx);
    return "redirect:/board";
  }
}