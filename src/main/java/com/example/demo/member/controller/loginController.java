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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class loginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@RequestParam(defaultValue = "/") String redirectURL, Model model){
        model.addAttribute("loginRequest", new LoginRequest());
        model.addAttribute("redirectURL", redirectURL);
        return "member/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginRequest") LoginRequest loginRequest,
                            BindingResult bindingResult,
                            HttpServletRequest request,
                            Model model,
                            @RequestParam(defaultValue = "/") String redirectURL) {
        model.addAttribute("redirectURL", redirectURL); //로그인 실패시에도 유지

        if(bindingResult.hasErrors()){
            System.out.println("=== 검증 실패: " + bindingResult.getAllErrors());
            model.addAttribute("redirectURL", redirectURL);
            return "member/login";
        }


        try{
            LoginMember loginMember = loginService.login(loginRequest);
            System.out.println("=== 로그인 성공: " + loginMember);
            HttpSession session = request.getSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        }catch (BuisinessException e){
            bindingResult.reject("loginFail", e.getMessage());
            System.out.println("=== 로그인 실패: " + e.getMessage());

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
