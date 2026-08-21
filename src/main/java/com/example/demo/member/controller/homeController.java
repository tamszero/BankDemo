package com.example.demo.member.controller;

import com.example.demo.common.session.SessionConst;
import com.example.demo.member.dto.LoginMember;
import jakarta.servlet.http.HttpSession;
import org.hibernate.validator.cfg.defs.Mod10CheckDef;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class homeController {

    @GetMapping("/")
    public String home(HttpSession session, Model model){
        LoginMember loginMember = (LoginMember) session.getAttribute(SessionConst.LOGIN_MEMBER);
        model.addAttribute("loginMember", loginMember);
        return "home";

    }
}
