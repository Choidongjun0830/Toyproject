package toyproject.MatnMut.repository.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import toyproject.MatnMut.domain.member.Member;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Repository
public class MemberRepositoryWithNoDB {

    private Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    public Member save(Member member) {
        member.setId(++sequence);
        member.setRegisterDate(LocalDate.now());
        log.info("save : member = {}", member);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }

    public List<Member> findAllMember() {
        return new ArrayList<>(store.values());
    }

    public Optional<Member> findByLoginId(String loginId) {
        List<Member> allMember = findAllMember();
        for (Member member : allMember) {
            if(member.getLoginId().equals(loginId)) {
                return Optional.of(member);
            }
        }
        return Optional.empty();
    }

    public Optional<Member> findByUserId(String userId) {
        List<Member> allMember = findAllMember();
        for (Member member : allMember) {
            if(member.getId().equals(userId)) {
                return Optional.of(member);
            }
        }
        return Optional.empty();
    }

    public void updatePassword(Long memberId, Member updateMember) {
        Member loginMember = findById(memberId);
        loginMember.setPassword(updateMember.getPassword());
    }
}
