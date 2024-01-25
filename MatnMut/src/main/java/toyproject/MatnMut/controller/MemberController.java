package toyproject.MatnMut.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import toyproject.MatnMut.domain.member.Member;
import toyproject.MatnMut.domain.member.MemberRepository;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/register")
    public String registerForm(@ModelAttribute Member member) {
        return "members/registerMemberForm";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute Member member, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "members/registerMemberForm";
        }

        memberRepository.save(member);
        return "redirect:/"; //home으로 redirect
    }

    @GetMapping("/member/info")
    public String memberInfo(@ModelAttribute Member member, HttpServletRequest request, Model model) {
        Member loginMember = (Member) request.getSession().getAttribute(SessionConst.LOGIN_MEMBER);
        model.addAttribute("member", loginMember);
        return "members/memberInfo";
    }
}
