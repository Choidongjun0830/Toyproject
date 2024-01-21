package toyproject.wintersnack.domain.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toyproject.wintersnack.domain.member.Member;
import toyproject.wintersnack.domain.member.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public Member login(String loginId, String password) {
        Optional<Member> findMember = memberRepository.findByLoginId(loginId);
        Member member = findMember.get();
        if(member.getPassword().equals(password)) {
            return member; //로그인 성공
        } else{
            return null; //로그인 실패
        }
    }
}
