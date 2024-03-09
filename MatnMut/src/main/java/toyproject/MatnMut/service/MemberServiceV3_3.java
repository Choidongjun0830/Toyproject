package toyproject.MatnMut.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import toyproject.MatnMut.domain.member.Member;
import toyproject.MatnMut.repository.member.MemberRepositoryWithDBV3;

import java.sql.SQLException;

/**
 * 트랜잭션 - @Transactional AOP
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_3 {
    private final MemberRepositoryWithDBV3 memberRepository;

    //커뮤니티 포인트 전송
    @Transactional
    public void pointTransfer(String fromNickname, String toNickname, int point) throws SQLException {
        bizLogic(fromNickname,toNickname, point);
    }

    private void bizLogic(String fromNickname, String toNickname, int point) throws SQLException {
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
