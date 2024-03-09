package toyproject.MatnMut.repository.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.stereotype.Repository;
import toyproject.MatnMut.domain.member.Member;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * JdbcTemplate 사용
 */
@Slf4j
public class MemberRepositoryWithDBV5 implements MemberRepository{

    private final JdbcTemplate template;

    private static long sequence = 0L;

    public MemberRepositoryWithDBV5(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }
    @Override
    public Member save(Member member){
        String sql = "insert into member(id, register_date, name, loginid, password, nickname, point) values (?, ?, ?, ?, ?, ?, ?)";
        template.update(sql, ++sequence, Date.valueOf(LocalDate.now()), member.getName(), member.getLoginId(), member.getPassword(), member.getNickname(), member.getPoint());
        return member;
    }
    @Override
    public Member findById(Long id) {
        String sql = "select * from member where id = ?";
        return template.queryForObject(sql, memberRowMapper(), id);
    }
    @Override
    public List<Member> findAllMember() {
        String sql = "select * from member";
        return template.queryForObject(sql, memberListRowMapper());
    }
    @Override
    public String findByLoginId(String loginId) {
        String sql = "select name from member where loginId = ?";
        return template.queryForObject(sql, String.class, loginId);
    }
    @Override
    public Member findByNickname(String nickname) {
        String sql = "select * from member where nickname = ?";
        return template.queryForObject(sql, memberRowMapper(), nickname);
    }
    @Override
    public void updatePoint(String nickname, int point)  {
        String sql = "update member set point = ? where nickname = ?";
        template.update(sql, point, nickname);
    }
    @Override
    public void delete(Long id)  {
        String sql = "delete from member where id = ?";
        template.update(sql, id);
    }

    public void deleteByNickname(String nickname) {
        String sql = "delete from member where nickname = ?";
        template.update(sql,nickname);
    }

    private RowMapper<Member> memberRowMapper(){
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setId(rs.getLong("id"));
            member.setName(rs.getString("name"));
            member.setRegisterDate(rs.getDate("register_Date").toLocalDate());
            member.setLoginId(rs.getString("loginId"));
            member.setPassword(rs.getString("password"));
            member.setPoint(rs.getInt("point"));
            member.setNickname(rs.getString("nickname"));
            return member;
        };
    }

    private RowMapper<List> memberListRowMapper() {
        return (rs, rowNum) -> {
            List<Member> allMember = new ArrayList<>();
            while(rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                member.setRegisterDate(rs.getDate("register_Date").toLocalDate());
                member.setLoginId(rs.getString("loginId"));
                member.setPassword(rs.getString("password"));
                allMember.add(member);
            }
            return allMember;
        };
    }
}
