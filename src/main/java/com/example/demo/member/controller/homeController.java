package com.example.demo.member.controller;

import com.example.demo.account.Account;
import com.example.demo.account.AccountService;
import com.example.demo.account.dto.AccountResponse;
import com.example.demo.common.exception.BuisinessException;
import com.example.demo.common.session.SessionConst;
import com.example.demo.member.dto.LoginMember;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.eclipse.jdt.internal.compiler.env.IModule;
import org.hibernate.validator.cfg.defs.Mod10CheckDef;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class homeController {

    private final AccountService accountService;

    @GetMapping("/")
    public String home( @SessionAttribute(name = SessionConst.LOGIN_MEMBER,
                       required = false) LoginMember loginMember,
                        Model model) throws BuisinessException {

        if(loginMember == null){
            return "home"; //비로그인 화면
        }

        List<AccountResponse> accounts = accountService.findMyAccounts(loginMember.getId());
        BigDecimal total = accounts.stream()
                .map(AccountResponse::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("accounts", accounts);
        model.addAttribute("totalBalance", total);

        return "home";

    }


}
