package com.example.demo.common.interceptor;

import com.example.demo.common.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

//컨트롤러에 도착하기 전에 가로채서 로그인을 검사함
//인터셉터 작성을 안 하면 모든 컨트롤러 앞에 로그인 체크 검사 코드를 작성해야함
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{

        HttpSession session = request.getSession(false); //false -> 세션이 없어도 새로 만들지 말라는 뜻, true-> 비로그인 방문자마다 빈 세션이 생성돼 서버 메모리를 낭비한다

        if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            String requestURI = request.getRequestURI();
            response.sendRedirect("/members/login?redirectURL=" + requestURI); //원래 가려던 주소 기억
            return false; //컨트롤러로 진행 중단
        }

        return true; //검사 통과 -> 컨트롤러 실행
    }
}
