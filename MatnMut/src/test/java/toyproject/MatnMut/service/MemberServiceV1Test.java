package toyproject.MatnMut.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import toyproject.MatnMut.connection.ConnectionConst;
import toyproject.MatnMut.domain.member.Member;
import toyproject.MatnMut.domain.member.MemberRepositoryWithDBV1;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static toyproject.MatnMut.connection.ConnectionConst.*;

/**
 * 기본 동작, 트랜잭션 없어서 문제 발생
 */
class MemberServiceV1Test {

    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    private MemberRepositoryWithDBV1 memberRepository;
    private MemberServiceV1 memberService;

    @BeforeEach
    void before() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        memberRepository = new MemberRepositoryWithDBV1(dataSource);
        memberService = new MemberServiceV1(memberRepository);
    }
    @AfterEach
    void after() throws SQLException {
        memberRepository.delete(0L);
        memberRepository.delete(1L);
        memberRepository.delete(5L);
    }

    @Test
    @DisplayName("정상 이체")
    void pointTransfer() throws SQLException {
        //given
        Member memberA = new Member(MEMBER_A, "dj", "gogi", MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, "yc", "fish", MEMBER_B, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when
        memberService.pointTransfer(memberA.getNickname(),
                memberB.getNickname(), 2000);
        //then
        Member findMemberA = memberRepository.findByNickname(memberA.getNickname());
        Member findMemberB = memberRepository.findByNickname(memberB.getNickname());
        assertThat(findMemberA.getPoint()).isEqualTo(8000);
    }

    @Test
    @DisplayName("이체중 예외 발생")
    void pointTransferEx() throws SQLException {
        //given
        Member memberA = new Member(MEMBER_A, "dj", "gogi", MEMBER_A, 10000);
        Member memberEx = new Member(MEMBER_EX, "yc", "fish", MEMBER_EX, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberEx);
        //when
        assertThatThrownBy(() ->
                memberService.pointTransfer(memberA.getNickname(), memberEx.getNickname(),
                        2000))
                .isInstanceOf(IllegalStateException.class);
        //then
        Member findMemberA = memberRepository.findByNickname(memberA.getNickname());
        Member findMemberEx = memberRepository.findByNickname(memberEx.getNickname());
        //memberA의 돈만 2000원 줄었고, ex의 돈은 10000원 그대로이다.
        assertThat(findMemberA.getPoint()).isEqualTo(8000);
        assertThat(findMemberEx.getPoint()).isEqualTo(10000);
    }

}