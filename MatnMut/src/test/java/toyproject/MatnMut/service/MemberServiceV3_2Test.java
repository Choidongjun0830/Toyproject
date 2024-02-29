package toyproject.MatnMut.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import toyproject.MatnMut.domain.member.Member;
import toyproject.MatnMut.domain.member.MemberRepositoryWithDBV2;
import toyproject.MatnMut.domain.member.MemberRepositoryWithDBV3;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static toyproject.MatnMut.connection.ConnectionConst.*;

class MemberServiceV3_2Test {
    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";
    private MemberRepositoryWithDBV3 memberRepository;
    private MemberServiceV3_2 memberService;

    @BeforeEach
    void before() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL,
                USERNAME, PASSWORD);
        //JDBC용 트랜잭션 매니저인 DataSourceTransactionManager를 서비스에 주입한다.
        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        memberRepository = new MemberRepositoryWithDBV3(dataSource);
        memberService = new MemberServiceV3_2(transactionManager, memberRepository);
    }

    @AfterEach
    void after() throws SQLException {
        memberRepository.deleteByNickname("memberA");
        memberRepository.deleteByNickname("memberB");
        memberRepository.deleteByNickname("ex");
    }

    @Test
    @DisplayName("정상 이체")
    void accountTransfer() throws SQLException {
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
        assertThat(findMemberB.getPoint()).isEqualTo(12000);
    }

    @Test
    @DisplayName("이체중 예외 발생")
    void accountTransferEx() throws SQLException {
        //given
        Member memberA = new Member(MEMBER_A, "dj", "gogi", MEMBER_A, 10000);
        Member memberEx = new Member(MEMBER_EX, "yc", "fish", MEMBER_EX, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberEx); //when
        assertThatThrownBy(() ->
                memberService.pointTransfer(memberA.getNickname(), memberEx.getNickname(),
                        2000))
                .isInstanceOf(IllegalStateException.class);
        //then
        Member findMemberA = memberRepository.findByNickname(memberA.getNickname());
        Member findMemberEx = memberRepository.findByNickname(memberEx.getNickname());
        //memberA의 돈이 롤백 되어야함
        assertThat(findMemberA.getPoint()).isEqualTo(10000);
        assertThat(findMemberEx.getPoint()).isEqualTo(10000);
    }
}
