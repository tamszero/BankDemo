package com.example.demo.transaction;


import com.example.demo.account.AccountService;
import com.example.demo.common.exception.BuisinessException;
import com.example.demo.common.session.SessionConst;
import com.example.demo.member.dto.LoginMember;
import com.example.demo.transaction.dto.DepositRequest;
import com.example.demo.transaction.dto.WithdrawRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountService accountService;

    /**
     * ===입금===
     */
    @GetMapping("/deposit")
    public String depositForm(@SessionAttribute(SessionConst.LOGIN_MEMBER)LoginMember loginMember, Model model) throws BuisinessException {

        model.addAttribute("depositRequest", new DepositRequest());
        model.addAttribute("accounts", accountService.findMyAccounts(loginMember.getId()));
        return "transaction/deposit";
    }

    @PostMapping("/deposit")
    public String deposit(@Valid @ModelAttribute DepositRequest depositRequest, BindingResult bindingResult,
                          @SessionAttribute(SessionConst.LOGIN_MEMBER) LoginMember loginMember,
                          Model model,
                          RedirectAttributes redirectAttributes) throws BuisinessException{

        if(bindingResult.hasErrors()){
            model.addAttribute("accounts", accountService.findMyAccounts(loginMember.getId()));
            return "transaction/deposit";
        }
        try{
            transactionService.deposit(depositRequest, loginMember.getId());
        }catch (BuisinessException e){
            bindingResult.reject("depositFailed", e.getMessage());
            model.addAttribute("account", accountService.findMyAccounts(loginMember.getId()));
            return "transaction/deposit";
        }

        redirectAttributes.addFlashAttribute("message" , "입금이 완료되었습니다");
        return "redirect:/accounts/" + depositRequest.getAccountId();
    }

    /**
     * ===출금---
     */
    @GetMapping("/withdraw")
    public String withdrawForm(@SessionAttribute(SessionConst.LOGIN_MEMBER) LoginMember loginMember,
                               Model model) throws BuisinessException{
        model.addAttribute("withdrawRequest", new WithdrawRequest());
        model.addAttribute("accounts", accountService.findMyAccounts(loginMember.getId()));
        return "transaction/withdraw";
    }

    @PostMapping("/withdraw")
    public String withdraw(@Valid @ModelAttribute WithdrawRequest withdrawRequest,
                           BindingResult bindingResult,
                           @SessionAttribute(SessionConst.LOGIN_MEMBER) LoginMember loginMember,
                           Model model,
                           RedirectAttributes redirectAttributes) throws BuisinessException{

        if(bindingResult.hasErrors()){
            model.addAttribute("account", accountService.findMyAccounts(loginMember.getId()));
            return "transaction/withdraw";
        }
        try{
            transactionService.withdraw(withdrawRequest, loginMember.getId());
        }catch (BuisinessException e){
            bindingResult.reject("withdrawFailed", e.getMessage());
            model.addAttribute("accounts", accountService.findMyAccounts(loginMember.getId()));
            return "transaction/withdraw";
        }

        redirectAttributes.addFlashAttribute("message", "출금이 완료되었습니다");
        return "redirect:/accounts/" + withdrawRequest.getAccountId();

    }

}
