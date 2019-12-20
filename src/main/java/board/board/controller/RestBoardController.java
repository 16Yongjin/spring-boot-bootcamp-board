package board.board.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import board.board.dto.BoardDto;
import board.board.dto.BoardFileDto;
import board.board.service.BoardService;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RestBoardController {
  @Autowired
  private BoardService boardService;

  @GetMapping("/board")
  public ModelAndView openBoardList() throws Exception {
    ModelAndView mv = new ModelAndView("/board/restBoardList");

    List<BoardDto> list = boardService.selectBoardList();
    mv.addObject("list", list);

    return mv;
  }

  @GetMapping("/board/write")
  public String openBoardWrite() throws Exception {
    return "/board/restBoardWrite";
  }

  @PostMapping("/board/write")
  public String insertBoard(BoardDto board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
    boardService.insertBoard(board, multipartHttpServletRequest);
    return "redirect:/board";
  }

  @GetMapping("/board/{boardIdx}")
  public ModelAndView openBoardDetail(@PathVariable("boardIdx") int boardIdx) throws Exception {
    ModelAndView mv = new ModelAndView("/board/restBoardDetail");

    BoardDto board = boardService.selectBoardDetail(boardIdx);
    mv.addObject("board", board);

    return mv;
  }

  @PutMapping("/board/{boardIdx}")
  public String updateBoard(BoardDto board) throws Exception {
    boardService.updateBoard(board);
    return "redirect:/board/openBoardList.do";
  }

  @DeleteMapping("/board/{boardIdx}")
  public String deleteBoard(@PathVariable("boardIdx") int boardIdx) throws Exception {
    boardService.deleteBoard(boardIdx);
    return "redirect:/board";
  }

  @GetMapping("/board/file")
  public void downloadBoardFile(@RequestParam int idx, @RequestParam int boardIdx, HttpServletResponse response)
      throws Exception {
    BoardFileDto boardFile = boardService.selectBoardFileInformation(idx, boardIdx);

    if (ObjectUtils.isEmpty(boardFile) == false) {
      String fileName = boardFile.getOriginalFileName();

      // 파일을 읽어서 바이내리 데이터로 만들기
      byte[] files = FileUtils.readFileToByteArray(new File(boardFile.getStoredFilePath()));

      // 헤더 작성
      response.setContentType("application/octet-stream");
      response.setContentLength(files.length);
      response.setHeader("Content-Disposition",
          "attachment; fileName=\"" + URLEncoder.encode(fileName, "UTF-8") + "\";");
      response.setHeader("Content-Transfer-Encoding", "binary");

      // 데이터를 쓰고 버퍼 정리 후 닫기
      response.getOutputStream().write(files);
      response.getOutputStream().flush();
      response.getOutputStream().close();
    }
  }
}