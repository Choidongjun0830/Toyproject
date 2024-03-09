package toyproject.MatnMut.repository.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.stereotype.Repository;
import toyproject.MatnMut.domain.member.Member;
import toyproject.MatnMut.repository.ex.MyDbException;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * SQLExceptionTranslator 추가
 */
@Slf4j
@Repository
public class MemberRepositoryWithDBV4_2 implements MemberRepository{

    private final DataSource dataSource;
    private final SQLExceptionTranslator exTranslator;
    private static long sequence = 0L;

    public MemberRepositoryWithDBV4_2(DataSource dataSource) {
        this.dataSource = dataSource;
        this.exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
    }
    @Override
    public Member save(Member member){
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
            throw exTranslator.translate("save", sql, e);
        } finally {
            close(con, pstmt, null);
        }
    }
    @Override
    public Member findById(Long id) {
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
            throw exTranslator.translate("findById", sql, e);
        } finally {
            close(con, pstmt, rs);
        }
    }
    @Override
    public List<Member> findAllMember() {
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
            throw exTranslator.translate("findAllMember", sql, e);
        } finally {
            close(con, pstmt, rs);
        }
    }
    @Override
    public String findByLoginId(String loginId) {
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
            throw exTranslator.translate("findByLoginId", sql, e);
        } finally {
            close(con,pstmt,rs);
        }
    }
    @Override
    public Member findByNickname(String nickname) {
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
            throw exTranslator.translate("findByNickName", sql, e);
        } finally {
            close(con, pstmt, rs);
        }
    }
    @Override
    public void updatePoint(String nickname, int point)  {
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
            throw exTranslator.translate("updatePoint", sql, e);
        } finally {
            close(con, pstmt, null);
        }
    }
    @Override
    public void delete(Long id)  {
        String sql = "delete from member where id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw exTranslator.translate("delete", sql, e);
        } finally {
            close(con, pstmt, null);
        }
    }
    @Override
    public void deleteByNickname(String nickname) {
        String sql = "delete from member where nickname = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, nickname);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw exTranslator.translate("deleteByNickname", sql, e);
        } finally {
            close(con, pstmt, null);
        }
    }
    private void close(Connection con, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        //트랜잭션 동기화를 사용하려면 DataSourceUtils를 사용해야함.
        DataSourceUtils.releaseConnection(con, dataSource);
    }

    private Connection getConnection() throws SQLException {
        Connection con = DataSourceUtils.getConnection(dataSource);
        log.info("get connection = {}, class = {}", con, con.getClass());
        return con;
    }
}
