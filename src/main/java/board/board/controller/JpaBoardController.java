package board.board.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import board.board.entity.BoardEntity;
import board.board.entity.BoardFileEntity;
import board.board.service.JpaBoardService;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
public class JpaBoardController {
  @Autowired
  private JpaBoardService jpaBoardService;

  @GetMapping("/jpa/board")
  public ModelAndView openBoardList(ModelMap model) throws Exception {
    ModelAndView mv = new ModelAndView("/board/jpaBoardList");

    List<BoardEntity> list = jpaBoardService.selectBoardList();
    mv.addObject("list", list);

    return mv;
  }

  @GetMapping("/jpa/board/write")
  public String openBoardWrite() throws Exception {
    return "/board/jpaBoardWrite";
  }

  @PostMapping("/jpa/board/write")
  public String insertBoard(BoardEntity board, MultipartHttpServletRequest multipartHttpServletRequest)
      throws Exception {
    jpaBoardService.saveBoard(board, multipartHttpServletRequest);
    return "redirect:/jpa/board";
  }

  @GetMapping("/jpa/board/{boardIdx}")
  public ModelAndView openBoardDetail(@PathVariable("boardIdx") int boardIdx) throws Exception {
    ModelAndView mv = new ModelAndView("/board/jpaBoardDetail");

    BoardEntity board = jpaBoardService.selectBoardDetail(boardIdx);
    mv.addObject("board", board);

    return mv;
  }

  @PutMapping("/jpa/board/{boardIdx}")
  public String updateBoard(BoardEntity board) throws Exception {
    jpaBoardService.saveBoard(board, null);
    return "redirect:/jpa/board";
  }

  @DeleteMapping("/jpa/board/{boardIdx}")
  public String deleteBoard(@PathVariable("boardIdx") int boardIdx) throws Exception {
    jpaBoardService.deleteBoard(boardIdx);
    return "redirect:/jpa/board";
  }

  @GetMapping("/jpa/board/file")
  public void downloadBoardFile(@RequestParam int boardIdx, @RequestParam int idx, HttpServletResponse response)
      throws Exception {
    BoardFileEntity boardFile = jpaBoardService.selectBoardFileInformation(boardIdx, idx);

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