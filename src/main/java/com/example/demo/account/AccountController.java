package com.example.demo.account;

import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import com.example.demo.account.dto.AccountCreateRequest;
import com.example.demo.common.exception.BuisinessException;
import com.example.demo.common.session.SessionConst;
import com.example.demo.member.dto.LoginMember;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.eclipse.tags.shaded.org.apache.xpath.operations.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public String list(@SessionAttribute(SessionConst.LOGIN_MEMBER)LoginMember loginMember, Model model){
        model.addAttribute("accounts", accountService.findMyAccounts(loginMember.getId()));
        return "account/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, @SessionAttribute(SessionConst.LOGIN_MEMBER) LoginMember loginMember, Model model) throws BuisinessException {
        model.addAttribute("account", accountService.findMyAccount(id, loginMember.getId()));
        return "account/detail";
    }

    @GetMapping("/new")
    public String createForm(Model model){
        model.addAttribute("accountCreateRequest",new AccountCreateRequest());
        return "account/new";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("accountCreateRequest")AccountCreateRequest req,
                         BindingResult bindingResult,
                         HttpSession session){
        if(bindingResult.hasErrors()){
            return "account/new";
        }

        LoginMember loginMember = (LoginMember) session.getAttribute(SessionConst.LOGIN_MEMBER);

        try{
            accountService.create(req, loginMember.getId());
        }catch (BuisinessException e){
            bindingResult.reject("createdFailed", e.getMessage());
            return "account/new";
        }

        return "redirect:/accounts";
    }
}
