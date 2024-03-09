package toyproject.MatnMut.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import toyproject.MatnMut.repository.member.MemberRepositoryWithDBV0;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class MemberRepositoryWithDBV0Test {

    MemberRepositoryWithDBV0 repository = new MemberRepositoryWithDBV0();

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