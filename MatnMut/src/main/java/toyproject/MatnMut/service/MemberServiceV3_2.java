package toyproject.MatnMut.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import toyproject.MatnMut.domain.member.Member;
import toyproject.MatnMut.domain.member.MemberRepositoryWithDBV3;

import java.sql.SQLException;

/**
 * 트랜잭션 - 트랜잭션 템플릿
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_2 {
    //transactionManager를 주입 받기
    private final TransactionTemplate txTemplate;
    private final MemberRepositoryWithDBV3 memberRepository;
    public MemberServiceV3_2(PlatformTransactionManager transactionManager, MemberRepositoryWithDBV3 memberRepository) {
        this.txTemplate = new TransactionTemplate(transactionManager);
        this.memberRepository = memberRepository;
    }

    //커뮤니티 포인트 전송
    public void pointTransfer(String fromNickname, String toNickname, int point) throws SQLException {
        txTemplate.executeWithoutResult((staus) -> {
            try {
                bizLogic(fromNickname, toNickname, point);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        });
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
