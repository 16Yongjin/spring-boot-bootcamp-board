package board.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ExceptionHandler {

  // 여기선 모든 에러 처리를 하지만 실제 프로젝트에서는 다양한 에러에 맞는 각각의 에러처리 필요
  @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
  public ModelAndView defaultExceptionHandler(HttpServletRequest request, Exception exception) {
    ModelAndView mv = new ModelAndView("/error/error_default");

    mv.addObject("exception", exception);

    log.error("defaultExceptionHandler", exception);

    // 이렇게 예외 로그를 화면에 노출시키면 절대 안됨!! 프로그램의 취약점이 드러나 공격받을 수 있음
    return mv;
  }
}
