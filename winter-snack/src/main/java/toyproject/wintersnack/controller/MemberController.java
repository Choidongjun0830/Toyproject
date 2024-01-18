package toyproject.wintersnack.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import toyproject.wintersnack.domain.member.Member;
import toyproject.wintersnack.domain.member.MemberRepository;

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
}
