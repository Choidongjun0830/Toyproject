package toyproject.MatnMut.domain.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toyproject.MatnMut.domain.member.Member;
import toyproject.MatnMut.repository.member.MemberRepositoryWithNoDB;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepositoryWithNoDB memberRepositoryWithNoDB;

    public Member login(String loginId, String password) {
        Optional<Member> findMember = memberRepositoryWithNoDB.findByLoginId(loginId);
        Member member = findMember.get();
        if(member.getPassword().equals(password)) {
            return member; //로그인 성공
        } else{
            return null; //로그인 실패
        }
    }
}
