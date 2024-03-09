package toyproject.MatnMut.domain.member;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import toyproject.MatnMut.repository.member.MemberRepositoryWithDBV1;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static toyproject.MatnMut.connection.ConnectionConst.*;

@Slf4j
class MemberRepositoryWithDBV1Test {
    MemberRepositoryWithDBV1 repository;
    @BeforeEach
    void beforeEach() throws Exception {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        repository = new MemberRepositoryWithDBV1(dataSource);
    }

    @Test
    void crud() throws SQLException {
        //save
        Member member1 = new Member("cd", "gogi", "massida");
        repository.save(member1);
        Member member2 = new Member("yc", "fish", "good");
        repository.save(member2);

        //findById
        Member findMember = repository.findById(1L);
        log.info("findMember = {}", findMember);

        //findAll
        List<Member> memberList = repository.findAllMember();
        log.info("memberList = {}", memberList);

        //findByLoginId
        String findLoginMember = repository.findByLoginId("gogi");
        log.info("findLoginMember = {}", findLoginMember);

        //delete
        repository.delete(1L);
        assertThatThrownBy(() -> repository.findById(1L))
                .isInstanceOf(NoSuchElementException.class);
        repository.delete(2L);
        assertThatThrownBy(() -> repository.findById(1L))
                .isInstanceOf(NoSuchElementException.class);
    }



}