package com.example.demo.transaction.controller;


import com.example.demo.account.AccountService;
import com.example.demo.common.exception.BuisinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.common.session.SessionConst;
import com.example.demo.member.dto.LoginMember;
import com.example.demo.transaction.dto.DepositRequest;
import com.example.demo.transaction.dto.TransferRequest;
import com.example.demo.transaction.dto.WithdrawRequest;
import com.example.demo.transaction.service.TransactionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

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

    // 이체
    @GetMapping("/transfer")
    public String transferFrom(@SessionAttribute(SessionConst.LOGIN_MEMBER) LoginMember loginMember,
                               Model model) throws BuisinessException{

        model.addAttribute("transferRequest", new TransferRequest());
        model.addAttribute("accounts", accountService.findMyAccounts(loginMember.getId()));
        return "transaction/transfer";
    }

    @PostMapping("/transfer/confirm")
    public String transferConfirm(@Valid @ModelAttribute TransferRequest transferRequest,
                                  BindingResult bindingResult,
                                  HttpSession session,
                                  @SessionAttribute(SessionConst.LOGIN_MEMBER) LoginMember loginMember,
                                  Model model) throws BuisinessException{
        if(bindingResult.hasErrors()){
            model.addAttribute("accounts", accountService.findMyAccounts(loginMember.getId()));
            return "transaction/transfer";
        }

        try{

            //수취인 존재 확인 + 예금주명 조회
            model.addAttribute("target", accountService.findTransferTarget(transferRequest.getToAccountNumber()));

        } catch (BuisinessException e){
            bindingResult.rejectValue("toAccountNumber", "notFound", e.getMessage());
            model.addAttribute("accounts", accountService.findMyAccounts(loginMember.getId()));
            return "transaction/transfer";
        }

        //멱등성 토큰 발급
        String token = UUID.randomUUID().toString();
        session.setAttribute(SessionConst.TRANSFER_TOKEN, token);
        model.addAttribute("token", token);
        return "transaction/transfer-confirm";
    }

    @PostMapping("/transfer")
    public String transfer(@Valid @ModelAttribute TransferRequest transferRequest,
                           BindingResult bindingResult,
                           @RequestParam(required = false) String token,
                           @SessionAttribute(SessionConst.LOGIN_MEMBER) LoginMember loginMember,
                           Model model,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) throws BuisinessException{
        if(bindingResult.hasErrors()){
            model.addAttribute("accounts", accountService.findMyAccounts(loginMember.getId()));
            return "transaction/transfer";
        }

        // 멱등성 검증
        String saved = (String) session.getAttribute(SessionConst.TRANSFER_TOKEN);
        if(saved == null || !saved.equals(token)){
            bindingResult.reject("transfer", ErrorCode.DUPLICATE_REQUEST.getMessage());
            model.addAttribute("accounts", accountService.findMyAccounts(loginMember.getId()));
            return "transaction/transfer";
        }
        session.removeAttribute(SessionConst.TRANSFER_TOKEN);

        try{
            transactionService.transfer(transferRequest, loginMember.getId());
        }catch (BuisinessException e){
            bindingResult.reject("transferFailed", e.getMessage());
            model.addAttribute("accounts", accountService.findMyAccounts(loginMember.getId()));
            return "transaction/transfer";
        }

        redirectAttributes.addFlashAttribute("message", "이체가 완료되었습니다");
        return "redirect:/accounts/" + transferRequest.getFromAccountId();
    }




}

