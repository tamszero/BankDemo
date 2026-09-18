package com.example.demo.account;

import com.example.demo.account.dto.AccountCreateRequest;
import com.example.demo.common.exception.BuisinessException;
import com.example.demo.common.session.SessionConst;
import com.example.demo.member.dto.LoginMember;
import com.example.demo.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.eclipse.tags.shaded.org.apache.xpath.operations.Mod;
import org.h2.engine.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    @GetMapping
    public String list(@SessionAttribute(SessionConst.LOGIN_MEMBER)LoginMember loginMember, Model model) throws BuisinessException {
        model.addAttribute("accounts", accountService.findMyAccounts(loginMember.getId()));
        return "account/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, @SessionAttribute(SessionConst.LOGIN_MEMBER) LoginMember loginMember, Model model) throws BuisinessException {
        model.addAttribute("account", accountService.findMyAccount(id, loginMember.getId()));
        return "account/detail";
    }
    @GetMapping("/{id}/histories")
    public String showHistory(@PathVariable Long id,
                              @SessionAttribute(SessionConst.LOGIN_MEMBER) LoginMember loginMember,
                              Model model,
                              @RequestParam(defaultValue = "1") int page) throws BuisinessException{
        model.addAttribute("account", accountService.findMyAccount(id, loginMember.getId()));
        model.addAttribute("histories", transactionService.findHistories(id,loginMember.getId(), page));
        model.addAttribute("currentPage", page);

        return "account/histories";

    }

    @PostMapping("/{id}/close")
    public String close(@PathVariable Long id,
                        @RequestParam String password,
                        @SessionAttribute(SessionConst.LOGIN_MEMBER) LoginMember loginMember,
                        RedirectAttributes redirectAttributes
                        ) {

        try{
            accountService.close(id, loginMember.getId(), password);
        }catch (BuisinessException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/accounts/" + id;
        }

        redirectAttributes.addFlashAttribute("message", "계좌가 정상적으로 해지되었습니다.");
        return "redirect:/accounts";

    }

    @GetMapping("/new")
    public String createForm(Model model){
        model.addAttribute("accountCreateRequest",new AccountCreateRequest());
        return "account/new";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("accountCreateRequest")AccountCreateRequest req,
                         BindingResult bindingResult,
                         @SessionAttribute(SessionConst.LOGIN_MEMBER) LoginMember loginMember) {
        if(bindingResult.hasErrors()){
            return "account/new";
        }

        try{
            accountService.create(req, loginMember.getId());
        }catch (BuisinessException e){
            bindingResult.reject("createdFailed", e.getMessage());
            return "account/new";
        }

        return "redirect:/accounts";
    }



}
