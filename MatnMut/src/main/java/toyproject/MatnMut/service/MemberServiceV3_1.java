package toyproject.MatnMut.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import toyproject.MatnMut.domain.member.Member;
import toyproject.MatnMut.domain.member.MemberRepositoryWithDBV2;
import toyproject.MatnMut.domain.member.MemberRepositoryWithDBV3;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 파라미터로 커넥션 연동, 풀을 고려한 종료
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_1 {
    //transactionManager를 주입 받기
    private final PlatformTransactionManager transactionManager;
    private final MemberRepositoryWithDBV3 memberRepository;

    //커뮤니티 포인트 전송
    public void pointTransfer(String fromNickname, String toNickname, int point) throws SQLException {
        //트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            bizLogic(fromNickname, toNickname, point);
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new IllegalStateException(e);
        }
    }

    private void bizLogic(String fromNickname, String toNickname, int point) throws SQLException {
        Member fromMember = memberRepository.findByNickname(fromNickname);
        Member toMember = memberRepository.findByNickname(toNickname);
        validation(toMember);
        memberRepository.updatePoint(fromNickname, fromMember.getPoint() - point);
        memberRepository.updatePoint(toNickname, toMember.getPoint() + point);
    }

    private void validation(Member toMember) {
        if(toMember.getNickname().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
