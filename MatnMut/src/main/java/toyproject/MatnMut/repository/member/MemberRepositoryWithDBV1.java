package toyproject.MatnMut.repository.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;
import toyproject.MatnMut.connection.DBConnectionUtil;
import toyproject.MatnMut.domain.member.Member;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Repository
public class MemberRepositoryWithDBV1 {

    private final DataSource dataSource;
    private static long sequence = 0L;

    public MemberRepositoryWithDBV1(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(id, register_date, name, loginid, password, nickname, point) values (?, ?, ?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, ++sequence);
            pstmt.setDate(2, Date.valueOf(LocalDate.now()));
            pstmt.setString(3, member.getName());
            pstmt.setString(4, member.getLoginId());
            pstmt.setString(5, member.getPassword());
            pstmt.setString(6, member.getNickname());
            pstmt.setInt(7, member.getPoint());

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
            con = getConnection();
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
                member.setPoint(rs.getInt("point"));
                member.setNickname(rs.getString("nickname"));
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
            con = getConnection();
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
            con = getConnection();
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

    public Member findByNickname(String nickname) throws SQLException {
        String sql = "select * from member where nickname = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, nickname);

            rs = pstmt.executeQuery();
            if(rs.next()){
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                member.setRegisterDate(rs.getDate("register_Date").toLocalDate());
                member.setLoginId(rs.getString("loginId"));
                member.setPassword(rs.getString("password"));
                member.setPoint(rs.getInt("point"));
                member.setNickname(rs.getString("nickname"));
                return member;
            } else {
                throw new NoSuchElementException("member not found nickname = " + nickname);
            }
        } catch (SQLException e) {
            log.error("db error ", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }


    public void updatePoint(String nickname, int point) throws SQLException {
        String sql = "update member set point = ? where nickname = ?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, point);
            pstmt.setString(2, nickname);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize = {}", resultSize);
        } catch (SQLException e) {
            log.error("DB error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "delete from member where id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
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
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
    }

    private Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("get connection = {}, class = {}", con, con.getClass());
        return con;
    }
}
