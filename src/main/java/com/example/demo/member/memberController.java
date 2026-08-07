package com.example.demo.member;


import com.example.demo.common.exception.BuisinessException;
import com.example.demo.member.dto.MemberJoinRequest;
import com.example.demo.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class memberController {

    private final MemberService memberService;

    @GetMapping("/join")
    public String joinForm(Model model){
        model.addAttribute("memberJoinRequest", new MemberJoinRequest());
        return "member/join";
    }

    @PostMapping("/join")
    public String join(@Valid @ModelAttribute MemberJoinRequest memberJoinRequest, BindingResult bindingResult){

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

        return "redirect:/login";    }
}
