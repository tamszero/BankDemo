package com.example.demo.member.controller;


import com.example.demo.common.exception.BuisinessException;
import com.example.demo.common.session.SessionConst;
import com.example.demo.member.dto.JoinRequest;
import com.example.demo.member.dto.LoginMember;
import com.example.demo.member.dto.PasswordChangeRequest;
import com.example.demo.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.eclipse.tags.shaded.org.apache.xpath.operations.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class memberController {

    private final MemberService memberService;

    // === 마이페이지 ===
    @GetMapping("/mypage")
    public String myPage(@SessionAttribute(SessionConst.LOGIN_MEMBER) LoginMember loginMember,
                         Model model) throws BuisinessException{
        model.addAttribute("PasswordChangeRequest", new PasswordChangeRequest());
        model.addAttribute("member", memberService.findById(loginMember.getId()));
        return "member/mypage";
    }

    @PostMapping("/password")
    public String changePassword(@Valid @ModelAttribute("PasswordChangeRequest") PasswordChangeRequest passwordChangeRequest,
                                 BindingResult bindingResult,
                                 @SessionAttribute(SessionConst.LOGIN_MEMBER) LoginMember loginMember,
                                 HttpServletRequest request,
                                 Model model,
                                 RedirectAttributes redirectAttributes) throws BuisinessException{
        if(bindingResult.hasErrors()){
            model.addAttribute("member", memberService.findById(loginMember.getId()));
            return "member/mypage";
        }
        try{
            memberService.changePassword(passwordChangeRequest, loginMember.getId());
        }catch (BuisinessException e){
            bindingResult.reject("changeFailed", e.getMessage());
            model.addAttribute("member", memberService.findById(loginMember.getId()));
            return "member/mypage";
        }

        // 비밀번호 재생성 후 기존 세션 끊고 재로그인 유도
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
        redirectAttributes.addFlashAttribute("message", "비밀번호가 변경되었습니다. 다시 로그인해주세요.");
        return "redirect:/members/login";
    }

    @GetMapping("/join")
    public String joinForm(Model model){
        model.addAttribute("memberJoinRequest", new JoinRequest());
        return "member/join";
    }

    @PostMapping("/join")
    public String join(@Valid @ModelAttribute("memberJoinRequest") JoinRequest memberJoinRequest, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "member/join";
        }
        try{
            memberService.join((memberJoinRequest));
        }catch (BuisinessException e){
            bindingResult.reject("joinFailed", e.getMessage());
            return "member/join";
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        return "redirect:/members/login";    }
}
