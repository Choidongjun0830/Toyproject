package toyproject.MatnMut.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import toyproject.MatnMut.domain.member.*;
import toyproject.MatnMut.repository.member.MemberRepositoryWithNoDB;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepositoryWithNoDB memberRepositoryWithNoDB;
//    private final MemberValidator memberValidator;

//    @InitBinder
//    public void init(WebDataBinder dataBinder) {
//        dataBinder.addValidators(memberValidator);
//    }

    @GetMapping("/register")
    public String registerForm(@ModelAttribute Member member) {
        return "members/registerMemberForm";
    }

    @PostMapping("/register")
    public String register(@Validated(SaveCheck.class) @ModelAttribute Member member, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "members/registerMemberForm";
        }

        memberRepositoryWithNoDB.save(member);
        return "redirect:/"; //home으로 redirect
    }

    @GetMapping("/member/info")
    public String memberInfo(@ModelAttribute Member member, HttpServletRequest request, Model model) {
        Member loginMember = (Member) request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        model.addAttribute("member", loginMember);
        return "members/memberInfo";
    }

    @GetMapping("/member/passwordUpdate")
    public String passwordUpdateForm(@ModelAttribute Member member) {
        return "members/passwordUpdateForm";
    }

    @PostMapping("/member/passwordUpdate")
    public String passwordUpdate(@Validated @ModelAttribute Member member, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "members/memberInfo";
        }

        memberRepositoryWithNoDB.updatePassword(member.getId(), member);
        return "redirect:/members/member/info";
    }
}
