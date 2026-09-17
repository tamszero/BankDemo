package com.example.demo.common.exception;

//컨트롤러에서 못 잡은 BuisinessException을 여기서 처리
// ->  500 대신 제대로 된 상태코드/메시지를 받기 위해

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /** 예상된 비즈니스 예외 **/
    @ExceptionHandler(BuisinessException.class)
    public ModelAndView handleBusinessException(BuisinessException e,
                                                 HttpServletRequest request){
        ErrorCode errorCode = e.getErrorCode();

        // 스택트레이스 없이 한 줄로 - 버그가 아니라 정상적인 거부
        log.warn("[BusinessExcption] code={}, message={}, uri={}",
                errorCode.getCode(),errorCode.getMessage(),request.getRequestURI());

        ModelAndView mav = new ModelAndView("error/buisiness");
        mav.addObject("code", errorCode.getCode());
        mav.addObject("message", errorCode.getMessage());
        mav.setStatus(errorCode.getStatus());

        return mav;
    }

    /** 예상 못 한 모든 예외 **/
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e,
                                        HttpServletRequest request){

        // 스택트레이스 전체를 로그에 - 개발자 확인용
        log.error("[UnexpectedException] uir={}" , request.getRequestURI(), e);

        ModelAndView mv = new ModelAndView("error/500");
        // e.getMessage()를 화면에 넘기지 못하게함
        mv.addObject("message", "일시적 오류가 발생하였습니다. 잠시 후 다시 시도해주세요.");
        return mv;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleNotFount(NoHandlerFoundException e){

        log.warn("[404] uri={}", e.getRequestURL());
        ModelAndView mv = new ModelAndView("error/404");
        mv.setStatus(HttpStatus.NOT_FOUND);

        return mv;
    }
}
