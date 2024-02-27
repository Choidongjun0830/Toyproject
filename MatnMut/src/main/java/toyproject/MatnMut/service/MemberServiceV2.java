package toyproject.MatnMut.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import toyproject.MatnMut.domain.member.Member;
import toyproject.MatnMut.domain.member.MemberRepositoryWithDBV1;
import toyproject.MatnMut.domain.member.MemberRepositoryWithDBV2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 파라미터로 커넥션 연동, 풀을 고려한 종료
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {
    private final MemberRepositoryWithDBV2 memberRepository;
    private final DataSource dataSource;

    //커뮤니티 포인트 전송
    public void pointTransfer(String fromNickname, String toNickname, int point) throws SQLException {

        Connection con = dataSource.getConnection();
        try {
            con.setAutoCommit(false);
            bizLogic(con, fromNickname, toNickname, point);
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw new IllegalStateException(e);
        } finally {
            release(con);
        }
    }

    private void bizLogic(Connection con, String fromNickname, String toNickname, int point) throws SQLException {
        Member fromMember = memberRepository.findByNickname(con, fromNickname);
        Member toMember = memberRepository.findByNickname(con, toNickname);
        validation(toMember);
        memberRepository.updatePoint(con, fromNickname, fromMember.getPoint() - point);
        memberRepository.updatePoint(con, toNickname, toMember.getPoint() + point);
    }

    private void validation(Member toMember) {
        if(toMember.getNickname().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }

    private void release(Connection con) {
        if (con != null) {
            try {
                con.setAutoCommit(true); //커넥션 풀 고려
                con.close();
            } catch (Exception e) {
                log.info("error", e);
            }
        }
    }
}
