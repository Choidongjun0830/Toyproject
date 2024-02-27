package toyproject.MatnMut.service;

import lombok.RequiredArgsConstructor;
import toyproject.MatnMut.domain.member.Member;
import toyproject.MatnMut.domain.member.MemberRepositoryWithDBV1;

import java.sql.SQLException;

/**
 * 트랜잭션 없이 단순하게 비즈니스 로직만
 */
@RequiredArgsConstructor
public class MemberServiceV1 {
    private final MemberRepositoryWithDBV1 memberRepository;

    //커뮤니티 포인트 전송
    public void pointTransfer(String fromNickname, String toNickname, int point) throws SQLException {
        Member fromMember = memberRepository.findByNickname(fromNickname);
        Member toMember = memberRepository.findByNickname(toNickname);

        memberRepository.updatePoint(fromNickname, fromMember.getPoint() - point);
        memberRepository.updatePoint(toNickname, toMember.getPoint() + point);
    }

    private void validation(Member toMember) {
        if(toMember.getNickname().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
