package toyproject.MatnMut.repository.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import toyproject.MatnMut.connection.DBConnectionUtil;
import toyproject.MatnMut.domain.member.Member;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Repository
public class MemberRepositoryWithDBV0 {
    private static long sequence = 0L;

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(id, register_date, name, loginid, password) values (?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DBConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, ++sequence);
            pstmt.setDate(2, Date.valueOf(LocalDate.now()));
            pstmt.setString(3, member.getName());
            pstmt.setString(4, member.getLoginId());
            pstmt.setString(5, member.getPassword());
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
            log.info("save : member = {}", member);
        }
    }

    public Member findById(Long id) throws SQLException {
        String sql = "select * from member where id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);

            rs = pstmt.executeQuery();
            if(rs.next()){
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                member.setRegisterDate(rs.getDate("register_Date").toLocalDate());
                member.setLoginId(rs.getString("loginId"));
                member.setPassword(rs.getString("password"));
                return member;
            } else {
                throw new NoSuchElementException("member not found memberId = " + id);
            }
        } catch (SQLException e) {
            log.error("db error ", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }
    public List<Member> findAllMember() throws SQLException {
        String sql = "select * from member";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);

            rs = pstmt.executeQuery();
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
        } catch (SQLException e) {
            log.error("error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public String findByLoginId(String loginId) throws SQLException {
        String sql = "select name from member where loginId = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            con = DBConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, loginId);

            rs = pstmt.executeQuery();

            if(rs.next()){
                String findUsername = rs.getString("name");
                return findUsername;
            } else {
                throw new NoSuchElementException("member not found loginId = " + loginId);
            }
        } catch (SQLException e) {
            log.error("error", e);
            throw e;
        } finally {
            close(con,pstmt,rs);
        }
    }

//
//    public void updatePassword(Long memberId, Member updateMember) {
//        Member loginMember = findById(memberId);
//        loginMember.setPassword(updateMember.getPassword());
//    }

    public void delete(Long id) throws SQLException {
        String sql = "delete from member where id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = DBConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }
    private void close(Connection con, Statement stmt, ResultSet rs) {
        if(rs != null) {
            try{
                rs.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
        if(stmt != null) {
            try{
                stmt.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
        if(con != null) {
            try{
                con.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
    }
}
