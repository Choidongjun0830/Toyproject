package toyproject.MatnMut.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import toyproject.MatnMut.domain.member.Member;
import toyproject.MatnMut.repository.member.MemberRepository;
import toyproject.MatnMut.repository.member.MemberRepositoryWithDBV3;

import java.sql.SQLException;

/**
 * 예외 누수 문제 해결
 * SQLException 제거
 *
 * MemberRepository 인터페이스 의존
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV4 {
    private final MemberRepository memberRepository;

    //커뮤니티 포인트 전송
    @Transactional
    public void pointTransfer(String fromNickname, String toNickname, int point) {
        bizLogic(fromNickname,toNickname, point);
    }

    private void bizLogic(String fromNickname, String toNickname, int point) {
        Member fromMember = memberRepository.findByNickname(fromNickname);
        Member toMember = memberRepository.findByNickname(toNickname);
        memberRepository.updatePoint(fromNickname, fromMember.getPoint() - point);
        validation(toMember);
        memberRepository.updatePoint(toNickname, toMember.getPoint() + point);
    }

    private void validation(Member toMember) {
        if(toMember.getNickname().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
