package com.example.demo.member.controller;

import com.example.demo.common.exception.BuisinessException;
import com.example.demo.common.session.SessionConst;
import com.example.demo.member.dto.LoginMember;
import com.example.demo.member.dto.LoginRequest;
import com.example.demo.member.service.LoginService;
import com.example.demo.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class loginController {

    private LoginService loginService;

    @PostMapping("/login")
    public String loginForm(@Valid @ModelAttribute LoginRequest loginRequest,
                            BindingResult bindingResult,
                            HttpServletRequest request,
                            @RequestParam(defaultValue = "/") String redirectURL) {

        if(bindingResult.hasErrors())
            return "member/login";

        try{
            LoginMember loginMember = loginService.login(loginRequest);
            HttpSession session = request.getSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        }catch (BuisinessException e){
            bindingResult.reject("loginFail", e.getMessage());
            return "member/login";
        }

        return "redirect:" + redirectURL;
    }

    //로그아웃을 Get매핑으로 해버리면 이미지 태그 하나로 강제 로그아웃이 될 수 도 있다함(CSRF)
    @PostMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session != null) session.invalidate();

        return "redirect:/";
    }



}
