package toyproject.MatnMut.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import toyproject.MatnMut.domain.member.Member;
import toyproject.MatnMut.repository.member.MemberRepository;
import toyproject.MatnMut.repository.member.MemberRepositoryWithDBV3;
import toyproject.MatnMut.repository.member.MemberRepositoryWithDBV4_1;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
class MemberServiceV4Test {
    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberServiceV4 memberService;

    @AfterEach
    void after() throws SQLException {
        memberRepository.deleteByNickname("memberA");
        memberRepository.deleteByNickname("memberB");
        memberRepository.deleteByNickname("ex");
    }
    @TestConfiguration
    static class TestConfig {
        private final DataSource dataSource;

        public TestConfig(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Bean
        MemberRepository memberRepository() {
            return new MemberRepositoryWithDBV4_1(dataSource);
        }

        @Bean
        MemberServiceV4 memberServiceV4() {
            return new MemberServiceV4(memberRepository());
        }
    }

    @Test
    void AopCheck() {
        log.info("memberService class = {}", memberService.getClass());
        log.info("memberRepository class = {}", memberRepository.getClass());
        Assertions.assertThat(AopUtils.isAopProxy(memberService)).isTrue();
        Assertions.assertThat(AopUtils.isAopProxy(memberRepository)).isFalse();

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
