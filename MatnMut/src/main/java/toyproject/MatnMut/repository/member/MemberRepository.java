package toyproject.MatnMut.repository.member;

import toyproject.MatnMut.domain.member.Member;

import java.util.List;

public interface MemberRepository {

    Member save(Member member);
    Member findById(Long id);
    List<Member> findAllMember();
    String findByLoginId(String loginId);
    Member findByNickname(String nickname);
    void updatePoint(String nickname, int point);
    void delete(Long id);
    void deleteByNickname(String nickname);
}
